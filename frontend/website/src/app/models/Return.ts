import {Auction} from "./Auction";

export class Return {
  public name:string;
  public accountNumber:string;
  public annotation:string;
  public invoice:boolean;
  public form:boolean;
  public status:number;
  public sum:number;
  public auctions:Auction[];


  constructor() {
  }



}
