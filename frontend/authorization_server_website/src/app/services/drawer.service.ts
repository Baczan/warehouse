import {EventEmitter, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DrawerService {

  drawerEvent: EventEmitter<boolean> = new EventEmitter();

  constructor() {
  }
}
