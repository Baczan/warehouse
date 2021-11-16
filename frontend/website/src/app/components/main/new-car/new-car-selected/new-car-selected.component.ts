import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NewCarService} from "../../../../services/new-car.service";
import {NewCar} from "../../../../modules/auth/Models/NewCar";
import {FileUploading} from "../../../../models/FileUploading";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {ReturnDialog} from "../../returns/return-add/return-add.component";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'app-new-car-selected',
  templateUrl: './new-car-selected.component.html',
  styleUrls: ['./new-car-selected.component.css']
})
export class NewCarSelectedComponent implements OnInit {


  updating = false;
  id = this.route.snapshot.params.id;
  loading = true;
  savedNewCar:NewCar = null;


  formGroup: FormGroup;

  constructor(private route: ActivatedRoute, private router: Router, private carService: NewCarService, private snackBar: MatSnackBar, public dialog: MatDialog) {
  }


  ngOnInit(): void {

    this.carService.get(this.id).subscribe(response => {
      this.setFromGroup(response);
      this.carService.getAllImages(this.id, true);
      this.savedNewCar = response;
    }, error => {
      this.router.navigate(["app", "newCar"]).then(() => {
        this.snackBar.open("Nie znaleziono samochodu", "OK", {
          duration: 2000
        });
      })
    });

  }

  navigateBack() {

    if(!this.savedNewCar || !compareNewCar(this.savedNewCar,this.getCarModel())){
      const dialogRef = this.dialog.open(NewCarLeaveDialog);

      dialogRef.afterClosed().subscribe(result => {
        if(result){
          window.history.back();
        }
      });
    }else{
      window.history.back();
    }


  }


  setFromGroup(car: NewCar) {
    this.formGroup = new FormGroup({
      carName: new FormControl(car.carName, Validators.required),
      brand: new FormControl(car.brand),
      year: new FormControl(car.year),
      productionYears: new FormControl(car.productionYears),
      engine: new FormControl(car.engine),
      engineCode: new FormControl(car.engineCode),
      transmission: new FormControl(car.transmission),
      transmissionCode: new FormControl(car.transmissionCode),
      body: new FormControl(car.body),
      ac: new FormControl(car.ac),
      paintCode: new FormControl(car.paintCode),
      electricWindows: new FormControl(car.electricWindows),
      electricMirrors: new FormControl(car.electricMirrors),
      mileage: new FormControl(car.mileage),
      abs: new FormControl(car.abs),
      vin: new FormControl(car.vin),
      annotation: new FormControl(car.annotation)
    });


    this.loading = false;
  }

  saveCar() {

    if (this.formGroup.valid) {

      this.updating = true;

      this.carService.updateCar(this.id, this.getCarModel())
        .pipe(finalize(()=>{
          this.updating = false;
        }))
        .subscribe(response => {
          this.savedNewCar = response;
        this.snackBar.open("Zaktualizowano samochód", "OK", {
          duration: 2000
        });
      }, error => {
        this.router.navigate(["app", "newCar"]).then(() => {
          this.snackBar.open("Błąd podczas aktualizacji samochodu", "OK", {
            duration: 2000
          });
        });
      })

    }

  }

  getCarModel(): NewCar {

    let car = new NewCar();
    car.carName = this.formGroup.controls['carName'].value;
    car.brand = this.formGroup.controls['brand'].value;
    car.year = this.formGroup.controls['year'].value;
    car.productionYears = this.formGroup.controls['productionYears'].value;
    car.engine = this.formGroup.controls['engine'].value;
    car.engineCode = this.formGroup.controls['engineCode'].value;
    car.transmission = this.formGroup.controls['transmission'].value;
    car.transmissionCode = this.formGroup.controls['transmissionCode'].value;
    car.body = this.formGroup.controls['body'].value;
    car.ac = this.formGroup.controls['ac'].value;
    car.paintCode = this.formGroup.controls['paintCode'].value;
    car.electricWindows = this.formGroup.controls['electricWindows'].value;
    car.electricMirrors = this.formGroup.controls['electricMirrors'].value;
    car.mileage = this.formGroup.controls['mileage'].value;
    car.abs = this.formGroup.controls['abs'].value;
    car.vin = this.formGroup.controls['vin'].value;
    car.annotation = this.formGroup.controls['annotation'].value;

    return car;
  }

  fileInputClick(event: Event) {

    // @ts-ignore
    let fileList: FileList = event.target.files;

    for (let i = 0; i < fileList.length; i++) {
      this.carService.uploadImage(fileList.item(i), this.id);
    }
  }

  getFileUpload(): FileUploading[] {

    return this.carService.currentFileUploads.filter(fileUpload => {
      return fileUpload.carId == this.id;
    })

  }

  deleteCar() {

    const dialogRef = this.dialog.open(NewCarDelete);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carService.deleteCar(this.id).subscribe(response => {
          this.router.navigate(["app", "newCar"]).then(() => {
            this.snackBar.open("Usunięto samochód", "OK", {
              duration: 2000
            });
          });
        }, error => {
          this.snackBar.open("Błąd podczas usuwania samochodu", "OK", {
            duration: 2000
          });
        })
      }
    });

  }


}

export function compareNewCar(car1:NewCar,car2:NewCar) {

  if(car1.carName!=car2.carName){
    return false;
  }

  if(car1.brand!=car2.brand){
    return false;
  }

  if(car1.year!=car2.year){
    return false;
  }

  if(car1.productionYears!=car2.productionYears){
    return false;
  }

  if(car1.engine!=car2.engine){
    return false;
  }

  if(car1.engineCode!=car2.engineCode){
    return false;
  }

  if(car1.transmission!=car2.transmission){
    return false;
  }

  if(car1.transmissionCode!=car2.transmissionCode){
    return false;
  }

  if(car1.body!=car2.body){
    return false;
  }

  if(car1.ac!=car2.ac){
    return false;
  }

  if(car1.paintCode!=car2.paintCode){
    return false;
  }

  if(car1.electricWindows!=car2.electricWindows){
    return false;
  }

  if(car1.electricMirrors!=car2.electricMirrors){
    return false;
  }

  if(car1.mileage!=car2.mileage){
    return false;
  }

  if(car1.abs!=car2.abs){
    return false;
  }

  if(car1.vin!=car2.vin){
    return false;
  }

  if(car1.annotation!=car2.annotation){
    return false;
  }


  return true;
}

@Component({
  selector: 'new-car-delete',
  templateUrl: 'new-car-delete.html',
})
export class NewCarDelete {
  constructor() {
  }
}

@Component({
  selector: 'new-car-leave-dialog',
  templateUrl: 'new-car-leave-dialog.html',
})
export class NewCarLeaveDialog {
  constructor() {
  }
}




