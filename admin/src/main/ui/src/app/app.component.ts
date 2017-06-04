import {Component} from "@angular/core";

@Component({
  selector: 'app-root',
  template: `
  <h3>
    <a [routerLink]="'/'">{{title}}</a>
  </h3>
  <hr/>
  <router-outlet></router-outlet>
  `,
  styles: [``]
})
export class AppComponent {
  title = 'Klusterfuck console';
}
