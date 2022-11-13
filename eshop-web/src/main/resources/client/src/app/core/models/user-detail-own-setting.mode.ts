import { SagEditorLanguageModel, SagAvailDisplaySettingModel, ExternalPartSettings } from 'sag-common';
import { DeliveryProfileModel } from 'src/app/wholesaler/models/delivery-profile.model';

export class UserDetailOwnSetting {
    netPriceView?: boolean;
    showDiscount?: boolean;
    showGross?: boolean;
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
    salesAbsEnabled: boolean;
    externalUrls: any;
    priceDisplayChanged: boolean;
    brandPriorityAvailFilter: string;
    // old version of avail setting
    availIcon: boolean;
    detailAvailText: SagEditorLanguageModel[];
    dropShipmentAvailability: boolean;
    listAvailText: SagEditorLanguageModel[];

    unicatCatalogUri?: string;
    wssDeliveryProfile: DeliveryProfileModel;
    customerBrandFilterEnabled: boolean;
    salesBrandFilterEnabled: boolean;
    mouseOverFlyoutDelay: number;
    vatTypeDisplay: string;
    vatTypeDisplayConvert: any;
    enhancedUsedPartsReturnProcEnabled: boolean;
    couponModuleEnabled: boolean;
    availDisplaySettings: SagAvailDisplaySettingModel[];
    invoiceRequestAllowed: boolean;
    invoiceRequestEmail: string;
    disabledBrandPriorityAvailability: boolean;
    externalPartSettings: ExternalPartSettings;
}
