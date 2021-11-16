import { Injectable } from '@angular/core';
import {NewCar} from "../modules/auth/Models/NewCar";
import {environment} from "../../environments/environment";
import {HttpClient, HttpEventType} from "@angular/common/http";
import {FileUploading} from "../models/FileUploading";
import {Image} from "../models/Image";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NewCarService {

  cars:NewCar[] = [];
  currentFileUploads:FileUploading[] = [];
  currentCarId = null;
  images:Image[] = [];


  constructor(private http:HttpClient,private snackBar: MatSnackBar) { }

  addCar(carName:string){
    const url = `${environment.RESOURCE_SERVER_URL}/newCar/add?carName=${carName}`;
    return this.http.post(url,null);
  }

  getAll(){
    const url = `${environment.RESOURCE_SERVER_URL}/newCar/getAll`;
    this.http.get<NewCar[]>(url).subscribe(response=>{
      this.cars = response;
    },error => {
      this.snackBar.open("Błąd podczas pobierania samochodów", "OK", {
        duration: 2000
      });
    })
  }

  get(id:string){
    const url = `${environment.RESOURCE_SERVER_URL}/newCar/get?id=${id}`;
    return this.http.get<NewCar>(url);
  }

  updateCar(id:string,car:NewCar){
    const url = `${environment.RESOURCE_SERVER_URL}/newCar/update?id=${id}`;
    return this.http.post<NewCar>(url,car);
  }

  uploadImage(file:File,carId){

    let fileUploading:FileUploading = new FileUploading();
    fileUploading.filename = file.name;
    fileUploading.carId = carId;


    const url = `${environment.RESOURCE_SERVER_URL}/newCar/uploadImage?carId=${carId}`;
    let formData = new FormData();
    formData.append("file",file,file.name);

    fileUploading.subscription = this.http.post<Image>(url,formData,{reportProgress: true, observe: "events"})
      .subscribe(response=>{

      if(response.type == HttpEventType.UploadProgress){
        fileUploading.progress= (response.loaded/response.total);
      }

      if(response.type == HttpEventType.Response){
        fileUploading.completed = true;

        this.currentFileUploads.splice(this.currentFileUploads.indexOf(fileUploading),1);

        let image:Image = response.body;
        if(image.carId==this.currentCarId){
          this.getAllImages(this.currentCarId);
        }

      }

    },error => {
      fileUploading.error = true;
      this.currentFileUploads.splice(this.currentFileUploads.indexOf(fileUploading),1);
        this.snackBar.open("Błąd podczas wstawiania zdjęcia", "OK", {
          duration: 2000
        });
    });

    this.currentFileUploads.push(fileUploading);
  }

  getAllImages(carId,resetImages=false){

    this.currentCarId = carId;

    if(resetImages){
      this.images = [];
    }


    const url = `${environment.RESOURCE_SERVER_URL}/newCar/getAllImages?carId=${carId}`;
    this.http.get<Image[]>(url).subscribe(response=>{
      this.images = response;
    },error => {
      this.snackBar.open("Błąd podczas pobierania zdjęć", "OK", {
        duration: 2000
      });
    })
  }

  deleteImage(imageId){

    const url = `${environment.RESOURCE_SERVER_URL}/newCar/deleteImage?imageId=${imageId}`;

    return this.http.post(url,null,{responseType:"text"});
  }

  deleteCar(carId){
    const url = `${environment.RESOURCE_SERVER_URL}/newCar/deleteCar?carId=${carId}`;
    return this.http.post(url,null,{responseType:"text"});
  }


}
