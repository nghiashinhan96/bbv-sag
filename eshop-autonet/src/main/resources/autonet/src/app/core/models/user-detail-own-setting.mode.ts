import { ExternalPartSettings } from 'sag-common';

export class UserDetailOwnSetting {
    netPriceView?: boolean;
    showDiscount?: boolean;
    emailOrderConfirmation?: boolean;
    sessionTimeoutSeconds: number;
    affiliateEmail?: string;
    showTyresGrossPriceHeader: boolean;
    showTyresDiscount: boolean;
    gaTrackingCode?: string;
    dvseCatalogUri: string;
    dvseUserActive: boolean;
    vatRate: number;
    paymentMethodCode: string;
    viewBilling?: boolean;
    netPriceConfirm?: boolean;
    showHappyPoints?: boolean;
    currentStateNetPriceView: boolean;
    deliveryAddressId: string;
    billingAddressId: string;
    customerAbsEnabled: boolean;
    demoCustomer: boolean;
    mouseOverFlyoutDelay: number;
    customerBrandFilterEnabled?: boolean;
    salesBrandFilterEnabled?: boolean;
    externalPartSettings: ExternalPartSettings;
}
