import {BrowserModule, HammerModule} from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule, Routes} from '@angular/router';
import { MainComponent } from './components/main/main.component';
import {TokenInterceptorService} from './interceptors/token-interceptor.service';
import {AuthModule} from './modules/auth/auth.module';
import {CodeGuard} from './modules/auth/guards/code.guard';
import {CookieModule} from 'ngx-cookie';
import {AuthInterceptor} from './modules/auth/interceptors/auth-interceptor';
import { RedirectComponent } from './components/redirect/redirect.component';
import { ConnectComponent } from './components/main/connect/connect.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from "@angular/forms";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import { HrefComponent } from './components/main/href/href.component';
import {MatDividerModule} from "@angular/material/divider";
import {AuthenticationStatusGuard} from "./modules/auth/guards/authentication-status.guard";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { WrapperComponent } from './components/main/wrapper/wrapper.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { PartsComponent } from './components/main/parts/parts.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatPaginatorModule} from "@angular/material/paginator";
import { PartItemComponent } from './components/main/parts/part-item/part-item.component';
import { AddPartComponent } from './components/main/add-part/add-part.component';
import {PartDialog, UpdatePartComponent} from './components/main/parts/update-part/update-part.component';
import {MatDialogModule} from "@angular/material/dialog";
import { CarsComponent } from './components/main/cars/cars.component';
import {CarDialog, CarListItemComponent} from './components/main/cars/car-list-item/car-list-item.component';
import { LabelGenerationComponent } from './components/main/label-generation/label-generation.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatSelectModule} from "@angular/material/select";
import { LabelComponent } from './components/main/label/label.component';
import {SavedLabelDialog, SavedLabelsComponent} from './components/main/saved-labels/saved-labels.component';
import { ReturnsComponent } from './components/main/returns/returns.component';
import { ReturnListItemComponent } from './components/main/returns/return-list-item/return-list-item.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {
  ReturnAddComponent,
  ReturnDialog,
  ReturnLeaveDialog
} from './components/main/returns/return-add/return-add.component';
import {NgxBarcodeModule} from "ngx-barcode";
import { SyncComponent } from './components/main/sync/sync.component';
import {QRCodeModule} from "angularx-qrcode";
import { NewCarComponent } from './components/main/new-car/new-car.component';
import {
  NewCarDelete, NewCarLeaveDialog,
  NewCarSelectedComponent
} from './components/main/new-car/new-car-selected/new-car-selected.component';
import { NewCarListItemComponent } from './components/main/new-car/new-car-list-item/new-car-list-item.component';
import { NewCarImageUploadComponent } from './components/main/new-car/new-car-image-upload/new-car-image-upload.component';
import { NewCarImageComponent } from './components/main/new-car/new-car-image/new-car-image.component';
import { CarGalleryComponent } from './components/main/new-car/car-gallery/car-gallery.component';
import 'hammerjs';

const routes : Routes = [
  {path:"app",component:MainComponent,canActivate:[CodeGuard],children:[
      {path:"connect",component:ConnectComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"parts",component:PartsComponent},
      {path:"parts/:id",component:UpdatePartComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"addPart",component:AddPartComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"cars",component:CarsComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"label",component:LabelGenerationComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"savedLabel", redirectTo: 'savedLabel/', pathMatch: 'full',canActivate:[AuthenticationStatusGuard]},
      {path:"savedLabel/:id",component:SavedLabelsComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"returns",component:ReturnsComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"returns/add",component:ReturnAddComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"returns/:id",component:ReturnAddComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"sync",component:SyncComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"newCar/:id",component:NewCarSelectedComponent,canActivate:[AuthenticationStatusGuard]},
      {path:"newCar",component:NewCarComponent,canActivate:[AuthenticationStatusGuard]}

    ]},
  {path:"redirect/:service",component:RedirectComponent},
  { path: '**', redirectTo:"/app/label"  }
];

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    RedirectComponent,
    ConnectComponent,
    HrefComponent,
    WrapperComponent,
    PartsComponent,
    PartItemComponent,
    AddPartComponent,
    UpdatePartComponent,
    PartDialog,
    CarsComponent,
    CarListItemComponent,
    CarDialog,
    LabelGenerationComponent,
    LabelComponent,
    SavedLabelsComponent,
    SavedLabelDialog,
    ReturnsComponent,
    ReturnListItemComponent,
    ReturnAddComponent,
    ReturnDialog,
    ReturnLeaveDialog,
    SyncComponent,
    NewCarComponent,
    NewCarSelectedComponent,
    NewCarListItemComponent,
    NewCarImageUploadComponent,
    NewCarImageComponent,
    CarGalleryComponent,
    NewCarDelete,
    NewCarLeaveDialog
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes, {enableTracing: false}),
    AuthModule,
    CookieModule.forChild(),
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatDividerModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatCheckboxModule,
    NgxBarcodeModule,
    QRCodeModule,
    HammerModule
  ],
  providers: [CodeGuard,{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},AuthenticationStatusGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
