import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {AppComponent} from "./app.component";
import {FunctionsComponent} from "./components/functions.component";
import {FunctionComponent} from "./components/function.component";
import { CreateFunctionComponent } from './components/create-function.component';

import {APIS} from "../client";
import {BASE_PATH} from "../client/variables";
declare let basePath: any;

import {RouterModule, Routes} from "@angular/router";
const routes: Routes = [
  {path: 'functions', component: FunctionsComponent},
  {path: 'functions/newfn', component: CreateFunctionComponent},
  {path: 'function/:id', component: FunctionComponent},
  {path: '**', redirectTo: 'functions',}
];

@NgModule({
  declarations: [
    AppComponent,
    FunctionsComponent,
    FunctionComponent,
    CreateFunctionComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(routes, {useHash: true}),
  ],
  providers: [
    ...APIS,
    {provide: BASE_PATH, useValue: '.'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
