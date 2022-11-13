import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';

import { SagValidator } from 'sag-common';
import { SubSink } from 'subsink';
import { finalize } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';

import { PotentialCustomerModel } from '../../models/potenial-customer.model';
import { SagAuthService } from '../../services/sag-auth.service';
import { SagAuthConfigService } from './../../services/sag-auth.config';
import { PotentialCustomerFieldModel } from '../../models/potential-customer-field.model';
import { SagAuthStorageService } from './../../services/sag-auth-storage.service';
import { POTENTIAL_CUSTOMER_FIELD } from './../../constants/potential-customer-field';

@Component({
  selector: 'sag-auth-potential-customer-registration',
  templateUrl: './potential-customer-registration.component.html',
  styleUrls: ['./potential-customer-registration.component.scss']
})

export class SagPotentialCustomerRegistrationComponent implements OnInit, OnDestroy {
  @Input() isShown: boolean;
  @Output() isShownChange: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() onRegister = new EventEmitter<string>();

  errorMsg: string = null;
  isSent: boolean = false;
  isSending: boolean = false;
  contactForm: FormGroup;

  subs = new SubSink();

  constructor (
    private authService: SagAuthService,
    private config: SagAuthConfigService,
    private appStorage: SagAuthStorageService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.initForm();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  back() {
    this.isShown = false;
    this.isShownChange.emit(this.isShown);
    this.isSent = false;
    this.errorMsg = null;
    this.isSending = false;
    this.contactForm.reset();
  }

  save() {
    if (this.contactForm.valid) {
      this.isSending = true;
      this.contactForm.disable();

      const fields = this.getFields(this.contactForm.getRawValue());

      const body = new PotentialCustomerModel(<PotentialCustomerModel>{
        collectionShortName: this.config.affiliate,
        langCode: this.appStorage.langCode,
        fields: fields
      });

      this.subs.sink = this.authService.registerPotentialCustomer(body)
        .pipe(
          finalize(() => {
            this.isSending = false;
            this.contactForm.enable();
          }))
        .subscribe(() => {
          this.isSent = true;
        }, () => {
          this.errorMsg = 'ERROR_MESSAGE.SYSTEM_ERROR';
        });
        this.onRegister.emit('potential-customer-register');
    }
  }

  private getFields(formValue): PotentialCustomerFieldModel[] {
    let fields: PotentialCustomerFieldModel[] = [];

    Object.keys(POTENTIAL_CUSTOMER_FIELD).forEach((keyValue: any) => {
      const obj = new PotentialCustomerFieldModel(<PotentialCustomerFieldModel>{
        key: POTENTIAL_CUSTOMER_FIELD[keyValue].key,
        value: formValue[keyValue],
        title: this.translateService.instant(POTENTIAL_CUSTOMER_FIELD[keyValue].label)
      });

      fields.push(obj);
    });

    return fields;
  }

  private initForm() {
    this.contactForm = new FormGroup({
      company: new FormControl('', [Validators.required]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      street: new FormControl(''),
      postalCode: new FormControl('', [Validators.required]),
      city: new FormControl('', [Validators.required]),
      phone: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, SagValidator.emailValidator]),
      website: new FormControl(''),
      comment: new FormControl('')
    });
  }
}
