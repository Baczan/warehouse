import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import * as crypto from 'crypto-js';
import {finalize, tap} from 'rxjs/operators';
import {AuthResponse} from '../Models/AuthResponse';
import jwt_decode from 'jwt-decode';
import {User} from '../Models/User';
import {CookieService} from 'ngx-cookie';
import {environment} from "../../../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class AuthService {


  current_user:User = null;

  constructor(private http: HttpClient, private router: Router,private cookieService:CookieService) {
  }

  loggedIn(){
    return !!(this.current_user && this.current_user.refresh_token && this.current_user.access_token );
  }

  isAdmin():boolean{
    return !this.current_user.authorities.findIndex(authority=>authority=="ROLE_ADMIN");
  }

  login(url: string = null) {

    //Set redirect url for when authorization code is returned
    if (url) {
      localStorage.setItem('redirect_url', url);
    } else {
      localStorage.setItem('redirect_url', this.router.url);
    }

    //Redirect to login page
    window.location.href = this.generateLoginLink();
  }

  getToken(code: string){

    //Build url
    const url = `${environment.AUTHORIZATION_SERVER_URL}/oauth2/token?grant_type=authorization_code` +
      `&code=${code}` +
      `&scope=read` +
      `&code_verifier=${localStorage.getItem('codeVerifier')}` +
      `&client_id=${environment.CLIENT_ID}` +
      `&redirect_uri=${environment.REDIRECT_URL}`;

    //Save to storage to so u can ignore multiple attempt with the same code
    localStorage.setItem('p_code', code);

    //Save user
    return this.http.post(url, '').pipe(tap((response: AuthResponse) => {
      this.saveUser(response);
    }));
  }

  private generateLoginLink() {

    //Create new code verifier to bo used when exchanging code for token
    const codeVerifier = this.strRandom(128);
    localStorage.setItem('codeVerifier', codeVerifier);

    //Create code challenge to send along with request for PKCE
    const codeVerifierHash = crypto.SHA256(codeVerifier).toString(crypto.enc.Base64);
    const codeChallenge = codeVerifierHash
      .replace(/=/g, '')
      .replace(/\+/g, '-')
      .replace(/\//g, '_');


    //Build login link
    return `${environment.AUTHORIZATION_SERVER_URL}/oauth2/authorize?response_type=code` +
      `&client_id=${environment.CLIENT_ID}` +
      `&scope=read` +
      `&code_challenge=${codeChallenge}` +
      `&code_challenge_method=S256` +
      `&redirect_uri=${environment.REDIRECT_URL}`;
  }

  //Generate random string with specified length
  private strRandom(length: number) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
  }

  saveUser(authResponse: AuthResponse) {
    let decoded_token = jwt_decode(authResponse.access_token);
    // @ts-ignore
    this.current_user = new User(decoded_token.sub,decoded_token.email,decoded_token.authorities,authResponse.access_token,authResponse.refresh_token);
    let current_user_json = JSON.stringify(this.current_user);
    let current_user_base_64 = btoa(current_user_json);

    let expires = new Date().getTime() + 1000 * 60 * 60 * 24 * 365;



    this.cookieService.put("userData",current_user_base_64,{domain:environment.COOKIE_DOMAIN,expires:new Date(expires)})
  }

  loadUser(){
    let previous_user = this.current_user;
    let current_user_base_64 = this.cookieService.get("userData");
    if(current_user_base_64){
      let current_user_json = atob(current_user_base_64);
      this.current_user = JSON.parse(current_user_json);

      //Reload page when user changes
      if(previous_user && previous_user.id!=this.current_user.id){
        window.location.reload()
      }
    }
  }

  logout(redirect_url:string = environment.DEFAULT_LOGOUT_URL){
    if(this.loggedIn()){
      let url = `${environment.AUTHORIZATION_SERVER_URL}/oauth2/revoke?token=${this.current_user.refresh_token}`;
      this.http.post(url,null).pipe(finalize(()=>{
        this.cleanUserDetailsAndLogout(redirect_url);
      })).subscribe();
    }else{
      this.cleanUserDetailsAndLogout(redirect_url);
    }
  }

  deleteAccessToken(){
    this.loadUser();
    if(this.current_user){
      this.current_user.access_token="abc";
      let current_user_json = JSON.stringify(this.current_user);
      let current_user_base_64 = btoa(current_user_json);

      this.cookieService.put("userData",current_user_base_64,{domain:environment.COOKIE_DOMAIN})
    }
  }

  deleteRefreshToken(){
    this.loadUser();
    if(this.current_user){
      this.current_user.refresh_token="abc";
      let current_user_json = JSON.stringify(this.current_user);
      let current_user_base_64 = btoa(current_user_json);

      this.cookieService.put("userData",current_user_base_64,{domain:environment.COOKIE_DOMAIN})
    }
  }

  private cleanUserDetailsAndLogout(redirect_url){
    this.cookieService.remove("userData",{domain:environment.COOKIE_DOMAIN});
    window.location.href = `${environment.AUTHORIZATION_SERVER_URL}/logout?redirect_uri=${redirect_url}`;
  }

  refreshToken(){
    let url;

    if(this.loggedIn()){
      url = `http://localhost:8080/oauth2/token?refresh_token=${this.current_user.refresh_token}&grant_type=refresh_token`;
    }else{
      url = `http://localhost:8080/oauth2/token?refresh_token=&grant_type=refresh_token`
    }

    return this.http.post(url,null).pipe(tap((authResponse:AuthResponse)=>{
      this.saveUser(authResponse)
    }))
  }

}
