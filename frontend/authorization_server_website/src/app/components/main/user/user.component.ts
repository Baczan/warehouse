import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup} from '@angular/forms';
import {UserDetailsResponse} from '../../../models/UserDetailsResponse';
import {finalize} from 'rxjs/operators';
import {environment} from '../../../../environments/environment';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  formGroup: FormGroup;
  loading = true;

  constructor(public auth: AuthService, private http: HttpClient, private _snackBar: MatSnackBar, private titleService: Title) {
  }


  ngOnInit(): void {


    this.titleService.setTitle(this.auth.user.email);

    this.formGroup = new FormGroup({
      firstName: new FormControl(''),
      lastName: new FormControl(''),
      email: new FormControl(this.auth.user.email),
      phone: new FormControl('')
    });

    this.http.get(`${environment.authorization_server_url}/details`).pipe(finalize(() => {
      this.loading = false;
    })).subscribe((response: UserDetailsResponse) => {
      this.formGroup.controls['firstName'].setValue(response.firstName);
      this.formGroup.controls['lastName'].setValue(response.lastName);
      this.formGroup.controls['phone'].setValue(response.phone);
    });
  }

  submit() {
    let formData = new FormData();
    formData.append('_csrf', this.auth.csrf_token);
    formData.append('firstName', this.formGroup.controls['firstName'].value);
    formData.append('lastName', this.formGroup.controls['lastName'].value);
    formData.append('phone', this.formGroup.controls['phone'].value);

    this.http.post(`${environment.authorization_server_url}/details`, formData, {responseType: 'text'}).subscribe(response => {
      this._snackBar.open('Zapisano', 'OK', {duration: 2000});
    }, error => {
      this._snackBar.open('Wystąpił błąd', 'OK', {duration: 2000});
    });
  }

}
