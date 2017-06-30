import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";

import {AuthApi, FunctionsApi, KFFunction, Service, Version, UserResponse, UserNamespace} from "../../client";

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

  user: UserResponse = null;
  ns: UserNamespace = null;

  payload: string = ' ';
  output: string = '';

  constructor(private route: ActivatedRoute,
              private fns: FunctionsApi,
              private authApi: AuthApi,) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      this.init();
    });
    this.authApi.whoami().subscribe(user => {
      this.user = user;
      this.ns = user.user.namespaces[0];
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
      });
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
