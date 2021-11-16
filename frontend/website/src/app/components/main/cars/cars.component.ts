import {Component, HostListener, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {CarService} from "../../../services/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'app-cars',
  templateUrl: './cars.component.html',
  styleUrls: ['./cars.component.css']
})
export class CarsComponent implements OnInit {

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if(event.key=="Enter"){
      this.addCar()
    }
  }

  carNameControl = new FormControl("",Validators.required);

  constructor(public carService:CarService,private snackBar:MatSnackBar) { }

  ngOnInit(): void {
    this.carService.getCars();
  }

  addCar(){
    this.carNameControl.markAsTouched();
    if(this.carNameControl.valid){

      this.carService.addCar(this.carNameControl.value)
        .pipe(finalize(()=>{
          this.carService.getCars();
        }))
        .subscribe(response=>{
        this.snackBar.open("Dodano samochód", "OK", {
          duration: 2000
        });
        this.carNameControl.reset();
      },error => {
        this.snackBar.open("Błąd podczas dodawania samochodu", "OK", {
          duration: 2000
        });
      })

    }
  }

}
