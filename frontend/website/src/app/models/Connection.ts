export class Connection {
  private _service:string;
  private _username:string;

  constructor() {
  }

  get service(): string {
    return this._service;
  }

  set service(value: string) {
    this._service = value;
  }

  get username(): string {
    return this._username;
  }

  set username(value: string) {
    this._username = value;
  }
}
