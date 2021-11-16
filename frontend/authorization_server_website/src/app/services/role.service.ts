import {Injectable} from '@angular/core';
import {ManagerResponse} from '../models/ManagerResponse';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  response: ManagerResponse[];

  constructor(private http: HttpClient) {

  }

  getRoles() {
    this.http.get(`${environment.authorization_server_url}/roles/get?withDefault=false`).subscribe((response: ManagerResponse[]) => {
      this.response = response;
    });
  }
}
