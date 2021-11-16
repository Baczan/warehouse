import {EventEmitter, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HeaderButtonService {

  roleAddButtonEvent: EventEmitter<any> = new EventEmitter();

  constructor() {
  }
}
