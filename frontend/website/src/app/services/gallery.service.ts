import { Injectable } from '@angular/core';
import {CarService} from "./car.service";
import {NewCarService} from "./new-car.service";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class GalleryService {

  opened = false;
  currentIndex = 0;

  constructor(private carService:NewCarService) { }

  openGallery(index=0){
    this.opened = true;
    this.currentIndex = index;
  }

  closeGallery(){
    this.opened = false;
  }

  getSrc(){

    if(this.carService.images.length>0 && this.currentIndex<this.carService.images.length){
      return `${environment.RESOURCE_SERVER_URL}/newCar/getImage?imageId=${this.carService.images[this.currentIndex].id}`;
    }else {
      return ""
    }

  }

  next(){
    if(this.currentIndex+1<this.carService.images.length){
      this.currentIndex = this.currentIndex+1;
    }
  }

  canNext(){
    return this.currentIndex+1<this.carService.images.length;
  }

  isLast(){
    return this.currentIndex==this.carService.images.length-1;
  }

  back(){
    if(this.currentIndex>0){
      this.currentIndex = this.currentIndex-1;
    }
  }

  canBack(){
    return this.currentIndex>0;
  }

  isFirst(){
    return this.currentIndex==0;
  }
}
