import { Injectable } from '@angular/core';
import {finalize} from "rxjs/operators";
import {ConnectService} from "./connect.service";
import {AuthService} from "../modules/auth/services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class InitzializingService {

  loadingConnections = false;

  constructor(private connectionService:ConnectService,public auth:AuthService) { }

  initialize(){

    this.auth.loadUser();

    if(this.auth.loggedIn()){
      this.loadingConnections=true;
      this.connectionService.loadConnections().pipe(finalize(()=>{
        this.loadingConnections=false;
      })).subscribe();
    }
  }

  appInitializing():boolean{
    return this.loadingConnections;
  }
}
