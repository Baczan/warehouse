import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl} from "@angular/forms";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Part} from "../../../models/Part";
import {ActivatedRoute, Router} from "@angular/router";
import {PartsService} from "../../../services/parts.service";
import {ReturnWithId} from "../../../models/ReturnWithId";
import {ReturnService} from "../../../services/return.service";

@Component({
  selector: 'app-returns',
  templateUrl: './returns.component.html',
  styleUrls: ['./returns.component.css']
})
export class ReturnsComponent implements OnInit,OnDestroy,AfterViewInit {

  pageNumber = 0;
  searchText = "";
  searchControl: FormControl;
  valueSubscription:Subscription;
  paginatorSubscription:Subscription;
  returns:ReturnWithId[];

  paramSubscription:Subscription;

  @ViewChild('paginator') paginator: MatPaginator;

  constructor(private route: ActivatedRoute, private router: Router,private returnService:ReturnService) {
  }

  ngOnInit(): void {

    this.paramSubscription = this.route.queryParams.subscribe(params => {

      if (params.searchText) {
        this.searchText = params.searchText;
      } else {
        this.searchText = "";
      }

      if (params.pageNumber) {
        this.pageNumber = params.pageNumber;
      } else {
        this.pageNumber = 0;
      }

      this.searchControl = new FormControl(this.searchText);

      this.valueSubscription = this.searchControl.valueChanges.subscribe(value => {
        this.searchText=value;
        this.pageNumber=0;
        this.navigate();
      });

      this.getReturns();

    })

  }

  ngAfterViewInit(): void {

    this.paginator.pageIndex = this.pageNumber;

    this.paginatorSubscription = this.paginator.page.subscribe((page:PageEvent)=>{
      this.pageNumber = page.pageIndex;
      this.navigate();
    })
  }

  navigate() {

    this.router.navigate(["app", "returns"], {
      queryParams: {
        searchText: this.searchText,
        pageNumber: this.pageNumber
      }
    })
  }

  ngOnDestroy(): void {
    this.valueSubscription.unsubscribe();
    this.paginatorSubscription.unsubscribe();
    this.paramSubscription.unsubscribe();
  }

  getReturns(){
    this.returnService.searchReturns(this.searchText,this.pageNumber).subscribe(response=>{
      this.returns = response.returns;
      this.paginator.length = response.hits;
    })
  }

  clear(){
    this.searchControl.setValue("");
  }

  addNewReturn(){
    this.router.navigate(["app","returns","add"])
  }


}
