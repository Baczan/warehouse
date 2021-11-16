import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ElasticUser} from '../../../../../models/ElasticUser';
import {DrawerService} from '../../../../../services/drawer.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.css']
})
export class ListItemComponent implements OnInit, AfterViewInit,OnDestroy {

  @Input('user') user: ElasticUser;
  @ViewChild('listItem') listItem: ElementRef;
  lastNameDisplay: boolean = true;
  firstNameDisplay: boolean = true;
  dateDisplay: boolean = true;
  drawerSubscription:Subscription;

  constructor(private drawerService: DrawerService, private router: Router, private cdr: ChangeDetectorRef) {
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.adjust();
  }

  ngOnInit(): void {

    this.drawerSubscription = this.drawerService.drawerEvent.subscribe(() => {
      this.adjust();
    });

  }

  adjust() {
    let width = this.listItem.nativeElement.offsetWidth;
    this.lastNameDisplay = width >= 720;
    this.firstNameDisplay = width >= 630;
    this.dateDisplay = width >= 500;
    this.cdr.detectChanges();
  }

  ngAfterViewInit(): void {
    this.adjust();
  }

  navigate() {
    this.router.navigate(['main', 'roles', 'assign', this.user.id, this.user.email]);
  }

  ngOnDestroy(): void {
    this.drawerSubscription.unsubscribe();
  }


}
