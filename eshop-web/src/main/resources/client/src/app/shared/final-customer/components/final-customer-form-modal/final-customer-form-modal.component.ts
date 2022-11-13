import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators, FormControl } from '@angular/forms';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { uniqBy } from 'lodash';

import { SpinnerService } from 'src/app/core/utils/spinner';
import { FinalCustomerTemplate } from 'src/app/settings/models/final-customer/final-customer-template';
import { SagMessageData, AffiliateEnum, AffiliateUtil } from 'sag-common';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { FinalCustomerDetail } from 'src/app/settings/models/final-customer/final-customer-detail.model';
import { PermissionConfigurationModel } from 'src/app/settings/models/permission-configuration.model';
import { Validator } from 'src/app/core/utils/validator';
import { DeliveryProfileService } from 'src/app/wholesaler/services/delivery-profile.service';
import { DeliveryProfileRequestModel } from 'src/app/wholesaler/models/delivery-profile.model';
import { UserService } from 'src/app/core/services/user.service';
import { Constant } from 'src/app/core/conts/app.constant';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-final-customer-form-modal',
    templateUrl: 'final-customer-form-modal.component.html',
    styleUrls: ['final-customer-form-modal.component.scss']
})
export class FinalCustomerFormModalComponent implements OnInit {
    @Input() title: string;
    @Input() user: UserDetail;
    @Input() init: Observable<FinalCustomerTemplate>;
    @Input() submit: any;

    isProfileOpened = true;
    isSettingOpened = true;

    form: FormGroup;
    customerTypeOptions = [];
    salutationOptions = [];
    permissionOptions = [];
    deliveryTypes = [];

    deliveryProfileOptions = [];
    readonly DP_PAGE_SIZE = 20;
    currentDPPage = 0;
    totalDPPage = 0;

    result: SagMessageData;
    marginGroups = [];
    sb = AffiliateEnum.SB;
    isCz = AffiliateUtil.isAffiliateCZ10(environment.affiliate);

    private readonly spinnerSelector = '.modal-body';

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private deliveryProfileService: DeliveryProfileService,
        private userService: UserService
    ) { }

    ngOnInit() {
        this.form = this.fb.group({
            collectionShortname: '',
            customerType: '',
            isActive: true,
            customerNr: ['', Validators.required],
            customerName: ['', Validators.required],
            salutation: '',
            surName: ['', Validators.required],
            firstName: ['', Validators.required],
            street: '',
            addressOne: '',
            addressTwo: '',
            poBox: '',
            postcode: ['', Validators.required],
            place: ['', Validators.required],
            phone: '',
            fax: '',
            email: ['', Validator.emailValidator],
            showNetPrice: { value: false, disabled: true },
            perms: this.fb.array([]),
            wssDeliveryProfileDto: [''],
            deliveryId: [''],
            wssMarginGroup: ''
        });
        this.customerNr.valueChanges.subscribe((data) => {
            this.customerNr.setValue((data || '').replace(/[^a-zA-Z0-9]/g, ''), { emitEvent: false });
        });
        this.initData();
    }

    get permissions() { return this.form.get('perms') as FormArray; }
    get customerNr() { return this.form.get('customerNr') as FormControl; }
    get customerName() { return this.form.get('customerName') as FormControl; }
    get surName() { return this.form.get('surName') as FormControl; }
    get firstName() { return this.form.get('firstName') as FormControl; }
    get postcode() { return this.form.get('postcode') as FormControl; }
    get place() { return this.form.get('place') as FormControl; }
    get email() { return this.form.get('email') as FormControl; }

    initData() {
        SpinnerService.start(this.spinnerSelector);
        this.init
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe((res: FinalCustomerTemplate) => {
                this.customerTypeOptions = res.customerTypes.map(value => ({ label: value, value }));
                this.salutationOptions = res.salutations;
                this.deliveryTypes = res.deliveryTypes;
                this.marginGroups = res.marginGroups;

                this.fetchMoreDeliveryProfile();
                let customer: FinalCustomerDetail;

                if (res.selectedFinalCustomer) {
                    customer = res.selectedFinalCustomer;
                    if (customer.wssDeliveryProfileDto) {
                        this.deliveryProfileOptions = [customer.wssDeliveryProfileDto];
                    }
                } else {
                    customer = new FinalCustomerDetail({
                        customerType: res.customerTypes[0],
                        salutation: res.salutations[0].code,
                        collectionShortname: this.user.collectionShortname,
                        showNetPrice: res.showNetPrice,
                        isActive: true,
                        wssDeliveryProfileDto: '',
                        deliveryId: res.defaultDeliveryType,
                        wssMarginGroup: ''
                    });
                }
                this.form.patchValue(customer);

                if (this.userService.userDetail.wholeSalerHasNetPrice) {
                    this.form.get('showNetPrice').enable();
                }

                this.form.get('wssDeliveryProfileDto').patchValue(customer.wssDeliveryProfileDto ? customer.wssDeliveryProfileDto.id : null);
                this.form.get('wssMarginGroup').patchValue(customer.wssMarginGroup || null);

                res.permissions.forEach(item => {
                    const ctrl = this.getPermissionControl(item);
                    if (ctrl) {
                        this.permissions.push(ctrl);
                    }
                });
            }, () => {
                this.result = { type: 'WARNING', message: 'SEARCH.NO_RESULTS_FOUND' } as SagMessageData;
            });
    }

    onSubmit() {
        if (this.form.invalid) {
            return;
        }

        SpinnerService.start(this.spinnerSelector);
        const data = this.form.getRawValue();

        if (data.wssDeliveryProfileDto) {
            data.wssDeliveryProfileDto = this.deliveryProfileOptions.find(opt => opt.id == data.wssDeliveryProfileDto);
        }

        this.form.markAsDirty();

        this.submit({
            data,
            onSuccess: () => {
                this.result = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;
                SpinnerService.stop(this.spinnerSelector);
                this.bsModalRef.hide();
            },
            onError: () => {
                this.result = { type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' } as SagMessageData;
                SpinnerService.stop(this.spinnerSelector);
                this.bsModalRef.hide();
            }
        });
    }

    fetchMoreDeliveryProfile() {
        this.deliveryProfileService.getDeliverProfileList(new DeliveryProfileRequestModel(), { currentPage: this.currentDPPage, itemsPerPage: this.DP_PAGE_SIZE }).subscribe((res: any) => {
            this.totalDPPage = res.totalPages || 0;
            this.deliveryProfileOptions = uniqBy([...res.content, ...this.deliveryProfileOptions], 'id');
        }, error => {
            console.log(error);
        });
    }

    onDeliveryProfileListScrollEnd() {
        if (this.currentDPPage == this.totalDPPage - 1) {
            return;
        }
        this.currentDPPage += 1;
        this.fetchMoreDeliveryProfile();
    }

    private getPermissionControl(permission: PermissionConfigurationModel) {
        let enable = permission.enable;
        let editable = true;
        if (permission.permission === 'HAYNESPRO') {
            editable = false;
            enable = false;
        }
        if (this.isCz && permission.permission === Constant.PERMISSIONS.DVSE) {
            return null;
        }
        return this.fb.group({
            permission: permission.permission,
            langKey: permission.langKey,
            permissionId: permission.permissionId,
            enable,
            editable
        });
    }
}
