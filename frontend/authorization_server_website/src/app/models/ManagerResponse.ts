import {RoleTemplate} from './RoleTemplate';

export class ManagerResponse {
  constructor() {
  }

  private _ownerId: number;

  get ownerId(): number {
    return this._ownerId;
  }

  set ownerId(value: number) {
    this._ownerId = value;
  }

  private _clientId: string;

  get clientId(): string {
    return this._clientId;
  }

  set clientId(value: string) {
    this._clientId = value;
  }

  private _clientName: string;

  get clientName(): string {
    return this._clientName;
  }

  set clientName(value: string) {
    this._clientName = value;
  }

  private _rolesTemplates: RoleTemplate[];

  get rolesTemplates(): RoleTemplate[] {
    return this._rolesTemplates;
  }

  set rolesTemplates(value: RoleTemplate[]) {
    this._rolesTemplates = value;
  }
}
