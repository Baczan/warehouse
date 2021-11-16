import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {Return} from "../../../../models/Return";
import {Auction} from "../../../../models/Auction";
import {ReturnService} from "../../../../services/return.service";
import {ActivatedRoute, Router} from "@angular/router";
import {finalize} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Subscription} from "rxjs";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {PartDialog} from "../../parts/update-part/update-part.component";
import {AllegroService} from "../../../../services/allegro.service";

@Component({
  selector: 'app-return-add',
  templateUrl: './return-add.component.html',
  styleUrls: ['./return-add.component.css']
})
export class ReturnAddComponent implements OnInit, OnDestroy {

  formGroup: FormGroup;
  auctionFormGroupsArray: FormGroup[] = [];
  id = null;
  loading = false;
  paramSubscription: Subscription;
  addLoading = false;
  updatingLoading = false;
  deletingLoading = false;
  savedReturnModel: Return = null;

  constructor(private returnService: ReturnService, private route: ActivatedRoute, private router: Router, private snackBar: MatSnackBar, public dialog: MatDialog,private allegroService:AllegroService) {

    this.paramSubscription = route.params.subscribe(params => {

      this.resetForm();

      if (params.id) {
        this.id = params.id;
        this.loading = true;
        this.returnService.getReturn(this.id)
          .pipe(finalize(() => {
            this.loading = false;
          }))
          .subscribe(response => {
            this.loadReturnModel(response);
            this.savedReturnModel = this.getReturnModel();
          }, error => {
            router.navigate(["app", "returns"]).then(() => {
              this.snackBar.open("Nie znaleziono zwrotu", "OK", {
                duration: 2000
              });
            })
          })
      }

    });


  }

  ngOnDestroy(): void {
    this.paramSubscription.unsubscribe();
  }

  ngOnInit(): void {
  }

  addAuction() {

    let formGroup = new FormGroup({
      auctionNumber: new FormControl("", Validators.required),
      auctionTitle: new FormControl("", Validators.required),
      quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
      price: new FormControl(0, Validators.required),
      damaged: new FormControl(false, Validators.required),
    });
    this.auctionFormGroupsArray.push(formGroup);
  }

  deleteAuction(index) {
    this.auctionFormGroupsArray.splice(index, 1);
  }

  addOrUpdateReturn() {
    this.formGroup.markAllAsTouched();
    this.auctionFormGroupsArray.forEach(formGroup => {
      formGroup.markAllAsTouched()
    });

    if (this.checkIfValid()) {
      let returnModel: Return = this.getReturnModel();

      if (this.id) {
        this.updatingLoading = true;
        this.returnService.updateReturn(returnModel, this.id)
          .pipe(finalize(() => {
            this.updatingLoading = false;
          }))
          .subscribe(response => {
            this.snackBar.open("Zapisano zwrot", "OK", {
              duration: 2000
            });
            this.savedReturnModel = this.getReturnModel();
          }, error => {
            this.snackBar.open("Błąd podczas zapisywania zwrotu", "OK", {
              duration: 2000
            });
          })
      } else {
        this.addLoading = true;
        this.returnService.addReturn(returnModel)
          .pipe(finalize(() => {
            this.addLoading = false;
          }))
          .subscribe(response => {
            this.snackBar.open("Dodano zwrot", "OK", {
              duration: 2000
            });
            this.resetForm();

          }, error => {
            this.snackBar.open("Błąd podczas dodawania zwrotu", "OK", {
              duration: 2000
            });
          })
      }

    }
  }

  checkIfValid() {
    if (this.formGroup.invalid) {
      return false;
    }


    for (let i = 0; i < this.auctionFormGroupsArray.length; i++) {
      if (this.auctionFormGroupsArray[i].invalid) {
        return false;
      }
    }
    return true;
  }

  getReturnModel() {
    let returnModel = new Return();
    returnModel.name = this.formGroup.controls['name'].value;
    returnModel.accountNumber = this.formGroup.controls['accountNumber'].value;
    returnModel.annotation = this.formGroup.controls['annotation'].value;
    returnModel.invoice = this.formGroup.controls['invoice'].value;
    returnModel.form = this.formGroup.controls['form'].value;
    returnModel.sum = this.formGroup.controls['sum'].value;
    returnModel.status = this.formGroup.controls['status'].value;

    let auctionsList: Auction[] = [];

    this.auctionFormGroupsArray.forEach(formGroup => {
      let auction = new Auction();
      auction.auctionNumber = formGroup.controls['auctionNumber'].value;
      auction.auctionTitle = formGroup.controls['auctionTitle'].value;
      auction.quantity = formGroup.controls['quantity'].value;
      auction.price = formGroup.controls['price'].value;
      auction.damaged = formGroup.controls['damaged'].value;
      auctionsList.push(auction);
    });

    returnModel.auctions = auctionsList;

    return returnModel;
  }

  loadReturnModel(returnModel: Return) {

    this.formGroup = new FormGroup({
      name: new FormControl(returnModel.name, Validators.required),
      accountNumber: new FormControl(returnModel.accountNumber),
      annotation: new FormControl(returnModel.annotation),
      invoice: new FormControl(returnModel.invoice, Validators.required),
      form: new FormControl(returnModel.form, Validators.required),
      status: new FormControl(returnModel.status, Validators.required),
      sum: new FormControl(returnModel.sum, Validators.required)
    });


    this.auctionFormGroupsArray = [];

    for (let i = 0; i < returnModel.auctions.length; i++) {
      let auction = returnModel.auctions[i];

      this.auctionFormGroupsArray.push(
        new FormGroup({
          auctionNumber: new FormControl(auction.auctionNumber, Validators.required),
          auctionTitle: new FormControl(auction.auctionTitle, Validators.required),
          quantity: new FormControl(auction.quantity, [Validators.required, Validators.min(1)]),
          price: new FormControl(auction.price, Validators.required),
          damaged: new FormControl(auction.damaged, Validators.required),
        }));

    }

  }

  deleteReturn() {

    const dialogRef = this.dialog.open(ReturnDialog);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deletingLoading = true;
        this.returnService.deleteReturn(this.id)
          .pipe(finalize(() => {
            this.deletingLoading = false;
          }))
          .subscribe(response => {
            window.history.back();
            this.snackBar.open("Usunięto zwrot", "OK", {
              duration: 2000
            });

          }, error => {
            this.snackBar.open("Błąd podczas usuwania zwrotu", "OK", {
              duration: 2000
            });
          })
      }
    });


  }

  navigateBack() {

    if (this.savedReturnModel) {

      if (!compareReturns(this.savedReturnModel, this.getReturnModel())) {

        const dialogRef = this.dialog.open(ReturnLeaveDialog);

        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            window.history.back();
          }
        });

      } else {
        window.history.back();
      }
    } else {
      window.history.back();
    }


  }


  resetForm() {
    this.formGroup = new FormGroup({
      name: new FormControl("", Validators.required),
      accountNumber: new FormControl(""),
      annotation: new FormControl(""),
      invoice: new FormControl(false, Validators.required),
      form: new FormControl(false, Validators.required),
      status: new FormControl(0, Validators.required),
      sum: new FormControl(0, Validators.required)
    });

    this.auctionFormGroupsArray = [];

    this.auctionFormGroupsArray.push(
      new FormGroup({
        auctionNumber: new FormControl("", Validators.required),
        auctionTitle: new FormControl("", Validators.required),
        quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
        price: new FormControl(0, Validators.required),
        damaged: new FormControl(false, Validators.required),
      }));

  }

  handleAuctionNumberValueChange(value,index){
    if(value.length>=5){
      this.allegroService.getOffer(value).subscribe(response=>{

        this.auctionFormGroupsArray[index].controls['auctionTitle'].setValue(response.name);
        this.auctionFormGroupsArray[index].controls['price'].setValue(response.price);
        this.calculateSum();
      },error => {
      })
    }
  }

  calculateSum(){

    let sum = 0;

    this.auctionFormGroupsArray.forEach(formGroup=>{

      if(formGroup.controls['price'].value && formGroup.controls['quantity'].value){
        sum = sum + (formGroup.controls['price'].value * formGroup.controls['quantity'].value);
      }

    });

    this.formGroup.controls['sum'].setValue(sum.toFixed(2));

  }

}


export function compareReturns(return1: Return, return2: Return) {

  if (return1.name != return2.name) {
    return false;
  }

  if (return1.accountNumber != return2.accountNumber) {
    return false;
  }

  if (return1.annotation != return2.annotation) {
    return false;
  }

  if (return1.invoice != return2.invoice) {
    return false;
  }

  if (return1.form != return2.form) {
    return false;
  }

  if (return1.status != return2.status) {
    return false;
  }

  if (return1.sum != return2.sum) {
    return false;
  }

  if (return1.auctions.length != return2.auctions.length) {
    return false;
  }

  for (let i = 0; i < return1.auctions.length; i++) {
    if (return1.auctions[i].auctionNumber != return2.auctions[i].auctionNumber) {
      return false;
    }

    if (return1.auctions[i].auctionTitle != return2.auctions[i].auctionTitle) {
      return false;
    }

    if (return1.auctions[i].quantity != return2.auctions[i].quantity) {
      return false;
    }

    if (return1.auctions[i].price != return2.auctions[i].price) {
      return false;
    }

    if (return1.auctions[i].damaged != return2.auctions[i].damaged) {
      return false;
    }
  }

  return true;
}

@Component({
  selector: 'return-dialog',
  templateUrl: 'return-dialog.html',
})
export class ReturnDialog {
  constructor() {
  }
}

@Component({
  selector: 'return-leave-dialog',
  templateUrl: 'return-leave-dialog.html',
})
export class ReturnLeaveDialog {
  constructor() {
  }
}
