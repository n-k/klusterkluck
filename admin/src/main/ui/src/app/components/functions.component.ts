import {Component, OnInit} from "@angular/core";

import {FunctionsApi, KFFunction, AuthApi, UserResponse, UserNamespace} from "../../client";

@Component({
  selector: 'app-functions',
  template: `
    <div>
        <div *ngIf="ns">
          <p>
            <a href="//cloud9.{{ns.name}}.kube.local" target="_blank">
                Open Cloud9 IDE <span class="glyphicon glyphicon-share"></span>
            </a>
          </p>
          <p>
            <a href="//gogs.{{ns.name}}.kube.local" target="_blank">
                Open Git Dashboard <span class="glyphicon glyphicon-share"></span>
            </a>
          </p>
          <p>Git username: {{ns.gitUser}}</p>
          <p>Git password: {{ns.gitPassword}}</p>
        </div>
        <hr/>
        <h4>Functions:</h4>
        <div *ngIf="!functions.length">No functions. Create one by clicking the link below.</div>
        <table style="width: 100%" *ngIf="functions.length">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>External URL</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr *ngFor="let f of functions">
                    <td><a [routerLink]="'/functions/' + f.id">{{f.displayName}}</a></td>
                    <td><a href="//{{f.ingressUrl}}" target="_blank">{{f.ingressUrl}}</a></td>
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
  user: UserResponse = null;
  ns: UserNamespace = null;

  constructor(private fns: FunctionsApi,
              private authApi: AuthApi,) {
  }

  ngOnInit() {
    this.fetch();
    this.authApi.whoami().subscribe(user => {
      this.user = user;
      this.ns = user.user.namespaces[0];
    });
  }

  private fetch() {
    this.fns.list()
      .subscribe(l => {
        this.functions = l;
      });
  }

  deleteFn(id) {
    this.fns._delete(id)
      .subscribe(
        x => this.fetch(),
        error => alert(error.toString()))
  }
}
