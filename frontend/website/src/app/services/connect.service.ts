import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Connection} from "../models/Connection";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ConnectService {

  public connections:Connection[]=[];

  constructor(private http:HttpClient) { }

  loadConnections(){
    const url = `${environment.RESOURCE_SERVER_URL}/connect/list`;
    return this.http.get<Connection[]>(url).pipe(tap(connections=>{
      this.connections = connections;
    }))
  }

  connectAllegro(){
    const url = `${environment.ALLEGRO_AUTHORIZATION_URL}/authorize?response_type=code` +
      `&client_id=${environment.ALLEGRO_CLIENT_ID}`+
      `&redirect_uri=${environment.ALLEGRO_REDIRECT_URL}`+
    `&scope=allegro:api:sale:offers:read allegro:api:sale:offers:write`;
    window.location.href = url;
  }

  refreshAllegro(){
    const url = `${environment.RESOURCE_SERVER_URL}/connect/allegro/refresh`;

    return this.http.post(url,null);
  }

  disconnectAllegro(){
    const url = `${environment.RESOURCE_SERVER_URL}/connect/allegro/disconnect`;
    return this.http.post(url,null,{responseType:"text"});
  }

}
