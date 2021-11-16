import {ElasticUser} from './ElasticUser';

export class ElasticUserResponse {

  constructor() {
  }

  private _users: ElasticUser[];

  get users(): ElasticUser[] {
    return this._users;
  }

  set users(value: ElasticUser[]) {
    this._users = value;
  }

  private _numberOfUsers: number;

  get numberOfUsers(): number {
    return this._numberOfUsers;
  }

  set numberOfUsers(value: number) {
    this._numberOfUsers = value;
  }
}
