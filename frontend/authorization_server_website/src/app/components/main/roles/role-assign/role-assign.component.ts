import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ElasticUser} from '../../../../models/ElasticUser';
import {HttpClient} from '@angular/common/http';
import {FormControl} from '@angular/forms';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {ElasticUserResponse} from '../../../../models/ElasticUserResponse';
import {environment} from '../../../../../environments/environment';
import {Title} from '@angular/platform-browser';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-role-assign',
  templateUrl: './role-assign.component.html',
  styleUrls: ['./role-assign.component.css']
})
export class RoleAssignComponent implements OnInit, AfterViewInit,OnDestroy {

  users: ElasticUser[] = [];
  pageSizeOptions: number[] = [5, 10, 25, 100];
  previousPageSize = 25;

  pageIndex = 0;
  pageSize = 25;

  searchTextControl: FormControl;

  sortBy: string = 'email';
  direction = 'asc';

  searchTextSubscription:Subscription;
  paginatorSubscription:Subscription;

  @ViewChild('paginator') paginator: MatPaginator;


  constructor(private http: HttpClient, private titleService: Title) {
  }


  ngOnInit(): void {

    this.titleService.setTitle('Przypisz role');
    this.searchTextControl = new FormControl('');

    this.searchTextSubscription = this.searchTextControl.valueChanges.subscribe(value => {
      this.paginator.firstPage();
      this.getUsers();
    });

  }

  getUsers() {
    let url = '';

    if (this.searchTextControl.value == '') {
      url = `${environment.authorization_server_url}/elastic/getUsers?sortBy=${this.sortBy}&direction=${this.direction}&pageNumber=${this.pageIndex}&pageSize=${this.pageSize}`;
    } else {
      url = `${environment.authorization_server_url}/elastic/getUsers?searchText=${this.searchTextControl.value}&pageNumber=${this.pageIndex}&pageSize=${this.pageSize}`;
    }

    this.http.get(url).subscribe((response: ElasticUserResponse) => {
      this.users = response.users;
      this.paginator.length = response.numberOfUsers;
    });

  }

  ngAfterViewInit(): void {

    this.paginatorSubscription = this.paginator.page.subscribe((page: PageEvent) => {

      if (this.previousPageSize != page.pageSize) {
        let totalNumberOfItems = (this.pageIndex + 1) * this.previousPageSize;

        if (this.previousPageSize < page.pageSize) {
          this.pageIndex = Math.floor(totalNumberOfItems / page.pageSize);
        } else {
          this.pageIndex = Math.floor(totalNumberOfItems / page.pageSize) - Math.ceil(this.previousPageSize / page.pageSize);
        }

        this.previousPageSize = page.pageSize;

      } else {
        this.pageIndex = page.pageIndex;
      }

      this.pageSize = page.pageSize;

      this.getUsers();
    });

    this.getUsers();

  }

  changeSorting(sortBy: string) {

    if (this.searchTextControl.value == '') {
      if (this.sortBy == sortBy) {
        this.direction = this.direction == 'asc' ? 'desc' : 'asc';
      } else {
        this.sortBy = sortBy;
        this.direction = 'asc';
      }

      this.getUsers();
    }

  }


  ngOnDestroy(): void {

    this.searchTextSubscription.unsubscribe();
    this.paginatorSubscription.unsubscribe();

  }
}
