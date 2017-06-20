import { Component, OnInit } from '@angular/core';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'alert',
  templateUrl: 'alert.component.html'
})

export class AlertComponent {
  message: any;
  timer: any;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.getMessage().subscribe(message => {
      this.message = message;
      if (this.timer) {
        this.timer.cancel();
      }
      this.timer = window.setTimeout(() => {
        this.message = null;
      }, 2000);
    });
  }
}
