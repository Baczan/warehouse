import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


    return next.handle(req).pipe(catchError(err => {
      if (err instanceof HttpErrorResponse) {
        if (err.url == `${environment.authorization_server_url}/auth/login` && err.status == 200 && environment.production) {
          window.location.href = `${environment.authorization_server_url}/logout`;
        }
      }
      return throwError(err);
    }));
  }


}
