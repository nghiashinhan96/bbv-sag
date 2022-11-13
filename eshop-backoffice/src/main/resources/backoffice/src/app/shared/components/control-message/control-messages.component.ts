import { Component, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { BOValidator } from 'src/app/core/utils/validator';

@Component({
  selector: "control-messages",
  template: `<div
    class="alert alert-danger alert-custom"
    *ngIf="errorMessage != ''"
  >
    {{ errorMessage }}
  </div>`,
})
export class ControlMessagesComponent {
  @Input() control: any;
  /**
   *
   */
  constructor(private translateService: TranslateService) { }

  get errorMessage(): string {
    for (const propertyName in this.control.errors) {
      if (
        this.control.errors.hasOwnProperty(propertyName) &&
        this.control.touched
      ) {
        return BOValidator.getValidatorErrorMessage(
          propertyName,
          this.translateService,
          this.control.errors[propertyName]
        );
      }
    }
    return '';
  }
}
