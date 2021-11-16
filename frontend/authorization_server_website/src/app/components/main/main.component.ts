import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints, BreakpointState} from '@angular/cdk/layout';
import {MatDrawer, MatDrawerContainer} from '@angular/material/sidenav';
import {ClientService} from '../../services/client.service';
import {HttpClient} from '@angular/common/http';
import {Client} from '../../models/Client';
import {finalize} from 'rxjs/operators';
import {DrawerService} from '../../services/drawer.service';
import {environment} from '../../../environments/environment';
import {HeaderButtonService} from '../../services/header-button.service';
import {MatButton} from '@angular/material/button';
import {ActivatedRoute, Router} from '@angular/router';
import {ManagerResponse} from '../../models/ManagerResponse';
import {AuthService} from '../../services/auth.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit, OnDestroy {

  @ViewChild('drawer') drawer: MatDrawer;
  @ViewChild('container') container: MatDrawerContainer;
  @ViewChild('headerButton') button: MatButton;

  currentUrl = '';

  opened;
  mode;
  hasBackdrop;

  loadingClients = true;
  loadingManagerResponse = true;
  managerResponseList: ManagerResponse[] = [];

  breakPointSubscription: Subscription;
  routeSubscription: Subscription;

  constructor(private http: HttpClient
    , private breakpointObserver: BreakpointObserver
    , private cdref: ChangeDetectorRef
    , public clientService: ClientService
    , private drawerService: DrawerService
    , public buttonService: HeaderButtonService
    , private route: ActivatedRoute
    , private router: Router
    , public auth: AuthService) {

    this.http.get(`${environment.authorization_server_url}/clients`).pipe(finalize(() => {
      this.loadingClients = false;
    })).subscribe((clients: Client[]) => {
      this.clientService.clients = clients;
    });

    let url = `${environment.authorization_server_url}/roles/get?withDefault=true`;
    this.http.get(url).pipe(finalize(() => {
      this.loadingManagerResponse = false;
    })).subscribe((response: ManagerResponse[]) => {
      this.managerResponseList = response;
      this.managerResponseList = this.managerResponseList.filter(value => value.ownerId != this.auth.user.id);
    });

  }

  ngOnInit(): void {

    this.breakPointSubscription = this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small]).subscribe((state: BreakpointState) => {

      this.opened = !state.matches;
      this.mode = state.matches ? 'over' : 'side';
      this.hasBackdrop = state.matches;

      this.cdref.detectChanges();
    });

    this.routeSubscription = this.route.url.subscribe(url => {
      this.currentUrl = this.router.routerState.snapshot.url;
    });
  }

  logout() {
    window.location.href = `${environment.authorization_server_url}/logout`;
  }

  emit(event) {
    this.drawerService.drawerEvent.emit(event);
  }

  ngOnDestroy(): void {
  this.breakPointSubscription.unsubscribe();
  this.routeSubscription.unsubscribe();
  }

}
