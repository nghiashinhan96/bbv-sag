import { FinalCustomerSetting } from './final-customer-setting.model';

export enum FinalCustomerType {
    ONLINE,
    PASSANT
}

export const FinalCustomerTypes = {
    ONLINE: 'FINAL_CUSTOMER.TYPE.ONLINE',
    PASSANT: 'FINAL_CUSTOMER.TYPE.PASSANT'
};

export class FinalCustomer {
    orgId: number;
    name: string;
    description: string;
    finalCustomerType: string;
    parentOrgId: number;
    addressInfo: string;
    contactInfo: string;
    hasInProgressOrders: boolean;
    customerProperties: FinalCustomerSetting;

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.orgId = data.orgId;
        this.name = data.name;
        this.description = data.description;
        this.finalCustomerType = data.finalCustomerType;
        this.parentOrgId = data.parentOrgId;
        this.addressInfo = data.addressInfo;
        this.contactInfo = data.contactInfo;
        this.hasInProgressOrders = data.hasInProgressOrders;
        this.customerProperties = data.customerProperties;
    }

    getCustomerTypeName() {
        return FinalCustomerTypes[this.finalCustomerType];
    }

    isOnlineCustomer() {
        return FinalCustomerType[FinalCustomerType.ONLINE] === this.finalCustomerType;
    }
}
