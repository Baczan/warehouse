import {Component, Input, OnInit} from '@angular/core';
import {FileUploading} from "../../../../models/FileUploading";

@Component({
  selector: 'app-new-car-image-upload',
  templateUrl: './new-car-image-upload.component.html',
  styleUrls: ['./new-car-image-upload.component.css']
})
export class NewCarImageUploadComponent implements OnInit {

  @Input() fileUpload:FileUploading;

  constructor() { }

  ngOnInit(): void {
  }

}
