import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';

import { AlertService } from '../services/alert.service';
import {ModalsProvider} from "../services/modals-provider";

@Component({
  selector: 'alert',
  templateUrl: 'alert.component.html'
})
export class AlertComponent implements ModalsProvider, OnInit {
  title: any;
  error: any;

  @ViewChild('modal') modal;
  @ViewChild('errorModal') errorModal;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.register(this);
  }

  doInModal(title: string, action: () => Observable<any>): Observable<any> {
    this.title = title;
    this.modal.open();
    var obs;
    const wrapper = Observable.create(observer => {obs = observer;});
    action().subscribe(
      result => {
        this.modal.close();
        obs.next(result);
      },
      error => {
        this.modal.close();
        obs.error(error);
      },
      () => {
        obs.complete();
      });
    return wrapper;
  }

  showAlert(title: string, error: string) {
    this.title = title;
    this.error = error;
    this.errorModal.open();
  }
}
