import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../../environments/environment';
import {ManagerResponse} from '../../../../../models/ManagerResponse';
import {RoleTemplate} from '../../../../../models/RoleTemplate';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {AuthService} from '../../../../../services/auth.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-role-assign-user',
  templateUrl: './role-assign-user.component.html',
  styleUrls: ['./role-assign-user.component.css']
})
export class RoleAssignUserComponent implements OnInit {

  managerResponseList: ManagerResponse[] = [];
  email;
  id;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router, public auth: AuthService, private titleService: Title) {
  }

  ngOnInit(): void {




    this.id = this.route.snapshot.paramMap.get('id');
    this.email = this.route.snapshot.paramMap.get('email');

    this.titleService.setTitle(`Przypisz role: ${this.email}`);

    let url = `${environment.authorization_server_url}/roles/get?withDefault=true&secondUserId=${this.id}`;
    this.http.get(url).subscribe((response: ManagerResponse[]) => {
      this.managerResponseList = response;
    }, error => {
      this.router.navigate(['main', 'roles', 'assign']);
    });


  }

  changed(event: MatCheckboxChange, roleTemplate: RoleTemplate) {

    let formData = new FormData();
    formData.append('_csrf', this.auth.csrf_token);

    if (event.checked) {

      let url = `${environment.authorization_server_url}/roles/assign?userIdToAssign=${this.id}&roleTemplateId=${roleTemplate.id}`;
      this.http.post(url, formData, {responseType: 'text'}).subscribe(response => {
      }, error => {
        window.location.reload();
      });

    } else {
      let url = `${environment.authorization_server_url}/roles/revoke?userIdToAssign=${this.id}&roleTemplateId=${roleTemplate.id}`;
      this.http.post(url, formData, {responseType: 'text'}).subscribe(response => {
      }, error => {
        window.location.reload();
      });
    }

  }

  navigateBack() {
    window.history.back();
  }

}
