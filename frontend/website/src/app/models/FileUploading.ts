import {Subscription} from "rxjs";

export class FileUploading {
  private _filename:string;
  private _carId:number;
  private _subscription:Subscription;
  private _progress:number= 0;
  private _error:boolean = false;
  private _completed:boolean = false;


  constructor() {
  }


  get filename(): string {
    return this._filename;
  }

  set filename(value: string) {
    this._filename = value;
  }

  get carId(): number {
    return this._carId;
  }

  set carId(value: number) {
    this._carId = value;
  }

  get subscription(): Subscription {
    return this._subscription;
  }

  set subscription(value: Subscription) {
    this._subscription = value;
  }

  get progress(): number {
    return this._progress;
  }

  set progress(value: number) {
    this._progress = value;
  }

  get error(): boolean {
    return this._error;
  }

  set error(value: boolean) {
    this._error = value;
  }

  get completed(): boolean {
    return this._completed;
  }

  set completed(value: boolean) {
    this._completed = value;
  }
}
