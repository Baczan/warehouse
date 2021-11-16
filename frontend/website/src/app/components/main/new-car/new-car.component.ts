import {Component, HostListener, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {CarService} from "../../../services/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {finalize} from "rxjs/operators";
import {NewCarService} from "../../../services/new-car.service";

@Component({
  selector: 'app-new-car',
  templateUrl: './new-car.component.html',
  styleUrls: ['./new-car.component.css']
})
export class NewCarComponent implements OnInit {

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if(event.key=="Enter"){
      this.addCar()
    }
  }

  carNameControl = new FormControl("",Validators.required);

  constructor(public carService:NewCarService,private snackBar:MatSnackBar) { }

  ngOnInit(): void {
    this.carService.getAll();
  }

  addCar(){
    this.carNameControl.markAsTouched();
    if(this.carNameControl.valid){

      this.carService.addCar(this.carNameControl.value)
        .pipe(finalize(()=>{
         this.carService.getAll();
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
