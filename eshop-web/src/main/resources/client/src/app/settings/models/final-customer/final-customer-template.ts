import { UserProfileSalutation } from '../user-profile/user-profile-salutation.model';
import { PermissionConfigurationModel } from '../permission-configuration.model';
import { FinalCustomerDetail } from './final-customer-detail.model';
import { DeliveryType } from './final-customer-delivery-types.model';


export class FinalCustomerTemplate {
    customerTypes: Array<string>;
    salutations: Array<UserProfileSalutation>;
    permissions: Array<PermissionConfigurationModel>;
    deliveryTypes: Array<DeliveryType>;
    selectedFinalCustomer: FinalCustomerDetail;
    showNetPrice: boolean;
    defaultDeliveryType: number;
    showNetPriceEnabled: boolean;
    marginGroups: any[] = [];

    constructor(data?: any | FinalCustomerTemplate) {
        if (!data) {
            return;
        }

        this.customerTypes = data.customerTypes;
        this.salutations = (data.salutations || []).map(item => new UserProfileSalutation(item));
        this.permissions = (data.permissions || []).map(item => new PermissionConfigurationModel(item));
        this.deliveryTypes = (data.deliveryTypes || []).map(item => new DeliveryType(item));
        this.selectedFinalCustomer = data.selectedFinalCustomer ? new FinalCustomerDetail(data.selectedFinalCustomer) : null;
        this.showNetPrice = data.showNetPrice;
        this.defaultDeliveryType = data.defaultDeliveryType;
        this.showNetPriceEnabled = data.showNetPriceEnabled;
        this.marginGroups = data.marginGroups || [];
    }
}
