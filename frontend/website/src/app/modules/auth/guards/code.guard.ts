import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, ParamMap, UrlSegment} from '@angular/router';
import {Observable, of} from 'rxjs';
import {AuthService} from '../services/auth.service';
import {catchError, map} from 'rxjs/operators';
import {environment} from "../../../../environments/environment";
import {InitzializingService} from "../../../services/initzializing.service";

@Injectable({
  providedIn: 'root'
})
export class CodeGuard implements CanActivate {

  constructor(private auth:AuthService,private router:Router,private initializingService:InitzializingService){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    //Load user
    this.auth.loadUser();

    //If you have a code in a url get and it is not the previous code get authorization token form the server
    if(state.root.queryParams.code && localStorage.getItem("p_code")!==state.root.queryParams.code){

      return this.auth.getToken(state.root.queryParams.code).pipe(map(response=>{

        this.initializingService.initialize();
        //Get previously saved redirect url and redirect to it after request
        let redirect_url = localStorage.getItem("redirect_url");

        if(redirect_url){
          return this.createUrlTreeFromRedirectUri(redirect_url);
        }else{
          return this.router.parseUrl(environment.DEFAULT_SUCCESSFUL_REDIRECT_URL);
        }

      }),catchError(()=>{
        return of(this.router.parseUrl(environment.DEFAULT_NOT_SUCCESSFUL_REDIRECT_URL))
      }))
    }
    return true;
  }

  private createUrlTreeFromRedirectUri(redirect_url):UrlTree{
    const urlTree:UrlTree = this.router.parseUrl(redirect_url);
    let params:ParamMap = urlTree.queryParamMap;
    let new_params = [];
    params.keys.forEach(paramKey=>{
      if(paramKey!="code"){
        new_params[paramKey]=params.get(paramKey);
      }
    });
    let navigationCommands = [];
    urlTree.root.children.primary.segments.forEach((command:UrlSegment)=>{
      navigationCommands.push(command.path)
    });

    return this.router.createUrlTree(navigationCommands,{queryParams:new_params});
  }

}
