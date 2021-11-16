import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {PartsResponse} from "../models/PartsResponse";
import {PartModel} from "../models/PartModel";

@Injectable({
  providedIn: 'root'
})
export class PartsService {

  constructor(private http:HttpClient) { }

  getParts(searchText:string,pageNumber:number){
    const url = `${environment.RESOURCE_SERVER_URL}/parts/search?pageNumber=${pageNumber}&pageSize=50&searchText=${searchText}`;
    return this.http.get<PartsResponse>(url)
  }

  addPart(partName:string,warehouseNumber:string){
    const url = `${environment.RESOURCE_SERVER_URL}/parts/add?partName=${partName}&warehouseNumber=${warehouseNumber}`
    return this.http.post(url,"")
  }

  getPart(id:string){
    const url = `${environment.RESOURCE_SERVER_URL}/parts/get?id=${id}`;
    return this.http.get<PartModel>(url);
  }

  updatePart(id,partName:string,warehouseNumber:string){
    const url = `${environment.RESOURCE_SERVER_URL}/parts/update?id=${id}&partName=${partName}&warehouseNumber=${warehouseNumber}`
    return this.http.post<PartModel>(url,"")
  }

  deletePart(id){
    const url = `${environment.RESOURCE_SERVER_URL}/parts/delete?id=${id}`;
    return this.http.delete(url,{responseType:"text"});
  }
}
