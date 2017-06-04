import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Headers, Http, RequestMethod, RequestOptions, RequestOptionsArgs} from "@angular/http";

import {FunctionsApi, KFFunction, Service, Version} from "../../client";

@Component({
  selector: 'app-function',
  template: `
    <div *ngIf="function">
        <h3>Function {{function.name}}</h3>
        <p>Gogs link: 
            <a href="{{function.gitUrl.replace('.git', '')}}" 
            target="_blank">{{function.gitUrl.replace('.git', '')}}</a>
        </p>
        <p>
        Note: if you are not logged in to gogs, the link will show a 404 - not found page.
        </p>
        <hr/>
        <div>
            <h4>Versions</h4>
            <p *ngFor="let v of versions; let idx = index">
                {{v.id.substring(0, 6)}} ({{v.message}}) by {{v.author}} @ {{v.timestamp*1000 | date}}
                <span *ngIf="isCurrentVersion(v.id, idx, function.commitId)" 
                    class="glyphicon glyphicon-ok"
                    style="color: green"></span>
                <span *ngIf="!isCurrentVersion(v.id, idx, function.commitId)"
                    class="glyphicon glyphicon-play-circle"
                    style="color: blue"
                    (click)="setVersion(v.id)"></span>
            </p>
        </div>
        <hr/>
        <div *ngIf="service">
            <h4>Try it</h4>
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
  private versions: Version[] = [];

  private payload: string = '';
  private output: string = '';

  constructor(private route: ActivatedRoute,
              private fns: FunctionsApi,
              private http: Http,) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      this.init();
    });
  }

  private init() {
    this.fns.get(this.id)
      .subscribe(fn => {
        this.function = fn;
      });
    this.fns.getService(this.id)
      .subscribe(s => {
        this.service = s;
      });
    this.fns.getVersions(this.id)
      .subscribe(versions => {
        this.versions = versions;
      });
  }

  private setVersion(versionId) {
    this.fns.setVersion(this.id, versionId)
      .subscribe(x => {
        this.init();
      })
  }

  private isCurrentVersion(versionId, index, functionCommitId) {
    return (functionCommitId == versionId) || (!functionCommitId && index == 0)
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
