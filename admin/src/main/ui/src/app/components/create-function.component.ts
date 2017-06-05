import {Component, OnInit} from "@angular/core";
import {Router} from '@angular/router';
import {FunctionsApi, CreateFunctionRequest} from "../../client";

@Component({
  selector: 'app-create-function',
  templateUrl: 'create-function.component.html',
  styles: []
})
export class CreateFunctionComponent implements OnInit {

  private name: string = '';
  private serviceType: string = 'ClusterIP';
  private ingress: boolean = false;
  private host: string = '';
  private path: string = '';

  constructor(
    private fns: FunctionsApi,
    private router: Router,
  ) {
  }

  ngOnInit() {
  }

  private create() {
    const cfr: CreateFunctionRequest = {
      name: '' + this.name,
      serviceType: CreateFunctionRequest.ServiceTypeEnum[this.serviceType],
      ingress: this.ingress,
      host: this.host,
      path: this.path,
    };
    this.name = '';
    this.fns.create(cfr)
      .subscribe(f => {
        this.router.navigate(['/functions'])
      })
  }
}
