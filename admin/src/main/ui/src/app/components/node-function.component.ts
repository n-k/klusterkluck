import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-node-function',
  template: `
    <p>node-function Works!</p>
  `,
  styles: []
})
export class NodeFunctionComponent implements OnInit {
  @Input() value: any;
  @Output() valueChange = new EventEmitter<any>();

  constructor() { }

  ngOnInit() {
  }

}
