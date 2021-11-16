import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {AuthService} from '../../../../services/auth.service';
import {finalize} from 'rxjs/operators';
import {Client} from '../../../../models/Client';
import {ClientService} from '../../../../services/client.service';
import {Router} from '@angular/router';
import {environment} from '../../../../../environments/environment';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-new-app',
  templateUrl: './new-app.component.html',
  styleUrls: ['./new-app.component.css']
})
export class NewAppComponent implements OnInit {

  formGroup: FormGroup;

  loading = false;


  constructor(private http: HttpClient, private auth: AuthService, private clientService: ClientService, private router: Router, private titleService: Title) {
  }

  ngOnInit(): void {


    this.titleService.setTitle('Nowa aplikacja');

    this.formGroup = new FormGroup({
      name: new FormControl('', Validators.required)
    });

  }

  submit() {

    this.formGroup.markAllAsTouched();

    if (this.formGroup.valid) {

      this.loading = true;

      let url = `${environment.authorization_server_url}/createClient?name=${this.formGroup.controls['name'].value}`;
      let formData = new FormData();
      formData.append('_csrf', this.auth.csrf_token);

      this.http.post(url, formData).pipe(finalize(() => {
        this.loading = false;
      })).subscribe((client: Client) => {

        this.clientService.clients.push(client);
        this.router.navigate(['main', 'apps', client.clientId]);

      }, (error: HttpErrorResponse) => {
        if (error.error == 'already_exists') {
          this.formGroup.controls['name'].setErrors({already_exists: true});
        }
      });
    }
  }

}
