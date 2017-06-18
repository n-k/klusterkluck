import { Component, OnInit } from "@angular/core";
import { Headers } from '@angular/http';

import { AuthService } from '../services/auth.service';
import { CreateFunctionRequest, FunctionsApi, KFFunction } from "../../client";

@Component({
  selector: 'app-functions',
  template: `
    <h4>Functions:</h4>
    <div>
        <p>
        Note: if you are not logged in to gogs, the link will show a 404 - not found page.
        </p>
        <table style="width: 100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Git Link</th>
                    <th>Namespace</th>
                    <th>Service</th>
                    <th>Deployment</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr *ngFor="let f of functions">
                    <td><a [routerLink]="'/functions/' + f.id">{{f.name}}</a></td>
                    <td>
                        <a href="{{f.gitUrl.replace('.git', '')}}" target="_blank">
                            <span class="glyphicon glyphicon-share"></span>
                        </a>
                    </td>
                    <td>{{f.namespace}}</td>
                    <td>{{f.service}}</td>
                    <td>{{f.deployment}}</td>
                    <td>
                      <button (click)="deleteFn(f.id)" class="btn btn-sm">
                        <span class="glyphicon glyphicon-trash"></span>
                      </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <hr/>
    <div>
        <a [routerLink]="'newfn'">Create a new function</a>
    </div>
  `,
  styles: []
})
export class FunctionsComponent implements OnInit {

  functions: KFFunction[] = [];

  constructor(private fns: FunctionsApi, private auth: AuthService, ) {
  }

  ngOnInit() {
    this.fetch();
  }

  private fetch() {
    this.auth.getHttpOptions()
      .subscribe(options => {
        this.fns
          .list(options)
          .subscribe(l => {
            this.functions = l;
          });
      });
  }

  deleteFn(id) {
    this.auth.getHttpOptions()
      .subscribe(options => {
        this.fns._delete(id, options)
          .subscribe(
            x => this.fetch(),
            error => alert(error.toString()))
      });
  }
}
