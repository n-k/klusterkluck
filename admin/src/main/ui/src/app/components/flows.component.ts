import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-flows',
  template: `
    <p>
      flows Works!
    </p>
    <a [routerLink]="'/flows/sample'">Sample flow</a>
  `,
  styles: []
})
export class FlowsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
