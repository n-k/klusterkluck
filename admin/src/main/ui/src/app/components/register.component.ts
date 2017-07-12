import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

import {AlertService} from '../services/alert.service';
import {AuthApi} from '../../client';

@Component({
  selector: 'register',
  templateUrl: 'register.component.html'
})

export class RegisterComponent {
  model: any;
  loading = false;
  modalControls: any;

  constructor(private fb: FormBuilder,
              private router: Router,
              private auth: AuthApi,
              private alertService: AlertService) {
    this.model = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      userName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  register() {
    this.loading = true;
    this.alertService.doInModal(
      'Registering user...',
      () => this.auth.register({
        firstName: this.model['firstName'],
        lastName: this.model['lastName'],
        username: this.model['userName'],
        email: this.model['email'],
        password: this.model['password']
      })).subscribe(
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
