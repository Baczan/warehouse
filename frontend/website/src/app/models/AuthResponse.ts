export class AuthResponse {
  private _access_token:string;
  private _expires_in:string;
  private _refresh_token:string;
  private _scope:string;
  private _token_type:string;

  constructor(){

  }


  get access_token(): string {
    return this._access_token;
  }

  set access_token(value: string) {
    this._access_token = value;
  }

  get expires_in(): string {
    return this._expires_in;
  }

  set expires_in(value: string) {
    this._expires_in = value;
  }

  get refresh_token(): string {
    return this._refresh_token;
  }

  set refresh_token(value: string) {
    this._refresh_token = value;
  }

  get scope(): string {
    return this._scope;
  }

  set scope(value: string) {
    this._scope = value;
  }

  get token_type(): string {
    return this._token_type;
  }

  set token_type(value: string) {
    this._token_type = value;
  }
}
