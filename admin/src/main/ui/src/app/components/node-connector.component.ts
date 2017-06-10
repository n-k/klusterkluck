import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-node-connector',
  template: `
    <p>node-connector Works!</p>
  `,
  styles: []
})
export class NodeConnectorComponent implements OnInit {

  @Input() value: any;
  @Output() valueChange = new EventEmitter<any>();

  constructor() { }

  ngOnInit() {
  }

}
