import {Component, Input, OnInit} from '@angular/core';
import {ManagerResponse} from '../../../../../models/ManagerResponse';
import {RoleTemplate} from '../../../../../models/RoleTemplate';

@Component({
  selector: 'app-role-add-item',
  templateUrl: './role-add-item.component.html',
  styleUrls: ['./role-add-item.component.css']
})
export class RoleAddItemComponent implements OnInit {

  @Input() managerResponse: ManagerResponse;

  @Input() visible: boolean;


  constructor() {

  }

  ngOnInit(): void {

  }

  addRole() {
    this.managerResponse.rolesTemplates.push(new RoleTemplate(null, this.managerResponse.clientId, false, false, '', null));
  }


}
