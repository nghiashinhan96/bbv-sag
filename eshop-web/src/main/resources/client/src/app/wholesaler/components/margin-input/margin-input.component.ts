import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'connect-margin-input',
  templateUrl: './margin-input.component.html',
  styleUrls: ['./margin-input.component.scss']
})
export class MarginInputComponent implements OnInit {
  @Input() form: FormGroup;
  @Input() controlName: string;
  @Input() className: string = '';

  constructor () { }

  ngOnInit() {
      setTimeout(()=>{
          this.validateInput(null);
      })
  }

  validateInput(event) {
    const value = this.form.controls[this.controlName].value || null;

    if (value === '' || value === null) {
      return;
    }

    const isValidRange = Number(value) >= 0 && Number(value) < 100;
    if (isValidRange) {
      this.form.controls[this.controlName].setErrors(null);
    } else {
      this.form.controls[this.controlName].setErrors({ invalidRange: true });
    }
  }

}
