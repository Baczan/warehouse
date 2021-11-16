export class Client {
  constructor() {
  }

  private _clientId: string;

  get clientId() {
    return this._clientId;
  }

  set clientId(value) {
    this._clientId = value;
  }

  private _clientSecret: string;

  get clientSecret(): string {
    return this._clientSecret;
  }

  set clientSecret(value: string) {
    this._clientSecret = value;
  }

  private _name: string;

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  private _requireUserConsent: boolean;

  get requireUserConsent(): boolean {
    return this._requireUserConsent;
  }

  set requireUserConsent(value: boolean) {
    this._requireUserConsent = value;
  }

  private _requireProofKey: boolean;

  get requireProofKey(): boolean {
    return this._requireProofKey;
  }

  set requireProofKey(value: boolean) {
    this._requireProofKey = value;
  }

  private _reuseRefreshToken: boolean;

  get reuseRefreshToken(): boolean {
    return this._reuseRefreshToken;
  }

  set reuseRefreshToken(value: boolean) {
    this._reuseRefreshToken = value;
  }

  private _owner: number;

  get owner(): number {
    return this._owner;
  }

  set owner(value: number) {
    this._owner = value;
  }

  private _accessTokenTimeToLive: string;

  get accessTokenTimeToLive(): string {
    return this._accessTokenTimeToLive;
  }

  set accessTokenTimeToLive(value: string) {
    this._accessTokenTimeToLive = value;
  }

  private _refreshTokenTimeToLive: string;

  get refreshTokenTimeToLive(): string {
    return this._refreshTokenTimeToLive;
  }

  set refreshTokenTimeToLive(value: string) {
    this._refreshTokenTimeToLive = value;
  }

  private _redirectUrls: string[];

  get redirectUrls(): string[] {
    return this._redirectUrls;
  }

  set redirectUrls(value: string[]) {
    this._redirectUrls = value;
  }

  private _authenticationMethodBasic: boolean;

  get authenticationMethodBasic(): boolean {
    return this._authenticationMethodBasic;
  }

  set authenticationMethodBasic(value: boolean) {
    this._authenticationMethodBasic = value;
  }

  private _authenticationMethodPost: boolean;

  get authenticationMethodPost(): boolean {
    return this._authenticationMethodPost;
  }

  set authenticationMethodPost(value: boolean) {
    this._authenticationMethodPost = value;
  }

  private _authenticationMethodNone: boolean;

  get authenticationMethodNone(): boolean {
    return this._authenticationMethodNone;
  }

  set authenticationMethodNone(value: boolean) {
    this._authenticationMethodNone = value;
  }

  private _authorizationGrantTypeAuthorizationCode: boolean;

  get authorizationGrantTypeAuthorizationCode(): boolean {
    return this._authorizationGrantTypeAuthorizationCode;
  }

  set authorizationGrantTypeAuthorizationCode(value: boolean) {
    this._authorizationGrantTypeAuthorizationCode = value;
  }

  private _authorizationGrantTypeImplicit: boolean;

  get authorizationGrantTypeImplicit(): boolean {
    return this._authorizationGrantTypeImplicit;
  }

  set authorizationGrantTypeImplicit(value: boolean) {
    this._authorizationGrantTypeImplicit = value;
  }

  private _authorizationGrantTypeRefreshToken: boolean;

  get authorizationGrantTypeRefreshToken(): boolean {
    return this._authorizationGrantTypeRefreshToken;
  }

  set authorizationGrantTypeRefreshToken(value: boolean) {
    this._authorizationGrantTypeRefreshToken = value;
  }

  private _authorizationGrantTypeClientCredentials: boolean;

  get authorizationGrantTypeClientCredentials(): boolean {
    return this._authorizationGrantTypeClientCredentials;
  }

  set authorizationGrantTypeClientCredentials(value: boolean) {
    this._authorizationGrantTypeClientCredentials = value;
  }

  private _authorizationGrantTypePassword: boolean;

  get authorizationGrantTypePassword(): boolean {
    return this._authorizationGrantTypePassword;
  }

  set authorizationGrantTypePassword(value: boolean) {
    this._authorizationGrantTypePassword = value;
  }
}
