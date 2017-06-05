import {Component, OnInit} from "@angular/core";

import {CreateFunctionRequest, FunctionsApi, KFFunction} from "../../client";

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
                    <th>Git URL</th>
                    <th>Namespace</th>
                    <th>Service Name</th>
                    <th>Deployment Name</th>
                </tr>
            </thead>
            <tbody>
                <tr *ngFor="let f of functions">
                    <td><a [routerLink]="'/function/' + f.id">{{f.name}}</a></td>
                    <td>
                        {{f.gitUrl}}
                        <a href="{{f.gitUrl.replace('.git', '')}}" target="_blank">
                            <span class="glyphicon glyphicon-share"></span>
                        </a>
                    </td>
                    <td>{{f.namespace}}</td>
                    <td>{{f.service}}</td>
                    <td>{{f.deployment}}</td>
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

  private functions: KFFunction[] = [];

  constructor(private fns: FunctionsApi,) {
  }

  ngOnInit() {
    this.fetch();
  }

  private fetch() {
    this.fns.list()
      .subscribe(l => {
        this.functions = l;
      });
  }

}
