import {Component, OnInit} from '@angular/core';
import {RoleService} from '../../../../services/role.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-role-add',
  templateUrl: './role-add.component.html',
  styleUrls: ['./role-add.component.css']
})
export class RoleAddComponent implements OnInit {


  constructor(public roleService: RoleService, private titleService: Title) {

    roleService.getRoles();


  }

  ngOnInit(): void {
    this.titleService.setTitle('Dodaj role');
  }


}
