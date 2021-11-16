import {Injectable} from '@angular/core';
import {Client} from '../models/Client';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  clients: Client[] = [];


  constructor() {
  }
}
