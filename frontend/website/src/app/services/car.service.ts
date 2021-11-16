import { Injectable } from '@angular/core';
import {Car} from "../models/Car";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CarService {

  cars:Car[] = [];

  constructor(private http:HttpClient) { }

  getCars(){
    const url = `${environment.RESOURCE_SERVER_URL}/car/getAll`;
    this.http.get<Car[]>(url).subscribe(response=>{
      this.cars = response;
    })
  }

  addCar(carName){
    const url = `${environment.RESOURCE_SERVER_URL}/car/add?carName=${carName}`;
    return this.http.post(url,"");
  }

  deleteCar(id){
    const url = `${environment.RESOURCE_SERVER_URL}/car/delete?id=${id}`;
    return this.http.delete(url,{responseType:"text"});
  }
}
