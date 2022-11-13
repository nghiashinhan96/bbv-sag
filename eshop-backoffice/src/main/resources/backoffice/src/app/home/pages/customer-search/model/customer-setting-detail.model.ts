import { WSS_DELIVERY_TYPE } from 'src/app/core/conts/app.constant';

export class CustomerSettingDetailModel {
    allocationId: number;
    collections: {
        name: string,
        collectionShortName: string
    }[] = [];
    collectionShortName: string;
    collectiveDelivery: number;
    deliveryId: number;
    emailNotificationOrder: boolean;
    hasPartnerprogramView: boolean;
    id: number;
    normautoDisplay: boolean;
    orgId: number;
    paymentId: number;
    perms: any[] = [];
    sessionTimeoutMinutes: number = null;
    showOciVat: boolean;
    isShowOciVatEditable: boolean;
    demoCustomer: boolean;
    netPriceView: boolean;
    showDiscount: boolean;
    wssDeliveryId: number;

    constructor(json?: CustomerSettingDetailModel | any) {
        if (!json) {
            return;
        }
        this.allocationId = json.allocationId;
        this.collections = json.collections;
        this.collectionShortName = json.collectionShortName;
        this.collectiveDelivery = json.collectiveDelivery;
        this.deliveryId = json.deliveryId;
        this.emailNotificationOrder = json.emailNotificationOrder;
        this.hasPartnerprogramView = json.hasPartnerprogramView;
        this.demoCustomer = json.demoCustomer;
        this.id = json.id;
        this.normautoDisplay = json.normautoDisplay;
        this.orgId = json.orgId;
        this.paymentId = json.paymentId;
        this.perms = json.perms;
        const sessionTimeoutSeconds = Number(json.sessionTimeoutMinutes || json.sessionTimeoutSeconds);
        if (!Number.isNaN(sessionTimeoutSeconds)) {
            this.sessionTimeoutMinutes = Math.floor(sessionTimeoutSeconds / 60);
        }
        this.showOciVat = json.showOciVat;
        this.netPriceView = json.netPriceView;
        this.showDiscount = json.showDiscount;
        this.wssDeliveryId = json.wssDeliveryId;
    }

    get dto() {
        const sessionTimeoutMinutes = Number(this.sessionTimeoutMinutes);
        let sessionTimeoutSeconds = null;
        if (!Number.isNaN(sessionTimeoutMinutes)) {
            sessionTimeoutSeconds = sessionTimeoutMinutes * 60;
        }
        return {
            allocationId: this.allocationId,
            collectionShortName: this.collectionShortName,
            collectiveDelivery: this.collectiveDelivery,
            deliveryId: this.deliveryId,
            emailNotificationOrder: this.emailNotificationOrder,
            hasPartnerprogramView: this.hasPartnerprogramView,
            demoCustomer: this.demoCustomer,
            id: this.id,
            normautoDisplay: this.normautoDisplay,
            orgId: this.orgId,
            paymentId: this.paymentId,
            perms: this.perms,
            sessionTimeoutSeconds,
            showOciVat: this.showOciVat,
            netPriceView: this.netPriceView,
            showDiscount: this.showDiscount,
            wssDeliveryId: this.wssDeliveryId
        };
    }
}
