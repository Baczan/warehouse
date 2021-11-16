import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Part} from "../../../../models/Part";
import {Router} from "@angular/router";

@Component({
  selector: 'app-part-item',
  templateUrl: './part-item.component.html',
  styleUrls: ['./part-item.component.css']
})
export class PartItemComponent implements OnInit{

  @Input() part:Part;
  @Input() index:number;

  constructor(private router:Router) { }

  ngOnInit(): void {

  }

  getBorder() {
    if (this.index == 49) {
      return "none"
    }
    return '1px solid rgba(0,0,0,0.3)'
  }

  navigate(){
    this.router.navigate(["app","parts",this.part.id])
  }


}
