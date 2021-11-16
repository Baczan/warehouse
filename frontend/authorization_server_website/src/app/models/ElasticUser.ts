// @ts-ignore
export class ElasticUser {

  constructor() {
  }

  private _id: number;

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  private _email: string;

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  private _emailKeyword: string;

  get emailKeyword(): string {
    return this._emailKeyword;
  }

  set emailKeyword(value: string) {
    this._emailKeyword = value;
  }

  private _firstName: string;

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  private _firstNameKeyword: string;

  get firstNameKeyword(): string {
    return this._firstNameKeyword;
  }

  set firstNameKeyword(value: string) {
    this._firstNameKeyword = value;
  }

  private _lastName: string;

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  private _lastNameKeyword: string;

  get lastNameKeyword(): string {
    return this._lastNameKeyword;
  }

  set lastNameKeyword(value: string) {
    this._lastNameKeyword = value;
  }

  private _createdAt: number;

  get createdAt(): number {
    return this._createdAt;
  }

  set createdAt(value: number) {
    this._createdAt = value;
  }
}
