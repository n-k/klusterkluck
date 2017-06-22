import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  template: `
  <p>
    <a [routerLink]="'/functions'">Functions</a>
    <a [routerLink]="'/flows'">Flows</a>
    <a [routerLink]="'/connectors'">Connectors</a>
    
    <a class="logout" href="./api/v1/auth/logout">Logout</a>
  </p>
  <hr/>
  `,
  styles: [`
  a.logout {
    padding-left: 10px;
  }
  `]
})
export class HeaderComponent {}
