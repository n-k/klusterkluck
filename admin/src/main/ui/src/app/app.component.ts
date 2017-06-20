import {Component} from "@angular/core";

@Component({
  selector: 'app-root',
  template: `
  <h3 style="display: inline">
    {{title}}
  </h3>
  <p>
    <a [routerLink]="'/functions'">Functions</a>
    <a [routerLink]="'/flows'">Flows</a>
    <a [routerLink]="'/connectors'">Connectors</a>
  </p>
  <hr/>
  <alert></alert>
  <router-outlet></router-outlet>
  `,
  styles: [``]
})
export class AppComponent {
  title = 'Klusterfuck console';
}
