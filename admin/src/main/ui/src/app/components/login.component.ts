import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

import {AuthApi} from "../../client/api/AuthApi";
import {AlertService} from "../services/alert.service";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-login',
  template: `
    <div class="col-md-6 col-md-offset-3">
      <h2>Register</h2>
      <form name="form" (ngSubmit)="f.form.valid && login()" #f="ngForm" novalidate>
        <div class="form-group" [ngClass]="{ 'has-error': f.submitted && !email.valid }">
          <label for="email">Email</label>
          <input type="text" class="form-control" name="email" [(ngModel)]="model.email" #email="ngModel" required />
          <div *ngIf="f.submitted && !email.valid" class="help-block">Email is required</div>
        </div>
        <div class="form-group" [ngClass]="{ 'has-error': f.submitted && !password.valid }">
          <label for="password">Password</label>
          <input type="password" class="form-control" name="password" [(ngModel)]="model.password" #password="ngModel" required />
          <div *ngIf="f.submitted && !password.valid" class="help-block">Password is required</div>
        </div>
        <div class="form-group">
          <button [disabled]="loading" class="btn btn-primary">Login</button>
          <img *ngIf="loading" src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
          <a (click)="cancel()" class="btn btn-link">Cancel</a>
        </div>
      </form>
    </div>
  `,
  styles: []
})
export class LoginComponent implements OnInit {
  model: any;
  loading = false;
  modalControls: any;

  constructor(private fb: FormBuilder,
              private auth: AuthService,
              private alertService: AlertService) {
    this.model = fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  login() {
    this.loading = true;
    this.alertService.doInModal(
      'logging in...',
      () => this.auth.login(this.model['email'], this.model['password']))
      .subscribe(
        data => {
          // this.alertService.showAlert('Registration successful', '');
          // this.router.navigate(['/login']);
          this.modalControls.success(data);
        },
        error => {
          // this.alertService.showAlert('Registration failed', error.toString());
          this.loading = false;
          this.modalControls.failure(error);
        });
  }

  cancel() {
    this.modalControls.abort();
  }
}
