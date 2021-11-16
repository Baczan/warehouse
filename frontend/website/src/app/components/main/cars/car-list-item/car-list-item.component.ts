import {Component, Inject, Input, OnInit} from '@angular/core';
import {Car} from "../../../../models/Car";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {PartDialog} from "../../parts/update-part/update-part.component";
import {CarService} from "../../../../services/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'app-car-list-item',
  templateUrl: './car-list-item.component.html',
  styleUrls: ['./car-list-item.component.css']
})
export class CarListItemComponent implements OnInit {

  @Input() car: Car;


  constructor(private dialog: MatDialog, private carService: CarService, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  delete() {
    const dialogRef = this.dialog.open(PartDialog, {
      data: {partName: this.car.carName},
    });

    dialogRef.afterClosed()
      .subscribe(result => {
        if(result){
          this.carService.deleteCar(this.car.id)
            .pipe(finalize(() => {
              this.carService.getCars();
            }))
            .subscribe(response => {
              this.snackBar.open("Usunięto samochód", "OK", {
                duration: 2000
              });
            }, error => {
              this.snackBar.open("Błąd podczas usuwania samochodu", "OK", {
                duration: 2000
              });
            })
        }

      })
  }

}

@Component({
  selector: 'car-dialog',
  templateUrl: 'car-dialog.html',
})
export class CarDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { carName: string }) {
  }
}
