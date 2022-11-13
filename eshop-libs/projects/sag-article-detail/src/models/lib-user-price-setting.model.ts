import { SagEditorLanguageModel, SagAvailDisplaySettingModel, ExternalPartSettings } from 'sag-common';
export class LibUserSetting {
    netPriceView: boolean;
    vatConfirm: boolean;
    showDiscount: boolean;
    showTyresDiscount: boolean;
    currentStateNetPriceView: boolean;
    currentStateVatConfirm: boolean;
    showTyresGrossPriceHeader: boolean;
    hasAvailabilityPermission: boolean;
    priceDisplayChanged: boolean;
    showGross: boolean;
    brandPriorityAvailFilter: string;

    // old avail settings
    availIcon: boolean;
    detailAvailText: SagEditorLanguageModel[] = null;
    dropShipmentAvailability: boolean;
    listAvailText: SagEditorLanguageModel[] = null;

    wholeSalerHasNetPrice: boolean;
    finalCustomerHasNetPrice: boolean;
    canViewNetPrice: boolean;
    customerBrandFilterEnabled: boolean;
    salesBrandFilterEnabled: boolean;
    isSalesOnBeHalf: boolean;
    showNetPriceEnabled: boolean;
    fcUserCanViewNetPrice: boolean;
    externalUrls: any;
    mouseOverFlyoutDelay: number;
    vatTypeDisplay: string;
    vatTypeDisplayConvert: any;
    availDisplaySettings: SagAvailDisplaySettingModel[];
    disabledBrandPriorityAvailability: boolean;

    externalPartSettings: ExternalPartSettings;
}
