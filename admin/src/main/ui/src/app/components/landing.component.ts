import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'landing',
    template: `
    <div style="text-align: center">
        <div>
            <a class="btn btn-primary" href="/sso/login">Login</a>
        </div>
        <div>
            <a class="btn btn-primary" routerLink="/register">Register</a>
        </div>
    </div>
    `,
  styles: [`
    div {
      padding: 10px;
    }
  `]
})
export class LandingComponent implements OnInit {

    ngOnInit(): void {
    }

}
