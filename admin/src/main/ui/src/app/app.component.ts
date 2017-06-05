import {Component} from "@angular/core";

@Component({
  selector: 'app-root',
  template: `
  <h3>
    <a [routerLink]="'/'">{{title}}</a>
  </h3>
  <p>
    <a [routerLink]="'/functions'">Functions</a>
    <a [routerLink]="'/flows'">Flows</a>
    <a [routerLink]="'/connectors'">Connectors</a>
  </p>
  <hr/>
  <router-outlet></router-outlet>
  `,
  styles: [``]
})
export class AppComponent {
  title = 'Klusterfuck console';
}
