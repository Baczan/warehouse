export class Part {
  private _id:number;
  private _partName:string;
  private _warehouseNumber:string;
  private _highlightedPartName:string;

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

  get highlightedPartName(): string {
    return this._highlightedPartName;
  }

  set highlightedPartName(value: string) {
    this._highlightedPartName = value;
  }
}
