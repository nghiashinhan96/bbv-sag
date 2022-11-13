import { UserDetailOwnSetting } from './user-detail-own-setting.mode';
import { UserPermission } from './user-permission.model';
import { UserAddress } from './user-address.model';
import { CustomerModel } from './customer.model';
import { ADDRESS_TYPE } from 'src/app/core/enums/shopping-basket.enum';

export class UserDetail {
    id: number;
    salesId: number;
    username?: string;
    companyName: string;
    lastName?: string;
    firstName?: string;
    language?: string;
    email?: string;
    settings: UserDetailOwnSetting;
    userAdminRole?: boolean; // should be provided by user service
    roleName?: string;
    salesUser?: boolean;
    normalUser?: boolean;
    hourlyRate: number;
    vatConfirm?: boolean;
    custNr?: string;
    defaultBranchName?: string; // in the settings
    defaultBranchId?: string; // in the settings
    cachedUserId: string;
    permissions: UserPermission[];
    customer: CustomerModel;
    deliveryAddressId: string;
    billingAddressId: string;
    affiliateShortName: string;
    hasAvailabilityPermission: boolean;
    hasWholesalerPermission: boolean;
    hasDmsPermission: boolean;
    collectionShortname: string;
    finalCustomer: any;
    isFinalUserRole: boolean;
    finalUserAdminRole: boolean;
    isSalesOnBeHalf: boolean;
    hasWholesalerAdminPermission: boolean;
    addresses: UserAddress[];
    defaultAddress: UserAddress;
    deliveryAddress?: UserAddress;
    billingAddress?: UserAddress;
    invoiceAddress?: UserAddress;
    // custome field for KLS mode
    isShopCustomer?: boolean;
    priceDisplayChanged: boolean;
    wholeSalerHasNetPrice: boolean;
    showNetPriceEnabled: boolean;
    finalCustomerHasNetPrice: boolean;
    legalAccepted: boolean;
    orderLocations?: any[];

    constructor (data: any) {
        if (data) {
            this.id = data.id;
            this.salesId = data.salesId;
            this.username = data.username;
            this.companyName = data.companyName;
            this.lastName = data.lastName;
            this.firstName = data.firstName;
            this.language = data.language;
            this.email = data.email;
            this.settings = data.settings;
            this.userAdminRole = data.userAdminRole;
            this.roleName = data.roleName;
            this.salesUser = data.salesUser;
            this.normalUser = data.normalUser;
            this.hourlyRate = data.hourlyRate;
            this.vatConfirm = data.vatConfirm;
            this.custNr = data.custNr;
            this.defaultBranchName = data.defaultBranchName;
            this.defaultBranchId = data.defaultBranchId;
            this.cachedUserId = data.cachedUserId;
            this.permissions = (data.permissions || []).map((permission: any) => new UserPermission(permission));
            this.customer = data.customer;
            this.deliveryAddressId = data.deliveryAddressId;
            this.billingAddressId = data.billingAddressId;
            this.affiliateShortName = data.affiliateShortName;
            this.hasAvailabilityPermission = data.hasAvailabilityPermission;
            this.hasWholesalerPermission = data.hasWholesalerPermission;
            this.hasDmsPermission = data.hasDmsPermission;
            this.collectionShortname = data.collectionShortname;
            this.finalCustomer = data.finalCustomer;
            this.isFinalUserRole = data.isFinalUserRole;
            this.finalUserAdminRole = data.finalUserAdminRole;
            this.isSalesOnBeHalf = data.isSalesOnBeHalf;
            this.hasWholesalerAdminPermission = data.hasWholesalerPermission && data.userAdminRole;

            this.addresses = (data.addresses || []).map((address: UserAddress) => {
                if (address.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]) {
                    this.defaultAddress = new UserAddress(address);
                }
                if (address.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DELIVERY]) {
                    this.deliveryAddress = new UserAddress(address);
                }
                return new UserAddress(address);
            });
            if (data.invoiceAddress) {
                this.invoiceAddress = new UserAddress(data.invoiceAddress);
            }
            this.isShopCustomer = data.isShopCustomer;
            this.priceDisplayChanged = data.priceDisplayChanged;
            this.wholeSalerHasNetPrice = data.wholeSalerHasNetPrice;
            this.showNetPriceEnabled = data.showNetPriceEnabled;
            this.finalCustomerHasNetPrice = data.finalCustomerHasNetPrice;
            this.legalAccepted = data.legalAccepted;
            this.orderLocations = data.orderLocations;
        }
    }
}
