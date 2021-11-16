import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Client} from '../../../../models/Client';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {AuthService} from '../../../../services/auth.service';
import {ClientService} from '../../../../services/client.service';
import {environment} from '../../../../../environments/environment';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Title} from '@angular/platform-browser';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-apps',
  templateUrl: './apps.component.html',
  styleUrls: ['./apps.component.css']
})
export class AppsComponent implements OnInit, OnDestroy {

  formGroup: FormGroup;
  formArray: FormArray;

  loadingClient = true;
  saving = false;
  paramSubscription:Subscription;
  formSubscription:Subscription;

  @ViewChild('form') form: HTMLFormElement;

  client: Client;

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private router: Router,
              private auth: AuthService,
              private clientService: ClientService,
              private _snackBar: MatSnackBar,
              private titleService: Title) {

  }


  ngOnInit(): void {
    this.paramSubscription = this.route.paramMap.subscribe(paramMap => {
      let id = paramMap.get('id');
      this.http.get(`${environment.authorization_server_url}/client?id=${id}`).pipe(finalize(() => {
        this.loadingClient = false;
      })).subscribe((client: Client) => {

        this.client = client;

        this.titleService.setTitle(`Aplikacja: ${client.name}`);

        this.formArray = new FormArray([]);

        this.client.redirectUrls.forEach(redirect_url => {
          this.formArray.push(new FormControl(redirect_url, Validators.required));
        });


        this.formGroup = new FormGroup({
          name: new FormControl(this.client.name, Validators.required),
          requireProofKey: new FormControl(this.client.requireProofKey),
          requireUserConsent: new FormControl(this.client.requireUserConsent),
          reuseRefreshTokens: new FormControl(this.client.reuseRefreshToken),
          accessTokenTimeToLive: new FormControl(this.client.accessTokenTimeToLive, Validators.required),
          refreshTokenTimeToLive: new FormControl(this.client.refreshTokenTimeToLive, Validators.required),
          authentication_method_basic: new FormControl(this.client.authenticationMethodBasic),
          authentication_method_post: new FormControl(this.client.authenticationMethodPost),
          authentication_method_none: new FormControl(this.client.authenticationMethodNone),
          authorization_grant_type_authorization_code: new FormControl(this.client.authorizationGrantTypeAuthorizationCode),
          authorization_grant_type_implicit: new FormControl(this.client.authorizationGrantTypeImplicit),
          authorization_grant_type_refresh_token: new FormControl(this.client.authorizationGrantTypeRefreshToken),
          authorization_grant_type_password: new FormControl(this.client.authorizationGrantTypePassword),
          authorization_grant_type_client_credentials: new FormControl(this.client.authorizationGrantTypeClientCredentials),
          redirect_urls: this.formArray
        });


        this.formSubscription = this.formGroup.valueChanges.subscribe(() => {

          if (!this.formGroup.controls['authentication_method_basic'].value
            && !this.formGroup.controls['authentication_method_post'].value
            && !this.formGroup.controls['authentication_method_none'].value) {
            this.formGroup.controls['authentication_method_basic'].setErrors({not_selected: true});
          } else {
            this.formGroup.controls['authentication_method_basic'].setErrors(null);
          }

          if (!this.formGroup.controls['authorization_grant_type_authorization_code'].value
            && !this.formGroup.controls['authorization_grant_type_implicit'].value
            && !this.formGroup.controls['authorization_grant_type_password'].value
            && !this.formGroup.controls['authorization_grant_type_client_credentials'].value) {
            this.formGroup.controls['authorization_grant_type_authorization_code'].setErrors({not_selected: true});
          } else {
            this.formGroup.controls['authorization_grant_type_authorization_code'].setErrors(null);
          }

        });
      }, error => {
        this.router.navigate(['main', 'user']);
      });
    });

  }

  deleteRedirect(index) {
    this.formArray.removeAt(index);
  }

  add() {
    this.formArray.push(new FormControl('', Validators.required));
  }

  submit() {
    this.formGroup.markAllAsTouched();
    if (this.formGroup.valid) {

      this.saving = true;
      let formData = new FormData();

      formData.append('_csrf', this.auth.csrf_token);

      formData.append('name', this.formGroup.controls['name'].value);
      formData.append('clientId', this.client.clientId);
      formData.append('authenticationMethodBasic', this.formGroup.controls['authentication_method_basic'].value);
      formData.append('authenticationMethodPost', this.formGroup.controls['authentication_method_post'].value);
      formData.append('authenticationMethodNone', this.formGroup.controls['authentication_method_none'].value);
      formData.append('authorizationGrantTypeAuthorizationCode', this.formGroup.controls['authorization_grant_type_authorization_code'].value);
      formData.append('authorizationGrantTypeImplicit', this.formGroup.controls['authorization_grant_type_implicit'].value);
      formData.append('authorizationGrantTypeRefreshToken', this.formGroup.controls['authorization_grant_type_refresh_token'].value);
      formData.append('authorizationGrantTypePassword', this.formGroup.controls['authorization_grant_type_password'].value);
      formData.append('authorizationGrantTypeClientCredentials', this.formGroup.controls['authorization_grant_type_client_credentials'].value);
      formData.append('requireUserConsent', this.formGroup.controls['requireUserConsent'].value);
      formData.append('requireProofKey', this.formGroup.controls['requireProofKey'].value);
      formData.append('reuseRefreshToken', this.formGroup.controls['reuseRefreshTokens'].value);
      formData.append('accessTokenTimeToLive', this.formGroup.controls['accessTokenTimeToLive'].value);
      formData.append('refreshTokenTimeToLive', this.formGroup.controls['refreshTokenTimeToLive'].value);

      let urls: string[] = this.formGroup.controls['redirect_urls'].value;

      urls.forEach(url => {
        formData.append('redirectUrls', url);
      });

      this.http.post(`${environment.authorization_server_url}/updateClient`, formData, {responseType: 'text'}).pipe(finalize(() => {
        this.saving = false;
      })).subscribe(response => {

        let index = this.clientService.clients.findIndex(client1 => client1.clientId == this.client.clientId);
        this.clientService.clients[index].name = this.formGroup.controls['name'].value;

        this._snackBar.open('Zapisano', 'OK', {duration: 2000});
      }, (error: HttpErrorResponse) => {
        if (error.error == 'already_exists') {
          this.formGroup.controls['name'].setErrors({already_exists: true});
        } else if (error.error == 'access_token_wrong_duration_format') {
          this.formGroup.controls['accessTokenTimeToLive'].setErrors({wrong_format: true});
        } else if (error.error == 'refresh_token_wrong_duration_format') {
          this.formGroup.controls['refreshTokenTimeToLive'].setErrors({wrong_format: true});
        } else {
          this._snackBar.open('Wystąpił błąd', 'OK', {duration: 2000});
        }

      });
    }
  }

  deleteClient() {
    let answer = confirm('Czy napewno chcesz usunąć tą aplikacje?');
    if (answer) {
      let formData = new FormData();
      formData.append('_csrf', this.auth.csrf_token);
      let url = `${environment.authorization_server_url}/clientDelete?clientId=${this.client.clientId}`;

      this.http.post(url, formData, {responseType: 'text'}).subscribe(response => {
        let index = this.clientService.clients.findIndex(client1 => client1.clientId == this.client.clientId);
        if (index > -1) {
          this.clientService.clients.splice(index, 1);
        }
        this.router.navigate(['main', 'user']);
      }, error => {
        window.location.reload();
      });
    }
  }

  refreshSecret() {
    let formData = new FormData();
    formData.append('_csrf', this.auth.csrf_token);
    let url = `${environment.authorization_server_url}/clientSecretRefresh?clientId=${this.client.clientId}`;

    this.http.post(url, formData, {responseType: 'text'}).subscribe(response => {
      this.client.clientSecret = response;
    }, error => {
      window.location.reload();
    });
  }

  ngOnDestroy(): void {

    this.paramSubscription.unsubscribe();
    this.formSubscription.unsubscribe();

  }


}
