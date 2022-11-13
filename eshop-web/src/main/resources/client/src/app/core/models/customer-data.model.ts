import { CustomerModel } from './customer.model';
import { CustomerAddressModel } from './customer-address.model';
import { CustomerBranchModel } from './customer-branch.model';

export class CustomerDataModel {
    recommendCustomers: {
        content: any[];
        totalElements: number
        last: boolean;
        first: boolean;
    };
    customer: CustomerModel;
    defaultAddress: CustomerAddressModel;
    deliveryAddress: CustomerAddressModel;
    invoiceAddress: CustomerAddressModel;
    admin: string;
    branches: CustomerBranchModel[];
    creditLimit: {
        alreadyUsedCredit: number;
        availableCredit: number;
    };
    isShopCustomer: boolean;
}
