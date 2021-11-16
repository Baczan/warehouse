export class Car {
  private _id:number;
  private _carName:string;
  private _dateAdded:string;


  constructor() {
  }


  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get carName(): string {
    return this._carName;
  }

  set carName(value: string) {
    this._carName = value;
  }

  get dateAdded(): string {
    return this._dateAdded;
  }

  set dateAdded(value: string) {
    this._dateAdded = value;
  }
}
