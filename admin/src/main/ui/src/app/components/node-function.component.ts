import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {FunctionsApi, KFFunction} from "../../client";

@Component({
  selector: 'app-node-function',
  template: `
    <p>Function:</p>
    <p>
        <select class="form-control" [(ngModel)]="value.functionId">
            <option *ngFor="let f of getFunctions()" [value]="f[0]">{{f[1]}} ({{f[0]}})</option>
        </select>
    </p>
  `,
  styles: []
})
export class NodeFunctionComponent implements OnInit {
  @Input() value: any = {};
  @Output() valueChange = new EventEmitter<any>();

  private functions: KFFunction[] = [];

  constructor(private fns: FunctionsApi,) {
  }

  ngOnInit() {
    this.fns.list()
      .subscribe(
        functions => this.functions = functions,
        error => alert(error.toString()))
  }

  getFunctions() {
    let fs = this.functions.map(f => [f.id, f.name]);
    if (!fs.find(f => f[0] == this.value.functionId)) {
      fs.push([this.value.functionId, 'Unknown']);
    }
    return fs;
  }
}
