import {Component, OnInit, ComponentFactoryResolver} from "@angular/core";
import {AuthApi, User} from '../client';

import {AlertService} from './services/alert.service';

import { RegisterComponent } from './components/register.component';

@Component({
  selector: 'app-root',
  template: `
    <div style="text-align: right; padding-right: 10px; height: 8em;">
      <span 
        style="float: left; font-size: 2em;">.
.
k̳̼͉̭̙̰̺̬̑͒̄̌͘̕ͅl̸̨̧̞̯̂̏͌̀͢͝ú̖̗̗̥͕͈̓̿̽͑͊̀̚s̻͕͈͖̞̯̭̀̃͂͌̈͠͡t̵̗̺̪̰͎̺̯͊̎́͊̽͜͟͝͡ȩ̴͎̱̮͇̺̟̬̥͊͆̂͒̇ͅr̸̡̢͎̺̥͈̜͎͗́̿͂̀͞͞f̴̢̩̫͚̘̻͕̠̓̊͗͌͗͘͡͝ư̲̣̟̘̰̯͈͒͑͛̍͐̂̚c̴̛̱̥̯͈̎̒̎͒̑̚̚͟ķ̶̲̠̺͔̜͕͍̜̔̽̊̕͢͞͞
.
.</span>
      <h3 style="display: inline">
        {{title}}
      </h3>
      <h4>{{user && user.email}}</h4>
      <app-header *ngIf="loggedIn && !userNotSetup"></app-header>
    </div>
    <alert></alert>
    <router-outlet *ngIf="loggedIn && !userNotSetup"></router-outlet>
    <div *ngIf="!loggedIn && !userNotSetup" style="text-align: center">
        <div style="padding: 10px;">
            <a class="btn btn-primary" href="/sso/login">Login</a>
        </div>
        <div style="padding: 10px;">
            <button (click)="startRegister()" class="btn btn-primary">Register</button>
        </div>
    </div>
    <div *ngIf="userNotSetup">
      <p>Your account has not been setup yet, or you have logged in with an administrator account</p>
      <p>
        <a class="logout" href="./api/v1/auth/logout">Logout</a>
      </p>
    </div>
  `,
  styles: [``]
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
