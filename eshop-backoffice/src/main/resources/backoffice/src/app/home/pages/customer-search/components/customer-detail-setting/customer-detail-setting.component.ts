import { Component, OnInit, Input, OnChanges, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormArray, Form, FormGroup } from '@angular/forms';

import { CustomerSetting } from '../../model/customer-setting.model';
import { CustomerSettingDetailModel } from '../../model/customer-setting-detail.model';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { WSS_DELIVERY_TYPE } from 'src/app/core/conts/app.constant';
import { Subscription } from 'rxjs';
import AffUtils from 'src/app/core/utils/aff-utils';

@Component({
    selector: 'backoffice-customer-detail-setting',
    templateUrl: './customer-detail-setting.component.html',
    styleUrls: ['./customer-detail-setting.component.scss'],
})
export class CustomerDetailSettingComponent implements OnInit, OnDestroy {
    @Input() customerInfo: any;
    @Input() set customerSetting(val: CustomerSetting) {
        if (!!val) {
            this.customerSettingModel = new CustomerSettingDetailModel(
                val.customerSettingsBODto
            );
            this.paymentMethods = val.paymentMethods;
            this.deliveryTypes = val.deliveryTypes;
            this.wssDeliveryType = val.deliveryTypes;
            this.collections = this.customerSettingModel.collections.map((item) => {
                return { value: item.collectionShortName, label: item.name };
            });

            const isShowOciVat = this.customerSettingModel.perms.find(perm => perm.permission === 'OCI').enable;
            if (isShowOciVat) {
                this.customerSettingModel.isShowOciVatEditable = isShowOciVat;
            }

            this.buildForm();
        }
    }

    customerSettingModel: CustomerSettingDetailModel;
    customerSettingForm: FormGroup;
    toggleCondition = true;
    isFormExpanded = false;
    // groups = [];

    paymentMethods = [];
    permissionList = [];
    deliveryTypes = [];
    collections = [];
    wssDeliveryType = [];

    private subs = new Subscription();

    isSb = AffUtils.isSB();

    isCh = AffUtils.isCH();

    isAT = AffUtils.isAT();

    constructor(
        private customerService: CustomerService,
        private fb: FormBuilder
    ) { }

    ngOnInit() { }


    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    get perms() {
        return this.customerSettingForm.get('perms') as FormArray;
    }

    buildPermListControl() {
        const permList: any[] = [];
        this.permissionList = this.customerSettingModel.perms;
        this.permissionList.map(perm => {
            permList.push(this.fb.control({ value: perm.enable, disabled: !perm.editable }));
        });
        return this.fb.array(permList);
    }

    buildForm() {
        this.customerSettingForm = this.fb.group({
            collections: {
                value: this.customerSettingModel.collectionShortName,
                // tslint:disable-next-line: max-line-length
                label: this.customerSettingModel.collections.filter(col => col.collectionShortName === this.customerSettingModel.collectionShortName)[0].name,
                disabled: false,
            },
            paymentId: this.customerSettingModel.paymentId,
            deliveryId: {
                value: this.customerSettingModel.deliveryId,
                disabled: false,
            },
            sessionTimeoutMinutes: new FormControl({
                value: this.customerSettingModel.sessionTimeoutMinutes,
                disabled: false,
            }),
            emailNotificationOrder: new FormControl({
                value: this.customerSettingModel.emailNotificationOrder,
                disabled: false,
            }),
            demoCustomer: {
                value: this.customerSettingModel.demoCustomer,
                disabled: false,
            },
            normautoDisplay: {
                value: this.customerSettingModel.normautoDisplay,
                disabled: false,
            },
            hasPartnerprogramView: {
                value: this.customerSettingModel.hasPartnerprogramView,
                disabled: false,
            },
            showOciVat: { value: this.customerSettingModel.showOciVat, disabled: !this.customerSettingModel.isShowOciVatEditable },
            wssDeliveryId: [this.customerSettingModel.wssDeliveryId],
            perms: this.buildPermListControl(),
            netPriceView: {
                value: this.customerSettingModel.netPriceView,
                disabled: false
            },
            showDiscount: {
                value: this.customerSettingModel.showDiscount,
                disabled: false
            }
        });

        if (this.wholesalerEnabled && !this.wholesalerEnabled.value) {
            this.customerSettingForm.get('wssDeliveryId').disable();
        }

        this.subs.add(this.wholesalerEnabled.valueChanges.subscribe(value => {
            if (!value) {
                this.customerSettingForm.get('wssDeliveryId').disable();
            } else {
                this.customerSettingForm.get('wssDeliveryId').enable();
            }
        }));

        this.customerSettingForm.valueChanges.subscribe(val => {
            this.mapToSettingModel(val);
        });
    }

    // return data to customer detail
    public getSetting() {
        return this.customerSettingModel.dto;
    }

    mapToSettingModel(val) {
        this.customerSettingModel.paymentId = val.paymentId || this.customerSettingModel.paymentId;
        this.customerSettingModel.deliveryId = val.deliveryId || this.customerSettingModel.deliveryId;
        this.customerSettingModel.demoCustomer = val.demoCustomer;
        this.customerSettingModel.emailNotificationOrder = val.emailNotificationOrder;
        this.customerSettingModel.hasPartnerprogramView = val.hasPartnerprogramView;
        this.customerSettingModel.normautoDisplay = val.normautoDisplay;
        this.customerSettingModel.sessionTimeoutMinutes = val.sessionTimeoutMinutes || this.customerSettingModel.sessionTimeoutMinutes;
        this.customerSettingModel.showOciVat = val.showOciVat;
        this.customerSettingModel.netPriceView = val.netPriceView;
        this.customerSettingModel.showDiscount = val.showDiscount;
        this.customerSettingModel.wssDeliveryId = val.wssDeliveryId;
        const permsForm: FormArray = this.customerSettingForm.get('perms') as FormArray;
        this.customerSettingModel.perms = this.customerSettingModel.perms.map((p, index) => {
            const enable = permsForm.controls[index] ? permsForm.controls[index].value : p.enable;
            const editable = permsForm.controls[index] ? permsForm.controls[index].enabled : p.editable;
            return { ...p, enable, editable };
        });
    }

    collectionChanged(event) {
        const collectonShortName = event && event.value || '';

        this.customerService
            .getCustomerCollection(collectonShortName)
            .subscribe((result) => {
                const res: any = result;
                this.customerSettingModel.collectionShortName = collectonShortName;
                this.customerSettingModel.perms = res.permissions;
                this.buildForm();
            });
    }

    permissionChanged(ctrl: FormControl, changedPerm) {
        this.customerSettingModel.perms.map(perm => {
            if (perm.permission === changedPerm.permission) {
                perm.enable = ctrl.value;
            }

            if (perm.permission === 'OCI') {
                this.customerSettingModel.isShowOciVatEditable = perm.enable;
                if (perm.enable) {
                    this.customerSettingForm.get('showOciVat').enable();
                } else {
                    this.customerSettingForm.get('showOciVat').setValue(false);
                    this.customerSettingForm.get('showOciVat').disable();
                }
            }
            return perm;
        });
    }

    get wholesalerEnabled(): FormControl {
        let control = null
        this.permissionList.map((perm, index) => {
            if (perm.permission === 'WHOLESALER') {
                control = this.perms && this.perms.controls[index]
            }
        });
        return control;
    }
}
