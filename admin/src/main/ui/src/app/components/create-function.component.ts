import {Component, OnInit, ViewChild} from "@angular/core";
import {Router} from '@angular/router';

import { AuthService } from '../services/auth.service';
import {AlertService} from '../services/alert.service';
import {FunctionsApi, CreateFunctionRequest} from "../../client";

@Component({
  selector: 'app-create-function',
  templateUrl: 'create-function.component.html',
  styles: []
})
export class CreateFunctionComponent implements OnInit {

  name: string = '';
  serviceType: string = 'ClusterIP';
  ingress: boolean = false;
  host: string = '';
  path: string = '';

  constructor(
    private fns: FunctionsApi,
    private router: Router,
    private auth: AuthService,
    private alertService: AlertService,
  ) {}

  ngOnInit() {
  }

  create() {
    const cfr: CreateFunctionRequest = {
      name: '' + this.name,
      serviceType: CreateFunctionRequest.ServiceTypeEnum[this.serviceType],
      ingress: this.ingress,
      host: this.host,
      path: this.path,
    };
    this.name = '';
    this.auth.getHttpOptions().subscribe(options => {
      this.alertService.doInModal(
        'Creating function',
        () => this.fns.create(cfr, options))
        .subscribe(f => {
          this.router.navigate(['/functions'])
        }, (err: Error) => {
          this.alertService.showAlert('Error while creating function', err.toString());
        });
    });
  }
}
