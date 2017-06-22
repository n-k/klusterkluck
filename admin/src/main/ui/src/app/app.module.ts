import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
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
import { LandingComponent } from './components/landing.component';
import { RegisterComponent } from './components/register.component';
import { AlertComponent } from './components/alert.component';
import { HeaderComponent } from './components/header.component';

import {AlertService} from './services/alert.service';
import {AuthGuard} from './services/auth-guard.service';

import {APIS} from "../client";
import {BASE_PATH} from "../client/variables";
declare let basePath: any;

import {RouterModule, Routes} from "@angular/router";
const routes: Routes = [
  {path: 'functions', component: FunctionsComponent},
  {path: 'functions/newfn', component: CreateFunctionComponent},
  {path: 'functions/:id', component: FunctionComponent},
  {path: 'flows', component: FlowsComponent},
  {path: 'flows/:id', component: FlowComponent},
  {path: 'connectors', component: ConnectorsComponent},
  {path: '**', redirectTo: 'functions',},
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
    LandingComponent,
    RegisterComponent,
    AlertComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    RouterModule.forRoot(routes, {useHash: true}),
    ModalModule,
  ],
  providers: [
    ...APIS,
    AuthGuard,
    AlertService,
    {provide: BASE_PATH, useValue: '.'}
  ],
  entryComponents: [
    AppComponent,
    RegisterComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
