import {Component, OnInit} from "@angular/core";
import {Flow, FlowsApi} from "../../client";

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

  constructor(private service: FlowsApi,) {
  }

  ngOnInit() {
    this.fetch();
  }

  private fetch() {
    this.service.list()
      .subscribe(
        fs => this.flows = fs,
        error => {
          alert(error.toString());
        }
      )
  }

  create() {
    const newFlowName = '' + this.name;
    this.name = '';
    this.service.create({name: newFlowName})
      .subscribe(x => this.fetch(), error => {
        alert(error.toString())
      });
  }

  deleteFlow(id) {
    this.service._delete(id)
      .subscribe(x => this.fetch(), error => {
        alert(error.toString())
      });
  }
}
