import { Component, OnInit } from '@angular/core';
import {ConnectService} from "../../../services/connect.service";
import {AllegroService} from "../../../services/allegro.service";
import {of} from "rxjs";
import {finalize} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-connect',
  templateUrl: './connect.component.html',
  styleUrls: ['./connect.component.css']
})
export class ConnectComponent implements OnInit {

  loading = true;

  constructor(public connectService:ConnectService,private allegro:AllegroService,private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.connectService.loadConnections().pipe(finalize(()=>{
      this.loading = false;
    })).subscribe(
      response=>{
      },
      error => {
      }
    );
  }

  connectAllegro(){
    this.connectService.connectAllegro()
  }

  disconnectAllegro(){
    let index = this.connectService.connections.findIndex(connection=>connection.service=="allegro");
    this.connectService.connections.splice(index,1);

    this.connectService.disconnectAllegro().pipe(finalize(()=>{
      this.connectService.loadConnections().subscribe();
    })).subscribe(response=>{

    },error => {
      this.snackBar.open("Błąd podczas rozłącznia aplikacji","OK",{
        duration:3000
      })
    });
  }

  offers(){
    this.allegro.loadOffers();
  }

  offer(offerId){
    this.allegro.getOffer(offerId).subscribe(response=>{
      console.log(response)
    },
      error => {
      console.log(error.error)
      })
  }

  allegroConnected():boolean{
    return !!this.connectService.connections.find(connection => connection.service == "allegro");
  }

  allegroUsername():string{
    return this.connectService.connections.find(connection => connection.service == "allegro").username;
  }


}
