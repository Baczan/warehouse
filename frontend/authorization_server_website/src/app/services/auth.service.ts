import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {User} from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  csrf_token;
  authenticated;
  admin;
  client_id;
  user: User;

  constructor(private cookieService: CookieService) {
  }

  loadCookies() {
    this.csrf_token = this.cookieService.get('csrf');
    this.authenticated = this.cookieService.get('authenticated') === 'true';
    this.admin = this.cookieService.get('admin') === 'true';

    let user_base64 = this.cookieService.get('user');
    if (user_base64) {
      let user_json = atob(user_base64);
      this.user = JSON.parse(user_json);
    }

  }

}
