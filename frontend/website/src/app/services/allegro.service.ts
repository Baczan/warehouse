import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {AllegroAuction} from "../models/AllegroAuction";

@Injectable({
  providedIn: 'root'
})
export class AllegroService {

  constructor(private http:HttpClient) { }

  loadOffers(){
    const url = `${environment.RESOURCE_SERVER_URL}/allegro/offers`;

    this.http.get(url).subscribe(response=>{
      console.log(response)
    },error => {
      console.log(error)
    })
  }

  getOffer(offerId){
    const url = `${environment.RESOURCE_SERVER_URL}/allegro/offer?offerId=${offerId}`;
    return this.http.get<AllegroAuction>(url);
  }
}
