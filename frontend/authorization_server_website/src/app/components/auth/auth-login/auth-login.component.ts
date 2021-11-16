import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../../../environments/environment';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-auth-login',
  templateUrl: './auth-login.component.html',
  styleUrls: ['./auth-login.component.css']
})
export class AuthLoginComponent implements OnInit {

  formGroup: FormGroup;
  badcredentials = false;

  loginActionUrl = `${environment.authorization_server_url}/login`;
  loginGoogleUrl = `${environment.authorization_server_url}/oauth2/authorization/google`;
  loginFacebookUrl = `${environment.authorization_server_url}/oauth2/authorization/facebook`;

  @ViewChild('form') form: HTMLElement;

  constructor(public auth: AuthService, private route: ActivatedRoute, private titleService: Title) {
  }


  ngOnInit(): void {
    this.titleService.setTitle('Zaloguj się');
    this.badcredentials = this.route.snapshot.queryParams['error'] == 'badcredentials';

    this.formGroup = new FormGroup({});
    this.formGroup.addControl('username', new FormControl('', Validators.required));
    this.formGroup.addControl('password', new FormControl('', Validators.required));

  }
}
