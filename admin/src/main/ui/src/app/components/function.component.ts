import {Component, OnInit} from '@angular/core';
import {ActivatedRoute,} from "@angular/router";
import {
  Http,
  RequestOptionsArgs,
  RequestOptions,
  RequestMethod,
  Headers,
} from '@angular/http';

import {FunctionsApi, KFFunction, Service,} from '../../client';

@Component({
  selector: 'app-function',
  template: `
    <div *ngIf="function">
        <p>Function {{function.name}}</p>
        <p>Gogs link: 
            <a href="{{function.gitUrl.replace('.git', '')}}" 
            target="_blank">{{function.gitUrl.replace('.git', '')}}</a>
        </p>
        <p>
        Note: if you are not logged in to gogs, the link will show a 404 - not found page.
        </p>
        <div *ngIf="service">
            ClusterIP: {{service.spec.clusterIP}}
            <div>
              <textarea [(ngModel)]="payload" class="form-control"></textarea>
              <button (click)="run(service.spec.clusterIP, payload)">Run</button>
            </div>
            <p>Output: {{output}}</p>
        </div>
        <div *ngIf="!service">Loading k8s service definition</div>
    </div>
    <div *ngIf="!function">Loading...</div>
  `,
  styles: []
})
export class FunctionComponent implements OnInit {

  private id: string = '';
  private function: KFFunction = null;
  private service: Service = null;

  private payload: string = '';
  private output: string = '';

  constructor(
    private route: ActivatedRoute,
    private fns: FunctionsApi,
    private http: Http,)
  {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      this.fns.get(this.id)
        .subscribe(fn => {
          this.function = fn;
        });
      this.fns.getService(this.id)
        .subscribe(s => {
          this.service = s;
        });
    });
  }

  private run(addr, payload) {
    let headers = new Headers();
    headers.set('Content-Type', 'application/json');
    let requestOptions: RequestOptionsArgs = new RequestOptions({
      method: RequestMethod.Post,
      headers: headers,
      body: JSON.stringify({payload: payload}),
    });
    this.http.request(`http://${addr}`, requestOptions)
      .subscribe(x => {
        this.output = JSON.stringify(x);
      });
  }
}
