import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NewCarService} from "../../../../services/new-car.service";
import {GalleryService} from "../../../../services/gallery.service";

@Component({
  selector: 'app-car-gallery',
  templateUrl: './car-gallery.component.html',
  styleUrls: ['./car-gallery.component.css']
})
export class CarGalleryComponent implements OnInit {

  imageLoaded = false;

  constructor(private carService:NewCarService,public galleryService:GalleryService) { }

  ngOnInit(): void {
  }

  next(event:Event){
    this.imageLoaded = false;
    event.stopImmediatePropagation();
    this.galleryService.next();
  }

  back(event:Event){
    this.imageLoaded = false;
    event.stopImmediatePropagation();
    this.galleryService.back();
  }

  nextSwipe(){

    if(this.galleryService.canNext()){
      this.imageLoaded = false;
      this.galleryService.next();
    }

  }

  backSwipe(){

    if(this.galleryService.canBack()){
      this.imageLoaded = false;
      this.galleryService.back();
    }

  }


}
