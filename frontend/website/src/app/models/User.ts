export class User {
  private _id:string;
  private _email:string;
  private _authorities:string[];
  private _access_token:string;
  private _refresh_token:string;
  private _access_token_exp:number;
  private _refresh_token_exp:number;

  constructor(){

  }

  setUserFromTokens(decoded_token,access_token,refresh_token){
    this.id = decoded_token.sub;
    this.email=decoded_token.email;
    this.authorities=decoded_token.authorities;
    this.access_token=access_token;
    this.refresh_token=refresh_token;
    this.access_token_exp=decoded_token.exp*1000;
    this.refresh_token_exp=new Date().getTime()+(1000*60*60*24*60);
  }


  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get authorities(): string[] {
    return this._authorities;
  }

  set authorities(value: string[]) {
    this._authorities = value;
  }

  get access_token(): string {
    return this._access_token;
  }

  set access_token(value: string) {
    this._access_token = value;
  }

  get refresh_token(): string {
    return this._refresh_token;
  }

  set refresh_token(value: string) {
    this._refresh_token = value;
  }

  get access_token_exp(): number {
    return this._access_token_exp;
  }

  set access_token_exp(value: number) {
    this._access_token_exp = value;
  }

  get refresh_token_exp(): number {
    return this._refresh_token_exp;
  }

  set refresh_token_exp(value: number) {
    this._refresh_token_exp = value;
  }
}
