import { FinalCustomerSettingModel } from './final-customer-setting.model';

export class FinalCustomerModel {
    orgId: number;
    name: string;
    description: string;
    finalCustomerType: string;
    parentOrgId: number;
    addressInfo: string;
    contactInfo: string;
    hasInProgressOrders: boolean;
    customerProperties: FinalCustomerSettingModel = new FinalCustomerSettingModel();
    status?: string;

    constructor(finalCustomer?: any) {
        if (finalCustomer) {
            this.orgId = finalCustomer.orgId;
            this.name = finalCustomer.name;
            this.description = finalCustomer.description;
            this.finalCustomerType = finalCustomer.finalCustomerType;
            this.parentOrgId = finalCustomer.parentOrgId;
            this.addressInfo = finalCustomer.addressInfo;
            this.contactInfo = finalCustomer.contactInfo;
            this.hasInProgressOrders = finalCustomer.hasInProgressOrders;
            this.customerProperties = new FinalCustomerSettingModel(finalCustomer.customerProperties);
            this.status = finalCustomer.status;
        }
    }
}
