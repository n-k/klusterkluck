import { Component, OnInit } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';

import { AuthService } from '../services/auth.service';

@Component({
    template: `
        <h4>LOGIN</h4>
        <p>Email: <input [(ngModel)]="username"/></p>
        <p>Password: <input type="password" [(ngModel)]="password"/></p>
        <button (click)="login()">login</button>
        <a routerLink="/register">Register</a>
    `
})
export class LoginComponent implements OnInit {

    username: string = '';
    password: string = '';

    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {
        console.log('initing login component');
        this.authService.isLoggedIn()
            .map(loggedIn => {
                if (loggedIn) {
                    console.log('logged in already');
                    // this.authService.getToken().map(x => console.log(x));
                    this.router.navigate(['/functions']);
                }
            });
    }

    login() {
        this.authService.login(this.username, this.password)
            .subscribe(
                _ => this.router.navigate(['/functions']),
                err => console.log(err));
    }
}
