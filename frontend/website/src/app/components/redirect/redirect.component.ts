import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ConnectService} from "../../services/connect.service";

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private http: HttpClient) {
  }

  ngOnInit(): void {


    const code = this.route.snapshot.queryParams.code;

    if (!code) {
      this.navigateToConnect();
    }

    if (this.route.snapshot.params.service == "allegro") {
      this.connectAllegro(code);
    }


  }

  connectAllegro(code: string) {
    console.log("connecting");
    const url = `${environment.RESOURCE_SERVER_URL}/connect/allegro?code=${code}`;

    this.http.post(url, "").subscribe(response => {
        this.navigateToConnect();
      }
      , (error: HttpErrorResponse) => {
        this.navigateToConnect()
      })
  }

  navigateToConnect(message: string = null) {
    let queryParams = {};
    if (message) {
      queryParams = {message: message}
    }

    this.router.navigate(["app", "connect"], {queryParams: queryParams})
  }

}
