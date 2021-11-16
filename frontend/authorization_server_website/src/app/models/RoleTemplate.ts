export class RoleTemplate {
  public id: number;
  public clientId: string;
  public defaultRole: boolean;
  public ownerOnly: boolean;
  public role: string;
  public active: boolean;


  constructor(id: number, clientId: string, defaultRole: boolean, ownerOnly: boolean, role: string, active: boolean) {
    this.id = id;
    this.clientId = clientId;
    this.defaultRole = defaultRole;
    this.ownerOnly = ownerOnly;
    this.role = role;
    this.active = active;
  }
}
