import {Component, OnInit, ComponentFactoryResolver} from "@angular/core";
import {AuthApi, User} from '../client';

import {RegisterComponent} from './components/register.component';
import {LoginComponent} from './components/login.component';

import {AlertService} from './services/alert.service';
import {HttpInterceptor} from "./services/http-interceptor";
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  template: `
    <div class="header">
      <span class="logo">k̳̼͉̭̙̰̺̬̑͒̄̌͘̕ͅl̞̂̏͌̀͢͝ú̖̗̗̥͊̀s̞̯̭̀̃͂͌͠͡t̵̗̺̪̰͎̺̯͊̎́͊̽͜͟͝͡ȩ̴͎̱̮͇̺̟̬̥͊͆̂͒̇ͅr̸̡̢͎̺̥͈̜͎͗́̿͂̀͞͞f̢̩̫͚̘̻̠̓̊u̟̘̰̯͒͑̂̔̽̕͞͞ck̔̽̕͞͞</span>
      <h3 style="display: inline">
        {{title}}
      </h3>
      <h4>{{user && user.email}}</h4>
      <app-header *ngIf="loggedIn && !userNotSetup"></app-header>
    </div>
    <alert></alert>
    <router-outlet *ngIf="loggedIn && !userNotSetup"></router-outlet>
    <div *ngIf="!loggedIn && !userNotSetup" class="landing">
        <div class="landing-link">
            <button (click)="startLogin()" class="landingBtn btn btn-primary">Login</button>
        </div>
        <div class="landing-link">
            <button (click)="startRegister()" class="landingBtn btn btn-primary">Register</button>
        </div>
    </div>
    <div *ngIf="userNotSetup">
      <p>Your account has not been setup yet, or you have logged in with an administrator account</p>
      <p>
        <a class="logout" href="./api/v1/auth/logout">Logout</a>
      </p>
    </div>
  `,
  styles: [`
    div.header {
      text-align: right;
      padding-right: 10px;
      height: 8em;
    }
    span.logo {
      float: left;
      font-size: 2em;
    }
    div.landing {
      text-align: center;
    }
    div.landing-link {
      padding-top: 10px;
    }
    a.landingBtn, button.landingBtn {
      width: 8em;
    }
  `]
})
export class AppComponent implements OnInit {
  title = 'Klusterfuck console';
  loggedIn = false;
  userNotSetup = false;
  user: User;

  private httpInterceptor: HttpInterceptor;

  constructor(
    private authApi: AuthApi,
    private auth: AuthService,
    private resolver: ComponentFactoryResolver,
    private alerts: AlertService,) {
  }

  ngOnInit(): void {
    this.httpInterceptor = HttpInterceptor.getInstance();
    this.auth.addTokenCallback(token => {
      this.httpInterceptor.setBearerToken(token)
    });
    this.httpInterceptor.registerStatusCallback(
      401,
      (status, res) => {
        //
      });

    this.auth.isLoggedIn().subscribe((loggedIn) => {
      if (loggedIn) {
        this.auth.getToken().subscribe(token => {
          this.httpInterceptor.setBearerToken(token);
          this.authApi.whoami()
            .subscribe(
              userResponse => {
                this.userNotSetup = !userResponse.user;
                this.loggedIn = true;
                this.user = userResponse.user;
                console.log(userResponse);
              },
              err => {
                console.log(err);
              });
        });
      } else {
        console.log('not logged in');
      }
    });
  }

  startLogin() {
    this.alerts.openComponent(this.resolver.resolveComponentFactory(LoginComponent))
      .subscribe(
        x => {
          console.log('Login modal:', x);
          // logged in
          this.authApi.whoami()
            .subscribe(
              userResponse => {
                this.userNotSetup = !userResponse.user;
                this.loggedIn = true;
                this.user = userResponse.user;
                console.log(userResponse);
              },
              err => {
                console.log(err);
              });
        },
        x => {console.log('Login modal:', x)},
        () => {console.log('finished login flow...')});
  }

  startRegister() {
    this.alerts.openComponent(this.resolver.resolveComponentFactory(RegisterComponent))
      .subscribe(
        x => {console.log(x)},
        x => {console.log(x)},
        () => {console.log('finished register flow...')});
  }

}
