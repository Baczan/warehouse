import {Component, Input, OnInit} from '@angular/core';
import {NewCar} from "../../../../modules/auth/Models/NewCar";

@Component({
  selector: 'app-new-car-list-item',
  templateUrl: './new-car-list-item.component.html',
  styleUrls: ['./new-car-list-item.component.css']
})
export class NewCarListItemComponent implements OnInit {

  @Input() car:NewCar;

  constructor() { }

  ngOnInit(): void {
  }

}
