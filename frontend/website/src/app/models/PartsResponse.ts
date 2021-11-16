import {Part} from "./Part";

export class PartsResponse {
  private _hits:number;
  private _parts:Part[];


  constructor() {
  }


  get hits(): number {
    return this._hits;
  }

  set hits(value: number) {
    this._hits = value;
  }

  get parts(): Part[] {
    return this._parts;
  }

  set parts(value: Part[]) {
    this._parts = value;
  }
}
