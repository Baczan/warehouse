import {Component, Input, OnInit} from '@angular/core';
import {MatDrawer} from "@angular/material/sidenav";

@Component({
  selector: 'app-href',
  templateUrl: './href.component.html',
  styleUrls: ['./href.component.css']
})
export class HrefComponent implements OnInit {

  @Input() link=null;
  @Input() text;
  @Input() icon;
  @Input() matDrawer:MatDrawer;

  constructor() { }

  ngOnInit(): void {
  }

  closeAfterNavigation(){
    if(this.matDrawer.mode=="over" && this.matDrawer.opened){
      this.matDrawer.close();
    }
  }

}
