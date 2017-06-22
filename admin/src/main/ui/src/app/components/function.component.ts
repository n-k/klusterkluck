import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Http} from "@angular/http";

import { AuthService } from '../services/auth.service';
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
              private http: Http,
              private auth: AuthService,) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      this.init();
    });
  }

  private init() {
    this.auth.getHttpOptions().subscribe(options => {
      this.fns.get(this.id, options)
        .subscribe(fn => {
          this.function = fn;
        });
      this.fns.getService(this.id, options)
        .subscribe(s => {
          this.service = s;
        });
      this.fns.getVersions(this.id, options)
        .subscribe(versions => {
          this.versions = versions;
        });
    });
  }

  private setVersion(versionId) {
    this.auth.getHttpOptions().subscribe(options => {
      this.fns.setVersion(this.id, versionId, options)
        .subscribe(x => {
          this.init();
        });
    });
  }

  private isCurrentVersion(versionId, index, functionCommitId) {
    return (functionCommitId == versionId) || (!functionCommitId && index == 0)
  }

  private address() {
    return this.service.spec.clusterIP;
  }

  private run(addr, payload) {
    this.auth.getHttpOptions().subscribe(options => {
      this.fns.proxy(this.id, this.payload || ' ', options)
        .subscribe(proxyResponse => {
          this.output = JSON.stringify(proxyResponse);
        });
    });
  }

}
