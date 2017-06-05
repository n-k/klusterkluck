import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Headers, Http, RequestMethod, RequestOptions, RequestOptionsArgs} from "@angular/http";

import {FunctionsApi, ClusterInfoApi, KFFunction, Service, Version} from "../../client";

import {Node as _Node} from "../../client";

@Component({
  selector: 'app-function',
  templateUrl: 'function.component.html',
  styles: []
})
export class FunctionComponent implements OnInit {

  private id: string = '';
  private function: KFFunction = null;
  private nodes: _Node[] = [];
  private service: Service = null;
  private versions: Version[] = [];

  private payload: string = ' ';
  private output: string = '';

  constructor(private route: ActivatedRoute,
              private fns: FunctionsApi,
              private cluster: ClusterInfoApi,
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
    this.cluster.listNodes()
      .subscribe(nodes => {
        this.nodes = nodes;
        this.fns.getService(this.id)
          .subscribe(s => {
            this.service = s;
          });
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
    // we know there will be only one port
    const port = this.service.spec.ports[0];
    if (port.nodePort) {
      return this.nodes[0].status.addresses[0].address + ":" + port.nodePort;
    } else {
      return this.service.spec.clusterIP;
    }
  }

  private run(addr, payload) {
    let headers = new Headers();
    headers.set('Content-Type', 'text/plain');
    let requestOptions: RequestOptionsArgs = new RequestOptions({
      method: RequestMethod.Post,
      headers: headers,
      body: payload || ' ', //empty input will cause a 406 error
    });
    this.http.request(`http://${addr}`, requestOptions)
      .subscribe(x => {
        if (x.status == 200) {
          this.output = `Success: ${x.text()}`;
        } else {
          this.output = `Error: ${x.status} - ${x.statusText}: ${x.text()}`;
        }
      });
  }

}
