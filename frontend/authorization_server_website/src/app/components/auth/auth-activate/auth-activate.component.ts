import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-auth-activate',
  templateUrl: './auth-activate.component.html',
  styleUrls: ['./auth-activate.component.css']
})
export class AuthActivateComponent implements OnInit {

  activationState = null;

  constructor(private route: ActivatedRoute, private http: HttpClient, private titleService: Title) {
  }

  ngOnInit(): void {

    this.titleService.setTitle('Aktywuj konto');

    let id = this.route.snapshot.queryParams['id'];

    if (id) {
      let url = `${environment.authorization_server_url}/user/activate?id=${id}`;
      this.http.get(url, {responseType: 'text'}).subscribe(response => {
        this.activationState = true;
      }, error => {
        this.activationState = false;
      });
    } else {
      this.activationState = false;
    }
  }

}
