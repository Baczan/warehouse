import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {RoleTemplate} from '../../../../../../models/RoleTemplate';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RoleService} from '../../../../../../services/role.service';
import {HeaderButtonService} from '../../../../../../services/header-button.service';
import {environment} from '../../../../../../../environments/environment';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {AuthService} from '../../../../../../services/auth.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-role-add-item-template',
  templateUrl: './role-add-item-template.component.html',
  styleUrls: ['./role-add-item-template.component.css']
})
export class RoleAddItemTemplateComponent implements OnInit, OnDestroy {

  @Input() roleTemplate: RoleTemplate;
  @Input() index: number;
  formGroup: FormGroup;
  buttonSubscription: Subscription;

  constructor(private roleService: RoleService, private buttonService: HeaderButtonService, private http: HttpClient, private auth: AuthService) {
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup({role: new FormControl(this.roleTemplate.role, Validators.required)});

    this.buttonSubscription = this.buttonService.roleAddButtonEvent.subscribe(() => {

      this.formGroup.markAllAsTouched();

      if (this.formGroup.valid && this.formGroup.controls['role'].value != this.roleTemplate.role) {

        let formData = new FormData();
        formData.append('_csrf', this.auth.csrf_token);

        if (this.roleTemplate.id) {

          let url = `${environment.authorization_server_url}/roles/update?roleTemplateId=${this.roleTemplate.id}&role=${this.formGroup.controls['role'].value}`;

          this.http.post(url, formData).subscribe((response: RoleTemplate) => {
            this.roleTemplate = response;
          }, error => {
            if (error instanceof HttpErrorResponse && error.error == 'cannot_be_default_role') {
              this.formGroup.controls['role'].setErrors({cannot_be_default_role: true});
            } else {
              window.location.reload();
            }

          });


        } else {

          let url = `${environment.authorization_server_url}/roles/add?clientId=${this.roleTemplate.clientId}&role=${this.formGroup.controls['role'].value}`;

          this.http.post(url, formData).subscribe((response: RoleTemplate) => {
            this.roleTemplate = response;
          }, error => {
            if (error instanceof HttpErrorResponse && error.error == 'cannot_be_default_role') {
              this.formGroup.controls['role'].setErrors({cannot_be_default_role: true});
            } else {
              window.location.reload();
            }
          });

        }

      }

    });
  }

  delete() {

    let answer = confirm('Czy napewno chcesz usunąć tę rolę');

    if (answer) {
      if (this.roleTemplate.id) {

        let formData = new FormData();
        formData.append('_csrf', this.auth.csrf_token);

        let url = `${environment.authorization_server_url}/roles/delete?roleTemplateId=${this.roleTemplate.id}`;
        this.http.post(url, formData, {responseType: 'text'}).subscribe(() => {
        }, error => {
          window.location.reload();
        });
      }


      let manageResponseIndex = this.roleService.response.findIndex(value => value.clientId == this.roleTemplate.clientId);
      this.roleService.response[manageResponseIndex].rolesTemplates.splice(this.index, 1);
    }

  }

  ngOnDestroy(): void {
    this.buttonSubscription.unsubscribe();
  }

}
