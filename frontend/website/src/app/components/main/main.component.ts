import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../../modules/auth/services/auth.service";
import {MatDrawer, MatDrawerContainer} from "@angular/material/sidenav";
import {MatButton} from "@angular/material/button";
import {BreakpointObserver, Breakpoints, BreakpointState} from "@angular/cdk/layout";
import {Subscription} from "rxjs";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit,OnDestroy {

  @ViewChild('drawer') drawer: MatDrawer;
  @ViewChild('container') container: MatDrawerContainer;

  opened;
  mode;
  hasBackdrop;

  breakPointSubscription: Subscription;


  constructor(public auth:AuthService, private breakpointObserver: BreakpointObserver
    , private cdref: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.breakPointSubscription = this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small]).subscribe((state: BreakpointState) => {

      this.opened = !state.matches;
      this.mode = state.matches ? 'over' : 'side';
      this.hasBackdrop = state.matches;

      this.cdref.detectChanges();
    });
  }

  openAccountPage(){
    window.open(environment.ACCOUNT_PAGE_URL, '_blank');
  }



  ngOnDestroy(): void {
    this.breakPointSubscription.unsubscribe();
  }
}
