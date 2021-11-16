import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {ValidationPatterns} from '../../../enums/ValidationPatterns';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-auth-register',
  templateUrl: './auth-register.component.html',
  styleUrls: ['./auth-register.component.css']
})
export class AuthRegisterComponent implements OnInit {

  registerFormGroup: FormGroup;

  registration_completed = false;

  constructor(private http: HttpClient, private titleService: Title) {
  }

  ngOnInit(): void {

    this.titleService.setTitle('Zarejestruj się');
    this.registerFormGroup = new FormGroup({
      email: new FormControl('', Validators.required),
      password: new FormControl('', [Validators.required, Validators.pattern(ValidationPatterns.Password)]),
      password_confirmation: new FormControl('')
    });

    this.registerFormGroup.controls['password_confirmation'].setValidators([Validators.required, matchesPassword(this.registerFormGroup.controls['password'])]);
  }

  register() {
    this.registerFormGroup.markAllAsTouched();

    if (this.registerFormGroup.valid) {
      let email = this.registerFormGroup.controls['email'].value;
      let password = this.registerFormGroup.controls['password'].value;
      let url = `${environment.authorization_server_url}/user/register?email=${email}&password=${password}`;
      this.http.get(url, {responseType: 'text'}).subscribe(response => {
        this.registration_completed = true;
      }, (error: HttpErrorResponse) => {
        if (error.error == 'user_already_exists') {
          this.registerFormGroup.controls['email'].setErrors({already_exists: true});
        }
      });
    }
  }

}

export function matchesPassword(passwordControl: AbstractControl): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (passwordControl.value == control.value) {
      return null;
    } else {
      return {dontMatch: true};
    }
  };

}
