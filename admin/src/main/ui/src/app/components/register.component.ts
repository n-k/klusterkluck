import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AlertService } from '../services/alert.service';
import { AuthApi } from '../../client';

@Component({
  templateUrl: 'register.component.html'
})

export class RegisterComponent {
  model: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private auth: AuthApi,
    private alertService: AlertService) {
    this.model = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  register(value) {
    this.loading = true;
    this.auth.register({
        firstName: this.model['firstName'],
        lastName: this.model['lastName'],
        email: this.model['email'],
        password: this.model['password']
      })
      .subscribe(
        data => {
          console.log(data);
          // set success message and pass true paramater to persist the message after redirecting to the login page
          this.alertService.success('Registration successful', true);
          this.router.navigate(['/login']);
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }
}
