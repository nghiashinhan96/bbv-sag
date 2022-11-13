import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { isEmpty } from 'lodash';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { PaymentMethod } from 'src/app/core/models/payment-method.model';
import { UserSetting } from 'src/app/core/models/user-setting.model';
import { ERP_INVOICE_TYPE, INVOICE_TYPE_CODE, DEFAULT_INVOICE_TYPE } from '../../enums/invoice/invoice.enum';
import { ADDRESS_TYPE, DELIVERY_TYPE, PAYMENT_METHOD } from 'src/app/core/enums/shopping-basket.enum';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-order-conditions',
    templateUrl: './order-conditions.component.html',
    styleUrls: ['./order-conditions.component.scss']
})
export class OrderConditionsComponent implements OnInit {
    @Input() parentForm: FormGroup;
    @Input() userSetting: UserSetting;
    @Input() paymentSettings: PaymentSetting;

    @Output() newPaymentMethodEmitter = new EventEmitter<PaymentMethod>();

    isOrderConditionOpened = true;
    deliveryAddress: any[] = [];
    billingAddress: any[] = [];
    invoiceId: number;

    cz = AffiliateEnum.CZ;
    sb = AffiliateEnum.SB;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isSb = AffiliateUtil.isSb(environment.affiliate);

    constructor() { }

    ngOnInit() {
        if (this.paymentSettings) {
            if (this.isCz) {
                this.paymentSettings.paymentMethods =
                    (this.paymentSettings.paymentMethods || []).filter(item => item.descCode !== PAYMENT_METHOD.EUR_PAYMENT);
            }
            this.filterAddress(this.paymentSettings);
            if (this.userSetting) {
                const invoiceType = this.getInvoiceType(this.paymentSettings.invoiceTypes, this.userSetting.invoiceId);
                this.invoiceId = this.getDefaultInvoiceTypeId(invoiceType);
            }
        }
    }

    updatePaymentMethod(method: PaymentMethod) {
        this.newPaymentMethodEmitter.emit(method);
    }

    private getInvoiceType(invoiceTypes, settingInvoiceId) {
        if (isEmpty(invoiceTypes)) {
            return null;
        }
        const invoiceType = invoiceTypes.find(type => type.id === settingInvoiceId);
        return invoiceType ? invoiceType :
            invoiceTypes.find(type => (type.id > INVOICE_TYPE_CODE.SINGLE_INVOICE && settingInvoiceId > INVOICE_TYPE_CODE.SINGLE_INVOICE));
    }

    private getDefaultInvoiceTypeId(userInvoiceType) {
        if (userInvoiceType && (userInvoiceType.invoiceType === ERP_INVOICE_TYPE.SINGLE_INVOICE.toString()
            || userInvoiceType.invoiceType === ERP_INVOICE_TYPE.SINGLE_INVOICE_WITH_CREDIT_SEPARATION.toString())) {
            return DEFAULT_INVOICE_TYPE.SINGLE_INVOICE;
        }
        return DEFAULT_INVOICE_TYPE.AGREEMENT_INVOICE;
    }

    private filterAddress(settings: PaymentSetting) {
        this.deliveryAddress = settings.addresses
            .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DELIVERY]
                || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);

        // remove default if more than one address type
        if (this.deliveryAddress.length > 1) {
            this.deliveryAddress = this.deliveryAddress.filter(item => {
                return item.addressType !== ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT];
            });
        }

        this.billingAddress = settings.billingAddresses
            .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.INVOICE]
                || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);
    }
}
