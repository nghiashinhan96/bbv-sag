import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SagAuthConfigService } from '../../services/sag-auth.config';
import { SagAuthStorageService } from '../../services/sag-auth-storage.service';
import { SagAuthService } from '../../services/sag-auth.service';
import { SagMessageData, SagValidator } from 'sag-common';

@Component({
    selector: 'sag-auth-customer-registration',
    templateUrl: './customer-registration.component.html',
    styleUrls: ['./customer-registration.component.scss']
})
export class SagCustomerRegistrationComponent implements OnInit {
    @Output() addtionalPageHandler: EventEmitter<any> = new EventEmitter<any>();
    @Output() gobackVerifyCodeEmitter = new EventEmitter();
    @Output() onRegister = new EventEmitter<string>();
    @Input() ssoWhitelist: string[] = [];

    disabledRegistry = true;
    disabledOnBehalf = true;
    showOnBehalf = false;
    validOnBehalf = false;
    checkingFormNoti: string[];
    registryFormNoti: SagMessageData;
    customerInfo: any;
    checkCustomerState = true;
    registerState = true;

    registrationForm: FormGroup;
    registerForm: FormGroup;
    searchCustomerForm: FormGroup;

    private redirectUrl: string;
    private validCustomerNumber: string;
    private validPostCode: string;
    private affiliate;

    constructor(
        private fb: FormBuilder,
        private authService: SagAuthService,
        private appStorage: SagAuthStorageService,
        private config: SagAuthConfigService
    ) {
        this.affiliate = config.affiliate;
        this.redirectUrl = config.redirectUrl + 'forgotpassword/verifycode';
    }

    ngOnInit() {
        this.buildRegistrationForm();
        this.showOnBehalf = this.isShowOnBehalf();
    }

    isShowOnBehalf() {
        let domain = window.location.origin;
        return this.ssoWhitelist.indexOf(domain) > -1;
    }

    buildRegistrationForm() {
        this.searchCustomerForm = this.fb.group({
            customerNumber: [this.validCustomerNumber, Validators.required],
            affiliate: this.affiliate,
            postCode: [this.validPostCode, Validators.required],
        });

        this.registerForm = this.fb.group({
            affiliate: this.affiliate,
            userName: ['', Validators.required],
            firstName: ['', Validators.required],
            surName: ['', Validators.required],
            email: ['', [Validators.required, SagValidator.emailValidator]],
            createOnBehalfOnly : false
        });

        this.registrationForm = this.fb.group({
            searchCustomerForm: this.searchCustomerForm,
            registerForm: this.registerForm
        });
    }

    onCheckOnBehalfOnly($event){
        this.validOnBehalf = false;
        if (this.validCustomerNumber){
            if ($event.target.checked){
                this.registerForm.reset();
                this.validOnBehalf = true;
                this.disabledRegistry = true;
                this.registerForm.controls['createOnBehalfOnly'].setValue(true);
                return;
            }
            this.disabledRegistry = false;
        }
    }

    checkCustomerInfo(formValue) {
        this.registerForm.reset();
        this.validOnBehalf = false;
        this.disabledOnBehalf = true;
        this.checkCustomerState = false;
        this.checkingFormNoti = null;
        this.authService.checkRegistrationInfo(formValue).subscribe(
            res => {
                const data: any = { ...res };
                this.customerInfo = data.customerInfo;
                this.validCustomerNumber = formValue.customerNumber;
                this.validPostCode = formValue.postCode;
                this.disabledRegistry = false;
                this.disabledOnBehalf = false;
                this.checkCustomerState = true;
            },
            ({ error }) => {
                this.customerInfo = null;
                this.validCustomerNumber = null;
                this.validPostCode = null;
                this.disabledRegistry = true;
                this.checkingFormNoti = this.handleCustomerErrorCode(error);
                this.checkCustomerState = true;
            }
        );
    }

    register(formValue) {
        formValue.customerNumber = this.validCustomerNumber;
        formValue.postCode = this.validPostCode;
        formValue.accessUrl = this.redirectUrl;
        formValue.affiliate = this.affiliate;

        if (this.registrationForm.valid || this.validOnBehalf) {
            this.registryFormNoti = null;
            this.registerState = false;
            this.authService.register(formValue)
                .subscribe(
                    () => {
                        this.registerState = true;
                        this.registryFormNoti = { message: 'REGISTER.MESSAGES.MESSAGE_SUCCESS', type: 'SUCCESS' };
                    },
                    ({ error }) => {
                        this.registerState = true;
                        this.registryFormNoti = { message: 'REGISTER.MESSAGES.' + error.error_code, type: 'ERROR' };
                    }
                );
            this.onRegister.emit('customer-registration');
        }
        return false;
    }

    private handleCustomerErrorCode(error): string[] {
        if (!error) {
            return [];
        }
        const patternStr = 'CUSTOMER_INFO.MESSAGES.';
        let technicalError = '';
        if (error.code === 500 && error.error_code === 'OTHERS') {
            technicalError = patternStr + 'TECHNICAL';
            return [patternStr + error.error_code, technicalError, error.message];
        }
        return [patternStr + error.error_code];
    }
}
