import { PriceDisplaySetting } from './price-display-setting.model';
import { CustomerSettingsOffer } from './customer-setting-offer.model';

export interface CustomerSettingRequest {
    priceDisplaySettings: PriceDisplaySetting[];
    netPriceView: boolean;
    allowNetPriceChanged: boolean;
    netPriceConfirm: boolean;
    showDiscount: boolean;
    viewBilling: boolean;
    allowViewBillingChanged: boolean;
    orgPropertyOffer: CustomerSettingsOffer;
    allowNetPriceConfirmChanged: boolean;
    allowDiscountChanged: boolean;
    notifier: any;
}

export class CustomerSetting {
    netPriceView: boolean;
    allowNetPriceChanged: boolean;
    netPriceConfirm: boolean;
    allowNetPriceConfirmChanged: boolean;
    viewBilling: boolean;
    allowViewBillingChanged: boolean;
    showDiscount: boolean;
    allowDiscountChanged: boolean;
    orgPropertyOffer: CustomerSettingsOffer;
    notifier: any;
    priceDisplaySettings: PriceDisplaySetting[] = [];
    wssShowNetPrice: boolean;

    constructor(customerSettings?: any) {
        if (customerSettings) {
            this.netPriceView = customerSettings.netPriceView;
            this.allowNetPriceChanged = customerSettings.allowNetPriceChanged;
            this.netPriceConfirm = customerSettings.netPriceConfirm;
            this.showDiscount = customerSettings.showDiscount;
            this.viewBilling = customerSettings.viewBilling;
            this.allowViewBillingChanged = customerSettings.allowViewBillingChanged;
            this.orgPropertyOffer = customerSettings.orgPropertyOffer ?
                new CustomerSettingsOffer(customerSettings.orgPropertyOffer) : new CustomerSettingsOffer();
            this.allowNetPriceChanged = true;
            this.allowViewBillingChanged = true;
            this.allowNetPriceConfirmChanged = this.netPriceView;
            this.allowDiscountChanged = this.netPriceView;
            this.priceDisplaySettings = customerSettings.priceDisplaySettings;
            this.wssShowNetPrice = customerSettings.wssShowNetPrice;
        }
    }
}
