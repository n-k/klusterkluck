import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Http} from "@angular/http";

import {FunctionsApi, KFFunction, Service, Version} from "../../client";

@Component({
  selector: 'app-function',
  templateUrl: 'function.component.html',
  styles: []
})
export class FunctionComponent implements OnInit {

  id: string = '';
  function: KFFunction = null;
  service: Service = null;
  versions: Version[] = [];

  payload: string = ' ';
  output: string = '';

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

  private address() {
    return this.service.spec.clusterIP;
  }

  private run(addr, payload) {
    this.fns.proxy(this.id, this.payload || ' ')
      .subscribe(proxyResponse => {
        this.output = JSON.stringify(proxyResponse);
      });
  }

}
