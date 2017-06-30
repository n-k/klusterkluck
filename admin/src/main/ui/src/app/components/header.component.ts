import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-header',
  template: `
  <p>
    <a [routerLink]="'/functions'">Functions</a>
    <a [routerLink]="'/flows'">Flows</a>
    <!--<a [routerLink]="'/connectors'">Connectors</a>-->
    
    <a class="logout" (click)="logout()">Logout</a>
  </p>
  <hr/>
  `,
  styles: [`
  a.logout {
    padding-left: 10px;
    cursor: pointer;
  }
  `]
})
export class HeaderComponent {
  @Output() onLogout = new EventEmitter<void>();

  logout() {
    this.onLogout.emit();
  }
}
