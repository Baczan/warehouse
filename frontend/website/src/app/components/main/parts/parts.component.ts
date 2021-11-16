import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl} from "@angular/forms";
import {Subscription} from "rxjs";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {PartsService} from "../../../services/parts.service";
import {Part} from "../../../models/Part";

@Component({
  selector: 'app-parts',
  templateUrl: './parts.component.html',
  styleUrls: ['./parts.component.css']
})
export class PartsComponent implements OnInit,OnDestroy,AfterViewInit {

  pageNumber = 0;
  searchText = "";
  searchControl: FormControl;
  valueSubscription:Subscription;
  paginatorSubscription:Subscription;
  parts:Part[];

  paramSubscription:Subscription;

  @ViewChild('paginator') paginator: MatPaginator;

  constructor(private route: ActivatedRoute, private router: Router,private partService:PartsService) {
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

      this.getParts();

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

    this.router.navigate(["app", "parts"], {
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

  getParts(){
    this.partService.getParts(this.searchText,this.pageNumber).subscribe(response=>{
      this.parts = response.parts;
      this.paginator.length = response.hits;
    })
  }

  clear(){
    this.searchControl.setValue("");
  }

}
