import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {ModalModule} from "ngx-modal";

import {AppComponent} from "./app.component";
import {FunctionsComponent} from "./components/functions.component";
import {FunctionComponent} from "./components/function.component";
import {CreateFunctionComponent} from './components/create-function.component';
import {FlowsComponent} from './components/flows.component';
import {ConnectorsComponent} from './components/connectors.component';
import {FlowComponent} from './components/flow.component';
import { ConnectorComponent } from './components/connector.component';
import { NodeConnectorComponent } from './components/node-connector.component';
import { NodeFunctionComponent } from './components/node-function.component';
import { LoginComponent } from './components/login.component';

import {AuthService} from './services/auth.service';
import {AuthGuard} from './services/auth-guard.service';

import {APIS} from "../client";
import {BASE_PATH} from "../client/variables";
declare let basePath: any;

import {RouterModule, Routes} from "@angular/router";
const routes: Routes = [
  {path: 'functions', canActivate: [AuthGuard], component: FunctionsComponent},
  {path: 'functions/newfn', canActivate: [AuthGuard], component: CreateFunctionComponent},
  {path: 'functions/:id',canActivate: [AuthGuard],  component: FunctionComponent},
  {path: 'flows', canActivate: [AuthGuard], component: FlowsComponent},
  {path: 'flows/:id', canActivate: [AuthGuard], component: FlowComponent},
  {path: 'connectors', canActivate: [AuthGuard], component: ConnectorsComponent},
  {path: 'login', component: LoginComponent},
  {path: '**', redirectTo: 'login',}
];

@NgModule({
  declarations: [
    AppComponent,
    FunctionsComponent,
    FunctionComponent,
    CreateFunctionComponent,
    FlowsComponent,
    ConnectorsComponent,
    FlowComponent,
    ConnectorComponent,
    NodeConnectorComponent,
    NodeFunctionComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(routes, {useHash: true}),
    ModalModule,
  ],
  providers: [
    ...APIS,
    AuthGuard,
    AuthService,
    {provide: BASE_PATH, useValue: '.'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
