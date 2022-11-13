import { ERP_INVOICE_TYPE, INVOICE_TYPE_CODE } from 'src/app/settings/enums/invoice/invoice.enum';

export class FeedbackUserSetting {
    viewBilling: boolean;
    netPriceView: boolean;
    netPriceConfirm: boolean;
    showDiscount: boolean;
    classicCategoryView: boolean;
    emailNotificationOrder: boolean;
    invoiceType: string;
    paymentMethod: string;
    deliveryType: string;
    collectionDelivery: string;
    deliveryAddress: string;
    billingAddress: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.viewBilling = data.viewBilling;
        this.netPriceView = data.netPriceView;
        this.netPriceConfirm = data.netPriceConfirm;
        this.showDiscount = data.showDiscount;
        this.classicCategoryView = data.classicCategoryView;
        this.emailNotificationOrder = data.emailNotificationOrder;
        this.invoiceType = data.invoiceType;
        this.paymentMethod = data.paymentMethod;
        this.deliveryType = data.deliveryType;
        this.collectionDelivery = data.collectionDelivery;
        this.deliveryAddress = data.deliveryAddress;
        this.billingAddress = data.billingAddress;
    }

    public fromSettings(userSetting, paymentSetting) {
        if (!paymentSetting || !userSetting) {
            return this;
        }

        this.viewBilling = userSetting.viewBilling;
        this.netPriceView = userSetting.netPriceView;
        this.netPriceConfirm = userSetting.netPriceConfirm;
        this.showDiscount = userSetting.showDiscount;
        this.classicCategoryView = userSetting.classicCategoryView;
        this.emailNotificationOrder = userSetting.emailNotificationOrder;

        if (paymentSetting.invoiceTypes && userSetting.invoiceId) {
            this.getUserInvoiceType(paymentSetting.invoiceTypes, userSetting.invoiceId);
        }
        if (paymentSetting.paymentMethods && userSetting.paymentId) {
            this.getPaymentMethod(paymentSetting.paymentMethods, userSetting.paymentId);
        }
        if (paymentSetting.deliveryTypes && userSetting.deliveryId) {
            this.getDeliveryType(paymentSetting.deliveryTypes, userSetting.deliveryId);
        }
        if (paymentSetting.collectiveTypes && userSetting.collectiveDelivery) {
            this.getCollectionDelivery(paymentSetting.collectiveTypes, userSetting.collectiveDelivery);
        }
        if (paymentSetting.addresses && userSetting.deliveryAddressId) {
            this.getDeliveryAddress(paymentSetting.addresses, userSetting.deliveryAddressId);
        }
        if (paymentSetting.addresses && userSetting.billingAddressId) {
            this.getBillingAddress(paymentSetting.addresses, userSetting.billingAddressId);
        }
        return this;
    }

    private getUserInvoiceType(invoiceTypes, invoiceId) {
        let invoiceType = invoiceTypes.find(type => type.id === invoiceId);
        if (!invoiceType) {
            invoiceType = invoiceTypes
                .find(type => (type.id > INVOICE_TYPE_CODE.SINGLE_INVOICE && invoiceId > INVOICE_TYPE_CODE.SINGLE_INVOICE));
        }
        this.invoiceType = (
            invoiceType.invoiceType === ERP_INVOICE_TYPE.SINGLE_INVOICE.toString() ||
            invoiceType.invoiceType === ERP_INVOICE_TYPE.SINGLE_INVOICE_WITH_CREDIT_SEPARATION.toString()
        ) ? 'SINGLE_INVOICE' : 'AGREEMENT_INVOICE';
    }

    private getPaymentMethod(paymentMethods, paymentId) {
        const paymentMethod = paymentMethods.find(x => x.id === paymentId);
        if (paymentMethod) {
            this.paymentMethod = paymentMethod.descCode;
        }
    }

    private getDeliveryType(deliveryTypes, deliveryId) {
        const deliveryType = deliveryTypes.find(x => x.id === deliveryId);
        if (deliveryType) {
            this.deliveryType = deliveryType.descCode;
        }
    }

    private getCollectionDelivery(collectiveDeliveries, collectiveDeliveryId) {
        const collectionDelivery = collectiveDeliveries.find(x => x.id === collectiveDeliveryId);
        if (collectionDelivery) {
            this.collectionDelivery = collectionDelivery.descCode;
        }
    }

    private getDeliveryAddress(addresses, deliveryAddressId) {
        const addr = addresses.find(x => x.id === deliveryAddressId);
        if (addr) {
            this.deliveryAddress = this.getFullAddress(addr);
        }
    }

    private getBillingAddress(addresses, billingAddressId) {
        const addr = addresses.find(x => x.id === billingAddressId);
        if (addr) {
            this.billingAddress = this.getFullAddress(addr);
        }
    }

    private getFullAddress(addr): string {
        return [
            addr.companyName,
            addr.street,
            addr.postCode,
            addr.city,
            addr.country
        ].filter(item => item).join(', ');
    }
}
