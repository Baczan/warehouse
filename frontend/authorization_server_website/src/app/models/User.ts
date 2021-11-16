export class User {
  private _id;

  get id() {
    return this._id;
  }

  set id(value) {
    this._id = value;
  }

  private _email;

  get email() {
    return this._email;
  }

  set email(value) {
    this._email = value;
  }

  private _authorities;

  get authorities() {
    return this._authorities;
  }

  set authorities(value) {
    this._authorities = value;
  }
}
