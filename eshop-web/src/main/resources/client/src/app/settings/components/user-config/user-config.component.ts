import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { UserSetting, UpdateUserSetting } from 'src/app/core/models/user-setting.model';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { PaymentMethod } from 'src/app/core/models/payment-method.model';
import { AffiliateEnum, AffiliateUtil, SagMessageData } from 'sag-common';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { environment } from 'src/environments/environment';
import { FinalUserAdminService } from '../../services/final-user-admin.service';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'connect-user-config',
    templateUrl: './user-config.component.html',
    styleUrls: ['./user-config.component.scss']
})
export class UserConfigComponent implements OnInit {
    @Input() userDetail: UserDetail;
    @Input() userSetting: UserSetting;
    @Input() userPaymentSettings: PaymentSetting;

    @Output() updateUserConfigEmitter = new EventEmitter<{ request: any, callback: any }>();

    userSettingForm: FormGroup;

    isUpdating = false;
    result: SagMessageData;

    cz = AffiliateEnum.CZ;
    ehcz = AffiliateEnum.EH_CZ;
    isSb = AffiliateUtil.isSb(environment.affiliate);
    sb = AffiliateEnum.SB;
    axcz = AffiliateEnum.AXCZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);

    constructor(
        private fb: FormBuilder,
        private finalUserAdminService: FinalUserAdminService
    ) { }

    ngOnInit() {
        this.initForm(this.userSetting);
        if (this.userDetail.isSalesOnBeHalf) {
            this.userSettingForm.disable();
        }
        this.netPriceView.valueChanges.subscribe(value => {
            if (!value) {
                this.netPriceConfirm.disable();
                this.netPriceConfirm.setValue(false);
            } else {
                this.netPriceConfirm.enable();
            }
        });
    }

    onSubmit() {
        if (this.userSettingForm.valid) {
            this.isUpdating = true;
            this.result = null;

            this.updateUserConfigEmitter.emit({
                request: this.userSettingForm.getRawValue(),
                callback: (result) => this.afterSubmit(result)
            });
            SpinnerService.start('.user-config-form');
        }
    }

    updatePaymentMethod(paymentMethod: PaymentMethod) {

    }

    private afterSubmit(result: SagMessageData) {
        this.isUpdating = false;
        this.result = result;
        SpinnerService.stop('.user-config-form');
    }

    get netPriceView() { return this.userSettingForm.get('netPriceView'); }
    get netPriceConfirm() { return this.userSettingForm.get('netPriceConfirm'); }
    get paymentMethod() { return this.userSettingForm.get('paymentId'); }

    private initForm(userSetting: UserSetting) {
        this.userSettingForm = this.fb.group({
            allocationId: userSetting.allocationId,
            deliveryAddressId: userSetting.deliveryAddressId,
            billingAddressId: userSetting.billingAddressId,
            viewBilling: [{
                value: (userSetting && userSetting.viewBilling) || false,
                disabled: userSetting && !userSetting.allowViewBillingChanged
            }],
            netPriceView: [{
                value: (userSetting && userSetting.netPriceView) || false,
                disabled: userSetting && !userSetting.allowNetPriceChanged || this.isSb
            }],
            netPriceConfirm: [
                {
                    value: (userSetting && userSetting.netPriceConfirm) || false,
                    disabled: userSetting && !userSetting.allowNetPriceConfirmChanged || this.isSb
                }
            ],
            classicCategoryView: [(userSetting && userSetting.classicCategoryView)],
            emailNotificationOrder: [(userSetting && userSetting.emailNotificationOrder)],
            invoiceId: [{
                value: userSetting && userSetting.invoiceId,
                disabled: true
            }],
            paymentId: [{
                value: userSetting && userSetting.paymentId,
                disabled: true
            }],
            deliveryId: [{
                value: userSetting && userSetting.deliveryId,
                disabled: true
            }],
            collectiveDelivery: [userSetting && userSetting.collectiveDelivery],
            singleSelectMode: [(userSetting && userSetting.singleSelectMode)]
        });

        if (this.isEhCh || this.isEhCz) {
            SpinnerService.start('.user-config-form')
            this.finalUserAdminService.getFinalUserProfile()
                .pipe(finalize(() => {
                    SpinnerService.stop('.user-config-form')
                }))
                .subscribe(res => {
                    const { showNetPriceEnabled } = res as any;
                    let isNetPriceViewDisable = !(this.userDetail.wholeSalerHasNetPrice && showNetPriceEnabled);
                    if (isNetPriceViewDisable) {
                        this.userSettingForm.get('netPriceView').disable();
                        this.userSettingForm.get('netPriceConfirm').disable();
                    } else {
                        this.userSettingForm.get('netPriceView').enable();
                    }
                });
        }
    }
}
