export class User {
  id: string;
  email: string;
  authorities: string[];
  access_token: string;
  refresh_token: string;


  constructor(id: string, email: string, authorities: string[], access_token: string, refresh_token: string) {
    this.id = id;
    this.email = email;
    this.authorities = authorities;
    this.access_token = access_token;
    this.refresh_token = refresh_token;
  }

}
