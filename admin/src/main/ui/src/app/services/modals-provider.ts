import { ComponentFactory } from '@angular/core';
import { Observable } from 'rxjs/Observable';

export interface ModalsProvider {
  doInModal(title: string, action: () => Observable<any>): Observable<any>;

  showAlert(title: string, error: string);

  openComponent(factory: ComponentFactory<any>): Observable<any>;
}
