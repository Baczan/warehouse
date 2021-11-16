import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PartsService} from "../../../../services/parts.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";
import {PartModel} from "../../../../models/PartModel";
import {finalize} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-update-part',
  templateUrl: './update-part.component.html',
  styleUrls: ['./update-part.component.css']
})
export class UpdatePartComponent implements OnInit {

  formGroup: FormGroup;
  loading = true;
  saving = false;
  part: PartModel;

  constructor(private partService: PartsService, private snackBar: MatSnackBar, private route: ActivatedRoute, private router: Router, public dialog: MatDialog) {
  }

  ngOnInit(): void {

    let id = this.route.snapshot.params.id;

    this.partService.getPart(id).pipe(finalize(() => {
      this.loading = false;
    })).subscribe(response => {
      this.part = response;
      this.formGroup = new FormGroup({
        partName: new FormControl(this.part.partName, Validators.required),
        warehouseNumber: new FormControl(this.part.warehouseNumber)
      })
    }, error => {
      this.router.navigate(["app", "parts"])
    });
  }

  goBack() {
    window.history.back();
  }

  updatePart() {
    this.saving = true;
    this.partService.updatePart(this.part.id, this.formGroup.controls['partName'].value, this.formGroup.controls['warehouseNumber'].value)
      .pipe(finalize(() => {
        this.saving = false;
      })).subscribe(response => {
      this.snackBar.open("Zaktualizowano część", "OK", {
        duration: 2000
      })
    }, (error: HttpErrorResponse) => {

      if (error.error == "bad_parameters") {
        this.snackBar.open("Błąd w danych", "OK", {
          duration: 3000
        })
      } else {
        this.router.navigate(["app", "parts"])
      }

    })
  }

  deletePart() {

    const dialogRef = this.dialog.open(PartDialog,{
      data: { partName: this.part.partName },
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.partService.deletePart(this.part.id).subscribe(
          response => {
            this.snackBar.open("Część została usunięta", "OK", {
              duration: 2000
            });
            window.history.back();
          }, error => {
            this.snackBar.open("Nie można było usunąć części", "OK", {
              duration: 2000
            });
            window.history.back();
          }
        )
      }

    });



  }

}

@Component({
  selector: 'part-dialog',
  templateUrl: 'part-dialog.html',
})
export class PartDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: {partName: string}) { }
}
