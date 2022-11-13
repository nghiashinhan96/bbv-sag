import { CustomerSettingDetailModel } from './customer-setting-detail.model';

export class CustomerSetting {
    allocation = [];
    collectiveDelivery = [];
    deliveryTypes = [];
    invoiceTypes = [];
    paymentMethods = [];
    customerSettingsBODto = new CustomerSettingDetailModel();

    constructor(json?: CustomerSetting) {
        if (!json) {
            return;
        }
        this.allocation = json.allocation;
        this.collectiveDelivery = json.collectiveDelivery;
        this.deliveryTypes = json.deliveryTypes;
        this.invoiceTypes = json.invoiceTypes;
        this.paymentMethods = json.paymentMethods;
        this.customerSettingsBODto = new CustomerSettingDetailModel(json.customerSettingsBODto);
    }

    get dto() {
        return {
            allocation: this.allocation,
            collectiveDelivery: this.collectiveDelivery,
            deliveryTypes: this.deliveryTypes,
            invoiceTypes: this.invoiceTypes,
            paymentMethods: this.paymentMethods,
            customerSettingsBODto: this.customerSettingsBODto && this.customerSettingsBODto.dto
        };
    }
}
