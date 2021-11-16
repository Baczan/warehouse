import {Injectable} from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {catchError, filter, switchMap, take} from 'rxjs/operators';
import {AuthResponse} from '../Models/AuthResponse';
import {environment} from "../../../../environments/environment";
import {ConnectService} from "../../../services/connect.service";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  public isRefreshing = false;
  public isRefreshingAllegro = false;

  public refreshingSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);
  public refreshingSubjectAllegro: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);

  private previousUserId = null;

  constructor(private auth: AuthService, private router: Router,private connectService:ConnectService) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    //Get current url
    let url = req.url.split('?')[0];

    //If logged in and not sending requests to special url add access token
    if (url != `${environment.AUTHORIZATION_SERVER_URL}/oauth2/token` && url != `${environment.AUTHORIZATION_SERVER_URL}/oauth2/revoke`) {
      req = this.addAccessToken(req);
    } else {
      //Add client credentials for special urls
      req = AuthInterceptor.addClientCredentials(req);
    }


    return next.handle(req).pipe(catchError(err => {
      if (err instanceof HttpErrorResponse && err.status == 401) {
        return this.handle401(req, next)
      }if(err instanceof HttpErrorResponse && err.status == 403){
        this.router.navigateByUrl(environment.DEFAULT_UNAUTHORIZED_URL)
      }if(err instanceof HttpErrorResponse && err.status == 400){
        if(err.error == "allegro_unauthorized"){
          return this.handle401Allegro(req, next);
        }
      }

      return throwError(err);
    }));
  }

  handle401(request: HttpRequest<any>, next: HttpHandler):Observable<HttpEvent<any>> {

    if(!this.isRefreshing){
        this.isRefreshing=true;

        this.refreshingSubject.next(null);

        this.auth.refreshToken().pipe(switchMap((authResponse:AuthResponse)=>{

          this.isRefreshing = false;
          this.refreshingSubject.next(true);

          return next.handle(this.addAccessToken(request));
        }),catchError(err => {

          this.auth.login();
          return throwError(err)
        })).subscribe();

    }else{
      return this.refreshingSubject.pipe(
        filter(refreshed => refreshed!=null),
        take(1),
        switchMap(refreshed=>{
          return next.handle(this.addAccessToken(request));
        })
      )
    }

  }

  handle401Allegro(request: HttpRequest<any>, next: HttpHandler):Observable<HttpEvent<any>>{

    if(!this.isRefreshingAllegro){
      this.isRefreshingAllegro=true;

      this.refreshingSubjectAllegro.next(null);

      this.connectService.refreshAllegro().pipe(switchMap((response)=>{

        this.isRefreshingAllegro = false;
        this.refreshingSubjectAllegro.next(true);

        return next.handle(this.addAccessToken(request));
      }),catchError(err => {
        this.router.navigate(["app","connect"]);
        return throwError(err)
      })).subscribe();

    }else{

      return this.refreshingSubjectAllegro.pipe(
        filter(refreshed =>{
          return refreshed!=null
        } ),
        take(1),
        switchMap(refreshed=>{
          return next.handle(this.addAccessToken(request));
        })
      )
    }
  }

  //Add token to request
  private addAccessToken(request: HttpRequest<any>){

    this.auth.loadUser();

    if(this.previousUserId){
      if(this.previousUserId !=this.auth.current_user.id ){
        window.location.reload()
      }
    }

    if(this.auth.loggedIn()){

      this.previousUserId = this.auth.current_user.id;
      return request.clone({
        headers: request.headers.set(
          'Authorization', 'Bearer ' + this.auth.current_user.access_token
        )
      });
    }else{
      return request;
    }

  }

  //Add token to request
  private static addClientCredentials(request: HttpRequest<any>) {
    return request.clone({
      headers: request.headers.set(
        'Authorization', 'Basic ' + btoa(`${environment.CLIENT_ID}:${environment.CLIENT_SECRET}`)
      )
    });
  }

}
