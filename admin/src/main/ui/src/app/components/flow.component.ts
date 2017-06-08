import { Component, OnInit, ViewChild } from '@angular/core';

declare var jsPlumb:any;

@Component({
  selector: 'app-flow',
  template: `
    <p>
      flow Works!
    </p>
    <div #test>Test</div>
  `,
  styles: []
})
export class FlowComponent implements OnInit {

  @ViewChild('test') testEl;

  constructor() { }

  ngOnInit() {
  }


  ngAfterViewInit() {
    let me = this;
    jsPlumb.ready(function() {
      jsPlumb.addEndpoint(me.testEl.nativeElement);
    });
  }

}
