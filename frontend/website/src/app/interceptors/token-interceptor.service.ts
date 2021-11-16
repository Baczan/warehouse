import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {AuthService} from '../modules/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  constructor(private auth: AuthService, private router: Router) {
  }

  //Add token to request
  private static addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      headers: request.headers.set(
        'Authorization', 'Bearer ' + token
      )
    });
  }

  //Add token to request
  private static addToken1(request: HttpRequest<any>) {
    return request.clone({
      headers: request.headers.set(
        'Authorization', 'Basic ' + btoa('client:12345')
      )
    });
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let url = req.url.split('?')[0];

    if (this.auth.loggedIn() && (url !== 'http://localhost:8080/oauth2/token' && url !== 'http://localhost:8080/oauth2/revoke')) {
      req = TokenInterceptorService.addToken(req, this.auth.current_user.access_token);
    } else {
      req = TokenInterceptorService.addToken1(req);
    }
    return next.handle(req).pipe(catchError(err => {

      if (err instanceof HttpErrorResponse && err.status === 401) {
        this.auth.login();
      } else if (err instanceof HttpErrorResponse && err.status === 403) {
        alert('Nie masz uprawnień');
        if (this.auth.loggedIn()) {
          this.router.navigate(['app', 'user']);
        } else {
          this.router.navigate(['app']);
        }
      }
      //If its other error type throw it normally
      return throwError(err);
    }));
  }

}
