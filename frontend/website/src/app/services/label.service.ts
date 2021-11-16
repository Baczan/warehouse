import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Label} from "../models/Label";

@Injectable({
  providedIn: 'root'
})
export class LabelService {

  constructor(private http:HttpClient) { }

  saveLabel(partName,carName,warehouseNumber){
    const url = `${environment.RESOURCE_SERVER_URL}/label/add?partName=${partName}&warehouseNumber=${warehouseNumber}&carName=${carName}`;
    return this.http.post<Label>(url,"");
  }

  getLabel(id){
    const url = `${environment.RESOURCE_SERVER_URL}/label/get?id=${id}`;
    return this.http.get<Label>(url);
  }

  updateLabel(id,partName,warehouseNumber,carName,warehouseNumberScanned){
    const url = `${environment.RESOURCE_SERVER_URL}/label/update?id=${id}&partName=${partName}&carName=${carName}&warehouseNumber=${warehouseNumber}&warehouseNumberScanned=${warehouseNumberScanned}`;
    return this.http.post<Label>(url,"");
  }

  deleteLabel(id){
    const url = `${environment.RESOURCE_SERVER_URL}/label/delete?id=${id}`;
    return this.http.delete(url,{responseType:"text"});
  }

  syncLabel(commands){
    const url = `${environment.RESOURCE_SERVER_URL}/label/sync`;
    return this.http.post(url,commands,{responseType:"text"});
  }
}
