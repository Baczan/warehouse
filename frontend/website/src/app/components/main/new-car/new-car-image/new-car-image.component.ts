import {Component, Input, OnInit} from '@angular/core';
import {Image} from "../../../../models/Image";
import {environment} from "../../../../../environments/environment";
import {GalleryService} from "../../../../services/gallery.service";
import {NewCarService} from "../../../../services/new-car.service";

@Component({
  selector: 'app-new-car-image',
  templateUrl: './new-car-image.component.html',
  styleUrls: ['./new-car-image.component.css']
})
export class NewCarImageComponent implements OnInit {

  @Input() image: Image;
  @Input() index: number;


  src: string;

  constructor(public galleryService:GalleryService,public newCarService:NewCarService) {
  }

  ngOnInit(): void {

    this.src = `${environment.RESOURCE_SERVER_URL}/newCar/getImage?imageId=${this.image.id}&&thumbnail=true`

  }


  deleteImage(){

    this.newCarService.deleteImage(this.image.id).subscribe(response=>{

      this.newCarService.getAllImages(this.image.carId,false);
      console.log(response)
    },error => {
      console.log(error)
    })

  }

}
