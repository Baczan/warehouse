import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PartsService} from "../../../services/parts.service";
import {finalize} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-add-part',
  templateUrl: './add-part.component.html',
  styleUrls: ['./add-part.component.css']
})
export class AddPartComponent implements OnInit {

  formGroup:FormGroup;
  loading = false;

  constructor(private partService:PartsService,private snackBar:MatSnackBar) { }

  ngOnInit(): void {

    this.formGroup = new FormGroup({
      partName:new FormControl("",Validators.required),
      warehouseNumber:new FormControl("")
    })

  }

  addPart(){
    this.loading = true;
    this.partService.addPart(this.formGroup.controls['partName'].value,this.formGroup.controls['warehouseNumber'].value)
      .pipe(finalize(()=>{
        this.loading = false;
      }))
      .subscribe(response=>{
        this.formGroup.reset();
        this.snackBar.open("Dodano część","OK",{
          duration:3000
        })
      },error => {
        this.snackBar.open("Błąd podczas dodawania części","OK",{
          duration:3000
        })
      })
  }
}


