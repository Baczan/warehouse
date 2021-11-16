import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {LabelService} from "../../../services/label.service";
import {finalize} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-sync',
  templateUrl: './sync.component.html',
  styleUrls: ['./sync.component.css']
})
export class SyncComponent implements OnInit,AfterViewInit {

  commandControl:FormControl;
  loading:boolean = false;
  imported:boolean = false;

  @ViewChild("textAreaElement") textArea:ElementRef;

  constructor(private labelService:LabelService,private snackBar:MatSnackBar,private cdr:ChangeDetectorRef) { }

  ngOnInit(): void {

    this.commandControl = new FormControl("",Validators.required)

  }

  sync(){
    this.commandControl.markAllAsTouched();
    if(this.commandControl.valid){
      this.loading = true;
      this.labelService.syncLabel(this.commandControl.value).pipe(finalize(()=>{
        this.loading = false;
      })).subscribe(response=>{
        this.snackBar.open("Zaimportowano miejsca magazynowe", "OK", {
          duration: 2000
        });
        this.imported = true;
        this.commandControl.reset();
      },error => {
        this.snackBar.open("Błąd podczas importowania miejsc magazynowych", "OK", {
          duration: 2000
        });
      })

    }

  }

  deleted(){
    this.imported = false;
  }

  ngAfterViewInit(): void {
    this.textArea.nativeElement.focus();
    this.cdr.detectChanges();
    console.log()
  }

}
