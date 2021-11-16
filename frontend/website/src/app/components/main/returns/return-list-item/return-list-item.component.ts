import {Component, Input, OnInit} from '@angular/core';
import {ReturnWithId} from "../../../../models/ReturnWithId";
import {Router} from "@angular/router";

@Component({
  selector: 'app-return-list-item',
  templateUrl: './return-list-item.component.html',
  styleUrls: ['./return-list-item.component.css']
})
export class ReturnListItemComponent implements OnInit {

  @Input() returnModel:ReturnWithId;
  @Input() index:number;
  @Input() last:boolean = false;

  constructor(private router:Router) { }

  ngOnInit(): void {
  }

  getStatus(){
    if(this.returnModel.status==0){
      return "NIE OPŁACONE"
    }

    if(this.returnModel.status==1){
      return "OPŁACONE"
    }
  }

  navigate(){
    this.router.navigate(["app","returns",this.returnModel.id])
  }

}
