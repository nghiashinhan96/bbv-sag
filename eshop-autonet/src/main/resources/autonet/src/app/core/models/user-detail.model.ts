import { UserDetailOwnSetting } from './user-detail-own-setting.mode';

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
    permissions: any;
    customer: any;
    deliveryAddressId: string;
    billingAddressId: string;
    affiliateShortName: string;
    hasAvailabilityPermission: boolean;
    hasWholesalerPermission: boolean;
    collectionShortname: string;
    finalCustomer: any;
    isFinalUserRole: boolean;
    finalUserAdminRole: boolean;
    userCode?: string;
    customerName?: string;
    constructor(data: any) {
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
            this.permissions = data.permissions;
            this.customer = data.customer;
            this.deliveryAddressId = data.deliveryAddressId;
            this.billingAddressId = data.billingAddressId;
            this.affiliateShortName = data.affiliateShortName;
            this.hasAvailabilityPermission = data.hasAvailabilityPermission;
            this.hasWholesalerPermission = data.hasWholesalerPermission;
            this.collectionShortname = data.collectionShortname;
            this.finalCustomer = data.finalCustomer;
            this.isFinalUserRole = data.isFinalUserRole;
            this.finalUserAdminRole = data.finalUserAdminRole;
        }
    }
}
