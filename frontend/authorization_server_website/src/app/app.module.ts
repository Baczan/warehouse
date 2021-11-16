import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CookieModule} from 'ngx-cookie';
import {AuthComponent} from './components/auth/auth.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AuthLoginComponent} from './components/auth/auth-login/auth-login.component';
import {InputComponent} from './components/input/input.component';
import {MatButtonModule} from '@angular/material/button';
import {NoAuthGuard} from './guards/no-auth.guard';
import {AuthRegisterComponent} from './components/auth/auth-register/auth-register.component';
import {MainComponent} from './components/main/main.component';
import {UserComponent} from './components/main/user/user.component';
import {AuthGuard} from './guards/auth.guard';
import {AuthActivateComponent} from './components/auth/auth-activate/auth-activate.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {AuthPasswordChangeComponent} from './components/auth/auth-password-change/auth-password-change.component';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {MatSidenavModule} from '@angular/material/sidenav';
import {LinkComponent} from './components/random/link/link.component';
import {MatInputModule} from '@angular/material/input';
import {NgxMatIntlTelInputModule} from 'ngx-mat-intl-tel-input';
import {WrapperComponent} from './components/main/wrapper/wrapper.component';
import {NewAppComponent} from './components/main/apps/new-app/new-app.component';
import {AppsComponent} from './components/main/apps/apps/apps.component';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {RoleAssignComponent} from './components/main/roles/role-assign/role-assign.component';
import {RoleAddComponent} from './components/main/roles/role-add/role-add.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {ListItemComponent} from './components/main/roles/role-assign/list-item/list-item.component';
import {CustomDatePipe} from './pipes/custom-date.pipe';
import {ListHeaderComponent} from './components/main/roles/role-assign/list-header/list-header.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {AuthInterceptorService} from './interceptors/auth-interceptor.service';
import {RoleAddItemComponent} from './components/main/roles/role-add/role-add-item/role-add-item.component';
import {RoleAddItemTemplateComponent} from './components/main/roles/role-add/role-add-item/role-add-item-template/role-add-item-template.component';
import {RoleAssignUserComponent} from './components/main/roles/role-assign/role-assign-user/role-assign-user.component';
import {MatCheckboxModule} from '@angular/material/checkbox';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'main/user'},
  {
    path: 'auth', component: AuthComponent, children: [
      {path: 'login', component: AuthLoginComponent, canActivate: [NoAuthGuard]},
      {path: 'register', component: AuthRegisterComponent, canActivate: [NoAuthGuard]},
      {path: 'activate', component: AuthActivateComponent},
      {path: 'password_change', component: AuthPasswordChangeComponent}
    ]
  },
  {
    path: 'main', component: MainComponent, canActivate: [AuthGuard], children: [
      {path: 'user', component: UserComponent},
      {path: 'apps/new', component: NewAppComponent},
      {path: 'apps/:id', component: AppsComponent},
      {path: 'roles/assign', component: RoleAssignComponent},
      {path: 'roles/add', component: RoleAddComponent},
      {path: 'roles/assign/:id/:email', component: RoleAssignUserComponent}
    ]
  }
];

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    AuthLoginComponent,
    InputComponent,
    AuthRegisterComponent,
    MainComponent,
    UserComponent,
    AuthActivateComponent,
    AuthPasswordChangeComponent,
    LinkComponent,
    WrapperComponent,
    NewAppComponent,
    AppsComponent,
    RoleAssignComponent,
    RoleAddComponent,
    ListItemComponent,
    CustomDatePipe,
    ListHeaderComponent,
    RoleAddItemComponent,
    RoleAddItemTemplateComponent,
    RoleAssignUserComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    FormsModule,
    HttpClientModule,
    CookieModule.forChild(),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatIconModule,
    MatSidenavModule,
    MatInputModule,
    NgxMatIntlTelInputModule,
    MatSlideToggleModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatCheckboxModule
  ],
  providers: [NoAuthGuard, AuthGuard, {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
