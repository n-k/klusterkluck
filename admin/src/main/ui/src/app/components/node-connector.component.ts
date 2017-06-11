import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import {ConnectorsApi, Connector} from '../../client';

@Component({
  selector: 'app-node-connector',
  template: `
    <p>Connector:</p>
    <p>
        <select class="form-control" [(ngModel)]="value.connectorId">
            <option *ngFor="let f of getConnectors()" [value]="f[1]">{{f[1]}} ({{f[0]}})</option>
        </select>
    </p>
  `,
  styles: []
})
export class NodeConnectorComponent implements OnInit {

  @Input() value: any;
  @Output() valueChange = new EventEmitter<any>();

  private connectors: Connector[] = [];

  constructor(private conns: ConnectorsApi,) { }

  ngOnInit() {
    this.conns.list()
      .subscribe(
        cs => this.connectors = cs,
        error => alert(error.toString()))
  }

  getConnectors() {
    let fs = this.connectors.map(f => [f.id, f.displayName]);
    if (!fs.find(f => f[1] == this.value.connectorId)) {
      fs.push(['Unknown', this.value.connectorId]);
    }
    return fs;
  }
}
