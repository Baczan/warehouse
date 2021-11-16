import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {ValidationPatterns} from '../../../enums/ValidationPatterns';
import {matchesPassword} from '../auth-register/auth-register.component';
import {environment} from '../../../../environments/environment';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-auth-password-change',
  templateUrl: './auth-password-change.component.html',
  styleUrls: ['./auth-password-change.component.css']
})
export class AuthPasswordChangeComponent implements OnInit {

  passwordChangeRequestFormGroup: FormGroup;
  passwordChangeRequestState = false;
  token = null;

  passwordChangeFormGroup: FormGroup;
  passwordChangeStatus = null;

  constructor(private http: HttpClient, private route: ActivatedRoute, private titleService: Title) {
  }

  ngOnInit(): void {

    this.titleService.setTitle('Zmień hasło');
    this.token = this.route.snapshot.queryParams['token'];

    this.passwordChangeRequestFormGroup = new FormGroup({
      email: new FormControl('', Validators.required)
    });

    this.passwordChangeFormGroup = new FormGroup({
      password: new FormControl('', [Validators.required, Validators.pattern(ValidationPatterns.Password)]),
      password_confirmation: new FormControl('')
    });

    this.passwordChangeFormGroup.controls['password_confirmation'].setValidators([Validators.required, matchesPassword(this.passwordChangeFormGroup.controls['password'])]);
  }

  passwordChangeRequest() {
    this.passwordChangeRequestFormGroup.markAllAsTouched();

    if (this.passwordChangeRequestFormGroup.valid) {
      let email = this.passwordChangeRequestFormGroup.controls['email'].value;
      let url = `${environment.authorization_server_url}/user/password_change_request?email=${email}`;
      this.http.get(url, {responseType: 'text'}).subscribe(response => {
        this.passwordChangeRequestState = true;
      }, (error: HttpErrorResponse) => {
        if (error.error == 'user_not_found') {
          this.passwordChangeRequestFormGroup.controls['email'].setErrors({user_not_found: true});
        }
      });
    }
  }

  passwordChange() {
    this.passwordChangeFormGroup.markAllAsTouched();
    if (this.passwordChangeFormGroup.valid) {
      let password = this.passwordChangeFormGroup.controls['password'].value;
      let url = `${environment.authorization_server_url}/user/password_change?token=${this.token}&password=${password}`;
      this.http.get(url, {responseType: 'text'}).subscribe(response => {
        this.passwordChangeStatus = true;
      }, error => {
        this.passwordChangeStatus = false;
      });
    }
  }

}
