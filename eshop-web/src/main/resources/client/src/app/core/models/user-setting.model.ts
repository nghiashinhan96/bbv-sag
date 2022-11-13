import { PriceSetting } from './price-setting.model';

export interface UpdateUserSetting {
    allocationId: number;
    paymentId: number;
    invoiceId: number;
    deliveryId: number;
    collectiveDelivery: number;
    viewBilling: boolean;
    netPriceView: boolean;
    netPriceConfirm: boolean;
    deliveryAddressId: string;
    billingAddressId: string;
    emailNotificationOrder: boolean;
    classicCategoryView: boolean;
}

export class UserSetting {
    id: number;
    allocationId: number;
    paymentId: number;
    invoiceId: number;
    deliveryId: number;
    collectiveDelivery: number;

    netPriceView: boolean;
    allowNetPriceChanged: boolean;
    netPriceConfirm: boolean;
    allowNetPriceConfirmChanged: boolean;
    viewBilling: boolean;
    allowViewBillingChanged: boolean;
    showDiscount: boolean;
    allowDiscountChanged: boolean;
    classicCategoryView: boolean;
    billingAddressId: string;
    deliveryAddressId: string;
    emailNotificationOrder: boolean;

    priceSettings: PriceSetting;
    singleSelectMode?: boolean;
    currentStateSingleSelectMode?: boolean;

    constructor(userSettings) {
        this.id = userSettings.id;
        this.allocationId = userSettings.allocationId;
        this.paymentId = userSettings.paymentId;
        this.invoiceId = userSettings.invoiceId;
        this.deliveryId = userSettings.deliveryId;
        this.collectiveDelivery = userSettings.collectiveDelivery;

        this.netPriceView = userSettings.netPriceView;
        this.netPriceConfirm = userSettings.netPriceConfirm;
        this.viewBilling = userSettings.viewBilling;
        this.showDiscount = userSettings.showDiscount;

        this.billingAddressId = userSettings.billingAddressId;
        this.deliveryAddressId = userSettings.deliveryAddressId;
        this.emailNotificationOrder = userSettings.emailNotificationOrder;
        this.classicCategoryView = userSettings.classicCategoryView;
        this.priceSettings = userSettings.priceSettings;

        if (this.priceSettings) {
            this.allowViewBillingChanged = this.priceSettings.allowViewBillingChanged;
            this.allowNetPriceChanged = this.priceSettings.allowNetPriceChanged;
            this.allowNetPriceConfirmChanged = this.priceSettings.allowNetPriceConfirmChanged && this.netPriceView;
            this.allowDiscountChanged = this.priceSettings.allowDiscountChanged && this.netPriceView;
        }

        this.singleSelectMode = userSettings.singleSelectMode;
    }
}
