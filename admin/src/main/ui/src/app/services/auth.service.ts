import { Injectable } from '@angular/core';
import { Headers } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';

import {AuthApi} from '../../client';

@Injectable()
export class AuthService {
  accessToken: string = '';
  refreshtoken: string = '';
  expiresIn: number = 0;

  constructor(private auth: AuthApi,) {}

  // store the URL so we can redirect after logging in
  redirectUrl: string;

  login(username: string, password: string): Observable<boolean> {
    return this.auth.login({username: username, password: password})
      .map(x => {
        this.accessToken = x['access_token'];
        this.refreshtoken = x['refresh_token'];
        this.expiresIn = +x['expires_in'];
        return true;
      });
  }

  getToken(): Observable<string> {
    // TODO: check token expiration, attempt refresh etc.
    return Observable.of(this.accessToken);
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
    this.accessToken = '';
    this.refreshtoken = '';
    this.expiresIn = 0;
  }
}
