import {Component, ElementRef, Inject, LOCALE_ID, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Part} from "../../../models/Part";
import {fromEvent, Subscription} from "rxjs";
import {PartsService} from "../../../services/parts.service";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {finalize, map, takeUntil} from "rxjs/operators";
import {CarService} from "../../../services/car.service";
import {Car} from "../../../models/Car";
import {formatDate} from "@angular/common";
import {LabelComponent} from "../label/label.component";
import {LabelService} from "../../../services/label.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {NewCarService} from "../../../services/new-car.service";
import {NewCar} from "../../../modules/auth/Models/NewCar";

@Component({
  selector: 'app-label-generation',
  templateUrl: './label-generation.component.html',
  styleUrls: ['./label-generation.component.css']
})
export class LabelGenerationComponent implements OnInit, OnDestroy {

  parts: Part[] = [];
  partSearchSubscription: Subscription;
  pageNumber = 0;
  id = null;
  scrollSubscription: Subscription;

  loading = false;

  @ViewChild("label") label: LabelComponent;


  formGroup: FormGroup;

  constructor(private partService: PartsService,
              public newCarService:NewCarService,
              @Inject(LOCALE_ID) private locale: string
    , private labelService: LabelService
    , private snackBar: MatSnackBar) {

    this.formGroup = new FormGroup({
      partName: new FormControl("", Validators.required),
      car: new FormControl(""),
      warehouseNumber: new FormControl("", Validators.required),
      size: new FormControl(600, [Validators.required,Validators.min(100)])
    });

    if(localStorage.getItem("size")){
      this.formGroup.controls['size'].setValue(parseInt(localStorage.getItem("size")));
    }


    this.partSearchSubscription = this.formGroup.controls['partName'].valueChanges.subscribe(value => {

      let index = this.parts.findIndex(part => part.partName == value);
      if (index != -1) {
        if (!this.parts[index].warehouseNumber) {
          this.formGroup.controls['warehouseNumber'].enable();
          this.formGroup.controls['warehouseNumber'].setValue("");
        }
      } else {
        this.formGroup.controls['warehouseNumber'].enable();
      }

      this.pageNumber = 0;
      this.searchParts();
    });

    this.searchParts();
    this.newCarService.getAll();

  }

  ngOnDestroy(): void {
    if (this.scrollSubscription) {
      this.scrollSubscription.unsubscribe();
    }

    this.partSearchSubscription.unsubscribe();
  }

  ngOnInit(): void {
  }

  searchParts() {
    this.partService.getParts(this.formGroup.controls['partName'].value, this.pageNumber).subscribe(response => {
      if (this.pageNumber > 0) {
        this.parts = this.parts.concat(response.parts)
      } else {
        this.parts = response.parts
      }
    })
  }

  valueFromCar(car: NewCar) {
    let date = formatDate(car.dateAdded, "yyMMdd", this.locale);
    return `${car.carName}  ${date}`
  }

  selectedPart(selectedPart: Part) {

    if (selectedPart.warehouseNumber) {
      this.formGroup.controls['warehouseNumber'].setValue(selectedPart.warehouseNumber);
      this.formGroup.controls['warehouseNumber'].disable();
    }
  }

  printLabel() {
    this.formGroup.controls['warehouseNumber'].enable();
    this.formGroup.markAllAsTouched();
    if (this.formGroup.valid) {

      localStorage.setItem("size",this.formGroup.controls['size'].value);

      this.loading = true;

      this.labelService.saveLabel(this.formGroup.controls['partName'].value, this.formGroup.controls['car'].value, this.formGroup.controls['warehouseNumber'].value)
        .pipe(finalize(() => {
          this.loading = false;
        }))
        .subscribe(response => {
          this.label.id = response.id;
          this.label.print();
          this.formGroup.controls['partName'].setValue("");
          this.formGroup.controls['partName'].markAsUntouched();

          this.formGroup.controls['warehouseNumber'].reset();
        }, error => {
          this.snackBar.open("Błąd podczas dodawania etykiety", "OK", {
            duration: 3000
          })
        });


    }
  }

  setWarehouseNumber(value) {
    this.formGroup.controls['warehouseNumber'].enable();
    this.formGroup.controls['warehouseNumber'].setValue(value);
  }




  @ViewChild('auto') statesAutocompleteRef: MatAutocomplete;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger: MatAutocompleteTrigger;

  autocompleteScroll() {
    setTimeout(() => {
      if (
        this.statesAutocompleteRef &&
        this.autocompleteTrigger &&
        this.statesAutocompleteRef.panel
      ) {
        this.scrollSubscription = fromEvent(this.statesAutocompleteRef.panel.nativeElement, 'scroll')
          .pipe(
            map(x => this.statesAutocompleteRef.panel.nativeElement.scrollTop),
            takeUntil(this.autocompleteTrigger.panelClosingActions)
          )
          .subscribe(x => {
            const scrollTop = this.statesAutocompleteRef.panel.nativeElement
              .scrollTop;
            const scrollHeight = this.statesAutocompleteRef.panel.nativeElement
              .scrollHeight;
            const elementHeight = this.statesAutocompleteRef.panel.nativeElement
              .clientHeight;
            const atBottom = scrollHeight === scrollTop + elementHeight;
            if (atBottom) {
              this.pageNumber = this.pageNumber + 1;
              this.searchParts();
            }
          });
      }
    });
  }

}
