import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input, OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {DrawerService} from '../../../../../services/drawer.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-list-header',
  templateUrl: './list-header.component.html',
  styleUrls: ['./list-header.component.css']
})
export class ListHeaderComponent implements OnInit, AfterViewInit,OnDestroy {

  @Input() searchText;
  @Input() sortBy;
  @Input() direction;

  @Output() sortingChanged: EventEmitter<string> = new EventEmitter<string>();
  @ViewChild('listItem') listItem: ElementRef;
  lastNameDisplay: boolean = true;
  firstNameDisplay: boolean = true;
  dateDisplay: boolean = true;
  drawerSubscription:Subscription;

  constructor(private drawerService: DrawerService, private cdr: ChangeDetectorRef) {
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

  changeSorting(sortBy: string) {
    this.sortingChanged.emit(sortBy);
  }

  ngOnDestroy(): void {
    this.drawerSubscription.unsubscribe();
  }

}
