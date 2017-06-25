import { Injectable } from '@angular/core';
import { Headers } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';

import {AuthApi, AccesstokenResponseWrapper} from '../../client';

@Injectable()
export class AuthService {
  accessTokenResponse: AccesstokenResponseWrapper;

  private newtokenCallbacks: {(token: string): void;}[] = [];
  constructor(private auth: AuthApi,) {}

  public addTokenCallback(cb: {(token: string): void;}) {
    this.newtokenCallbacks.push(cb);
  }

  login(username: string, password: string): Observable<boolean> {
    return this.auth.login({username: username, password: password})
      .map(x => {
        this.accessTokenResponse = x;
        this.newtokenCallbacks.forEach(cb => cb(x.token));
        this.refreshLoop();
        return true;
      });
  }

  private refreshLoop() {
    if (1 == 1) {return;}
    window.setTimeout(
      this.refresh.bind(this),
      (this.accessTokenResponse.expiresIn - 10) * 1000);
  }

  refresh() {
    const headers: Headers = new Headers();
    headers.append("Authorization", "Bearer " + this.accessTokenResponse.token);
    headers.append("Content-Type", "application/json");
    const options =  {headers: headers};
    this.auth.refresh({refreshToken: this.accessTokenResponse.refreshToken}, options)
      .subscribe(x => {
        this.accessTokenResponse = x;
        this.newtokenCallbacks.forEach(cb => cb(x.token));
        this.refreshLoop();
      });
  }

  getToken(): Observable<string> {
    // TODO: check token expiration, attempt refresh etc.
    return Observable.of(this.accessTokenResponse? this.accessTokenResponse.token: null);
  }

  getHttpOptions(): Observable<any> {
    return this.getToken().map(token => {
        const headers: Headers = new Headers();
        headers.append("Authorization", "Bearer " + token);
        headers.append("Content-Type", "application/json");
        return {headers: headers};
    });
  }

  isLoggedIn(): Observable<boolean> {
    return this.getToken().map(x => !!x);
  }

  logout(): void {
    this.accessTokenResponse = null;
  }
}
