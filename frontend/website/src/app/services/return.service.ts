import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Return} from "../models/Return";
import {environment} from "../../environments/environment";
import {ReturnSearchResponse} from "../models/ReturnSearchResponse";

@Injectable({
  providedIn: 'root'
})
export class ReturnService {

  constructor(private http:HttpClient) { }

  addReturn(returnModel:Return){
    const url = `${environment.RESOURCE_SERVER_URL}/return/add`;

    return this.http.post(url,returnModel);
  }

  getReturn(id){
    const url = `${environment.RESOURCE_SERVER_URL}/return/get?id=${id}`;

    return this.http.get<Return>(url);
  }

  updateReturn(returnModel:Return,id){
    const url = `${environment.RESOURCE_SERVER_URL}/return/update?id=${id}`;

    return this.http.post(url,returnModel);
  }

  deleteReturn(id){
    const url = `${environment.RESOURCE_SERVER_URL}/return/delete?id=${id}`;

    return this.http.delete(url,{responseType:"text"});
  }

  searchReturns(searchText,pageNumber){
    const url = `${environment.RESOURCE_SERVER_URL}/return/search?searchText=${searchText}&pageNumber=${pageNumber}&pageSize=25`;
    return this.http.get<ReturnSearchResponse>(url);
  }
}
