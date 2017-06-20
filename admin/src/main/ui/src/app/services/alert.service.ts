import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import {ModalsProvider} from "./modals-provider";

@Injectable()
export class AlertService implements ModalsProvider {
  private modalsProvider: ModalsProvider;

  constructor(private router: Router) {}

  register(mp: ModalsProvider) {
    this.modalsProvider = mp;
    console.log(mp);
  }

  doInModal(title: string, action: () => Observable<any>): Observable<any> {
    return this.modalsProvider.doInModal(title, action);
  }

  showAlert(title: string, error: string) {
    this.modalsProvider.showAlert(title, error);
  }

}
