import {
  Component,
  OnInit,
  ViewChild,
  ComponentFactory,
  ViewContainerRef,
  ComponentRef,
} from '@angular/core';
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

  alertObserver = null;
  alertObservable = null;

  @ViewChild('modal') modal;
  @ViewChild('errorModal') errorModal;

  @ViewChild("compModalContainer", { read: ViewContainerRef }) compModalContainer;
  @ViewChild('compModal') compModal;

  constructor(
    private alertService: AlertService,
  ) { }

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
    this.alertObservable = Observable.create(observer => {
      this.alertObservable = observer;
    });
    return this.alertObservable;
  }

  closeAlert() {
    this.errorModal.close();
    this.alertObserver.next();
    this.alertObservable = null;
    this.alertObserver = null;
  }

  openComponent(factory: ComponentFactory<any>): Observable<any> {
    this.compModalContainer.clear();
    this.compModal.open();
    const componentRef: ComponentRef<any> = this.compModalContainer.createComponent(factory);
    var obs;
    const wrapper = Observable.create(observer => {obs = observer;});
    componentRef.instance.modalControls = {
      success: (result: any) => {
        this.compModal.close();
        componentRef.destroy();
        obs.next(result);
      },
      failure: (err: any) => {
        this.compModal.close();
        componentRef.destroy();
        obs.error(err);
      },
      abort: () => {
        this.compModal.close();
        componentRef.destroy();
      }
    };
    return wrapper;
  }
}
