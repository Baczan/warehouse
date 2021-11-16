export class Label {
  private _id:number;
  private _partName:string;
  private _warehouseNumber:string;
  private _carName:string;
  private _warehouseNumberScanned:string;


  constructor() {
  }


  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get partName(): string {
    return this._partName;
  }

  set partName(value: string) {
    this._partName = value;
  }

  get warehouseNumber(): string {
    return this._warehouseNumber;
  }

  set warehouseNumber(value: string) {
    this._warehouseNumber = value;
  }

  get carName(): string {
    return this._carName;
  }

  set carName(value: string) {
    this._carName = value;
  }


  get warehouseNumberScanned(): string {
    return this._warehouseNumberScanned;
  }

  set warehouseNumberScanned(value: string) {
    this._warehouseNumberScanned = value;
  }
}
