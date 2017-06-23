import {Component, OnInit, ComponentFactoryResolver} from "@angular/core";
import {AuthApi, User} from '../client';

import {AlertService} from './services/alert.service';

import { RegisterComponent } from './components/register.component';

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
            <a class="landingBtn btn btn-primary" href="/sso/login">Login</a>
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

  constructor(
    private authApi: AuthApi,
    private resolver: ComponentFactoryResolver,
    private alerts: AlertService,) {}

  ngOnInit(): void {
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
        })
  }

  startRegister() {
    this.alerts.openComponent(this.resolver.resolveComponentFactory(RegisterComponent))
      .subscribe(
        x => {console.log(x)},
        x => {console.log(x)},
        () => {console.log('finished register flow...')});
  }

}
