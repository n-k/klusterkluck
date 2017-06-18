import {Component, OnInit} from "@angular/core";
import {Flow, FlowsApi} from "../../client";
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-flows',
  template: `
    <h4>Flows</h4>
    <hr/>
    <div>
        <table style="width: 90%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr *ngFor="let f of flows">
                    <td><a [routerLink]="f.id">{{f.displayName}}</a></td>
                    <td>
                        <button (click)="deleteFlow(f.id)">
                            <span class="glyphicon glyphicon-trash"></span>
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <hr/>
    <div>
        <p>New Flow name: <input [(ngModel)]="name" /></p>
        <p><button (click)="create()" class="btn-primary">Create</button></p>
    </div>
  `,
  styles: []
})
export class FlowsComponent implements OnInit {

  flows: Flow[] = [];

  name: string = '';

  constructor(private service: FlowsApi, private auth: AuthService,) {
  }

  ngOnInit() {
    this.fetch();
  }

  private fetch() {
      this.auth.getHttpOptions().subscribe(options => {
        this.service.list(options)
            .subscribe(
                fs => this.flows = fs,
                error => {
                    alert(error.toString());
                }
            )
      });
  }

  create() {
    const newFlowName = '' + this.name;
    this.name = '';
    this.auth.getHttpOptions().subscribe(options => {
        this.service.create({name: newFlowName}, options)
            .subscribe(x => this.fetch(), error => {alert(error.toString())});
    });
  }

  deleteFlow(id) {
      this.auth.getHttpOptions().subscribe(options => {
        this.service._delete(id, options)
            .subscribe(x => this.fetch(), error => {alert(error.toString())});
      });
  }
}
