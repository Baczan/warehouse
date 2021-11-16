import {Component, Input, OnInit} from '@angular/core';
import {MatDrawer} from '@angular/material/sidenav';

@Component({
  selector: 'app-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.css']
})
export class LinkComponent implements OnInit {


  @Input() text: string;
  @Input() icon: string;
  @Input() routerLink: string = '';
  @Input() expandable: boolean = false;
  @Input() drawer: MatDrawer;

  expanded = true;

  constructor() {
  }

  ngOnInit(): void {
  }

  click() {
    if (this.expandable) {
      this.expanded = !this.expanded;
    }
  }

  drawerClick() {
    if (this.drawer.mode == 'over') {
      this.drawer.close();
    }
  }

}
