import { Injectable } from '@angular/core';
import {
  CanActivate, 
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';

import { Observable } from 'rxjs/Observable';

import { AuthService } from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(): Observable<boolean> {
    console.log('AuthGuard#canActivate called');
    return this.authService.isLoggedIn();
  }
}
