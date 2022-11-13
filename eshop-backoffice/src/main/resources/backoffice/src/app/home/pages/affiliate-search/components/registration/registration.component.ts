import { FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Input } from '@angular/core';

import { finalize } from 'rxjs/operators';

import { BOValidator } from '../../../../../core/utils/validator';
import {
    RegistrationModel,
    CustomerCheckingModel,
    NotificationModel,
} from '../../models/registration.model';
import { CustomerService } from '../../../../services/customer/customer.service';
import { CustomerGroupRequestModel } from '../../../../models/customer-group/customer-group-request.model';
import { CustomerGroupService } from '../../../../services/customer-group/customer-group.service';
import { CustomerGroupSearchModel } from '../../../../models/customer-group/customer-group-search.model';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'backoffice-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
    @Input() affiliateShortName: string;

    registrationForm: any;
    customerCheckingForm: any;
    customerInfo: any;

    checkingFormNoti: NotificationModel;
    registryFormNoti: NotificationModel;
    registerState = true;
    checkCustomerState = true;
    customerGroups = [];
    isShownCustomerGroups = true;

    private initialRegistrationModel: RegistrationModel;
    private initialCustomerCheckingModel: CustomerCheckingModel;

    constructor(
        private fb: FormBuilder,
        private customerService: CustomerService,
        private customerGroupService: CustomerGroupService,
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
        this.initialRegistrationModel = new RegistrationModel({
            affiliate: this.affiliateShortName,
            userName: '',
            firstName: '',
            surName: '',
            email: '',
            collectionShortName: '',
        });

        this.initialCustomerCheckingModel = new CustomerCheckingModel({
            customerNumber: '',
            postCode: '',
            affiliate: this.affiliateShortName,
        });
        this.buildForms();
        this.getCustomerGroups();
    }

    checkCustomerInfo(formValue) {
        this.checkCustomerState = false;
        this.checkingFormNoti = null;
        this.registrationForm.reset(this.initialRegistrationModel);
        this.registrationForm.disable({ emitEvent: false });
        this.customerService
            .checkRegistrationInfo(formValue)
            .pipe(
                finalize(() => {
                    this.checkCustomerState = true;
                })
            )
            .subscribe(
                (result) => {
                    const res: any = result;
                    this.customerInfo = this.buildCustomerInfo(res);
                    this.registrationForm
                        .get('customerNumber')
                        .setValue(formValue.customerNumber);
                    this.registrationForm.get('postCode').setValue(formValue.postCode);
                    this.registrationForm.enable();
                    this.isShownCustomerGroups = !res.existed;
                },
                ({error}) => {
                    this.customerInfo = null;
                    this.checkingFormNoti = {
                        messages: ['REGISTER.MESSAGES.' + error.error_code],
                        status: false,
                    };
                }
            );
    }

    buildCustomerInfo(customerData) {
        let customerInfo = '';

        if (customerData.nr) {
            customerInfo += customerData.nr;
        }
        if (customerData.companyName) {
            customerInfo += ' - ' + customerData.companyName;
        }
        if (customerData.postCode) {
            customerInfo += ', ' + customerData.postCode;
        }
        if (customerData.city) {
            customerInfo += ' ' + customerData.city;
        }

        return customerInfo;
    }

    register(formValue: RegistrationModel) {
        if (this.registrationForm.valid) {
            this.registryFormNoti = null;
            this.registerState = false;
            this.customerService.register(formValue).subscribe(
                () => {
                    this.registerState = true;
                    this.registryFormNoti = {
                        messages: ['REGISTER.MESSAGES.MESSAGE_SUCCESS'],
                        status: true,
                    };
                    this.closeModal();
                },
                (error) => {
                    this.registerState = true;
                    this.registryFormNoti = {
                        messages: ['REGISTER.MESSAGES.' + error.error_code],
                        status: false,
                    };
                }
            );
        }
    }

    private buildForms() {
        this.buildRegistrationForm(this.initialRegistrationModel);
        this.buildCheckingForm(this.initialCustomerCheckingModel);
    }

    private buildRegistrationForm(registrationModel: RegistrationModel) {
        this.registrationForm = this.fb.group({
            customerNumber: ['', Validators.required],
            postCode: ['', Validators.required].push,
            affiliate: [registrationModel.affiliate, Validators.required],
            userName: [registrationModel.userName, Validators.required],
            firstName: [registrationModel.firstName, Validators.required],
            surName: [registrationModel.surName, Validators.required],
            email: [
                registrationModel.email,
                [Validators.required, BOValidator.emailValidator],
            ],
            collectionShortName: ['', Validators.required],
            hasPartnerprogramView: [registrationModel.hasPartnerprogramView],
        });
        this.registrationForm.disable({ emitEvent: false });
    }

    private buildCheckingForm(customerCheckingModel: CustomerCheckingModel) {
        this.customerCheckingForm = this.fb.group({
            customerNumber: [
                customerCheckingModel.customerNumber,
                Validators.required,
            ],
            affiliate: [this.affiliateShortName, Validators.required],
            postCode: [customerCheckingModel.postCode, Validators.required],
        });
    }

    private closeModal() {
        this.bsModalRef.hide();
    }

    private getCustomerGroups() {
        const customerGroupRequestModel = new CustomerGroupRequestModel();
        customerGroupRequestModel.affiliate = this.affiliateShortName;
        customerGroupRequestModel.useWholePage = true;
        this.customerGroupService.search(customerGroupRequestModel).subscribe(
            (customerSearchModel: CustomerGroupSearchModel) => {
                this.customerGroups = customerSearchModel.content.map((item) => {
                    return {
                        value: item.collectionShortName,
                        label: item.collectionName,
                    };
                });
                if (this.customerGroups.length > 0) {
                    this.initialRegistrationModel.collectionShortName = this.customerGroups[0].value;
                    this.registrationForm
                        .get('collectionShortName')
                        .setValue(this.initialRegistrationModel.collectionShortName);
                }
            },
            () => {
                this.customerGroups = [];
            }
        );
    }
}
