import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ConnectService} from "./services/connect.service";
import {finalize} from "rxjs/operators";
import {AuthService} from "./modules/auth/services/auth.service";
import {InitzializingService} from "./services/initzializing.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  implements OnInit{
  title = 'Magazyn Kruszwica';


  constructor(public initializingService:InitzializingService,public auth:AuthService){
  }

  ngOnInit(): void {

    this.initializingService.initialize();

  }


}
