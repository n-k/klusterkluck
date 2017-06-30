import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
} from '@angular/router';

import { Observable } from 'rxjs/Observable';

import { AuthService } from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(): Observable<boolean> {
    return Observable.of(true);
  }
}
