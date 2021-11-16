import {Component, HostListener, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Label} from "../../../models/Label";
import {LabelService} from "../../../services/label.service";
import {finalize} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";
import {LabelComponent} from "../label/label.component";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {PartDialog} from "../parts/update-part/update-part.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-saved-labels',
  templateUrl: './saved-labels.component.html',
  styleUrls: ['./saved-labels.component.css']
})
export class SavedLabelsComponent implements OnInit,OnDestroy {

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key == "Enter") {
      this.getLabel();
    }
  }

  updatingLabel = false;
  deletingLabel = false;
  label: Label = null;
  searching = false;
  notFound = false;
  searchFormControl: FormControl = new FormControl(1, [Validators.required, Validators.min(1)]);
  formGroup: FormGroup;
  paramSubscription:Subscription;
  size = "600";

  @ViewChild("labelRef") labelRef: LabelComponent;

  constructor(private labelService: LabelService, private route: ActivatedRoute, private router: Router, private snackBar: MatSnackBar, public dialog: MatDialog) {

    if(localStorage.getItem("size")){
      this.size = localStorage.getItem("size");
    }

  }

  ngOnDestroy(): void {
        this.paramSubscription.unsubscribe();
    }

  ngOnInit(): void {

    this.formGroup = new FormGroup({
      partName: new FormControl("", Validators.required),
      car: new FormControl("", Validators.required),
      warehouseNumber: new FormControl(""),
      warehouseNumberScanned: new FormControl("")
    });

    this.paramSubscription = this.route.params.subscribe(params => {
      if (params.id) {
        this.searchFormControl.setValue(params.id);
        this.searching = true;
        this.labelService.getLabel(params.id)
          .pipe(finalize(() => {
            this.searching = false;
          }))
          .subscribe(response => {
            this.notFound = false;
            this.label = response;
            this.formGroup.controls['partName'].setValue(response.partName);
            this.formGroup.controls['car'].setValue(response.carName);
            this.formGroup.controls['warehouseNumber'].setValue(response.warehouseNumber);
            this.formGroup.controls['warehouseNumberScanned'].setValue(response.warehouseNumberScanned);
            this.searchFormControl.setValue(response.id);
          }, error => {
            this.notFound = true;
          });
      }
    })

  }

  getLabel() {
    this.searchFormControl.markAsTouched();
    if (this.searchFormControl.valid) {
      if (this.route.snapshot.url[1].path == this.searchFormControl.value) {
        this.formGroup.controls['partName'].setValue(this.label.partName);
        this.formGroup.controls['car'].setValue(this.label.carName);
        this.formGroup.controls['warehouseNumber'].setValue(this.label.warehouseNumber);
        this.formGroup.controls['warehouseNumberScanned'].setValue(this.label.warehouseNumberScanned);
      } else {
        this.router.navigate(["app", "savedLabel", this.searchFormControl.value])
      }

    }
  }

  printLabel() {
    if (this.formGroup.valid) {
      this.labelRef.id = this.label.id;
      this.labelRef.print();
    }
  }

  updateLabel() {
    if (this.formGroup.valid) {
      this.updatingLabel = true;
      this.labelService.updateLabel(this.label.id, this.formGroup.controls['partName'].value, this.formGroup.controls['warehouseNumber'].value, this.formGroup.controls['car'].value,this.formGroup.controls['warehouseNumberScanned'].value)
        .pipe(finalize(() => {
          this.updatingLabel = false;
        }))
        .subscribe(response => {
          this.snackBar.open("Zapisano etykiete", "OK", {
            duration: 2000
          });
        }, error => {
          this.snackBar.open("Błąd podczas zapisywania etykiety", "OK", {
            duration: 2000
          });
        })
    }
  }

  deleteLabel() {
    if (this.searchFormControl.valid) {

      const dialogRef = this.dialog.open(SavedLabelDialog,);

      dialogRef.afterClosed().subscribe(result => {

        if (result) {
          this.deletingLabel = true;
          this.labelService.deleteLabel(this.searchFormControl.value)
            .pipe(finalize(() => {
              this.deletingLabel = false;
            }))
            .subscribe(response => {
              this.snackBar.open("Usunięto etykiete", "OK", {
                duration: 2000
              });
              this.label = null;
              this.notFound = false;
              this.searchFormControl.setValue(1);
              this.formGroup.reset();
              this.router.navigate(["app", "savedLabel", ""])
            }, error => {
              this.snackBar.open("Błąd podczas usuwania etykiety", "OK", {
                duration: 2000
              });
            });
        }

      });


    }
  }

}

@Component({
  selector: 'saved-label-dialog',
  templateUrl: 'saved-label-dialog.html',
})
export class SavedLabelDialog {
  constructor() {
  }
}
