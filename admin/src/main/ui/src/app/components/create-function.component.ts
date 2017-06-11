import {Component, OnInit, ViewChild} from "@angular/core";
import {Router} from '@angular/router';
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

  @ViewChild('modal') modal;
  @ViewChild('errorModal') errorModal;

  error: string = '';

  constructor(
    private fns: FunctionsApi,
    private router: Router,
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
    this.modal.open();
    this.fns.create(cfr)
      .subscribe(f => {
        this.modal.close();
        this.router.navigate(['/functions'])
      }, (err: Error) => {
        this.modal.close();
        this.errorModal.open();
        this.error = JSON.stringify(err);
      });
  }
}
