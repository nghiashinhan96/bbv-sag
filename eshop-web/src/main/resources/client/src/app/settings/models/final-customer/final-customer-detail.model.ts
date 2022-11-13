import { DeliveryProfileModel } from 'src/app/wholesaler/models/delivery-profile.model';
import { PermissionConfigurationModel } from '../permission-configuration.model';

export class FinalCustomerDetail {
    collectionShortname: string;
    customerType: string;
    isActive: boolean;
    showNetPrice: boolean;
    customerNr: number;
    customerName: string;
    salutation: string;
    surName: string;
    firstName: string;
    street: string;
    addressOne: string;
    addressTwo: string;
    poBox: string;
    postcode: string;
    place: string;
    phone: string;
    fax: string;
    email: string;
    perms: Array<PermissionConfigurationModel>;
    wssDeliveryProfileDto: DeliveryProfileModel = null;
    deliveryId: number;
    wssMarginGroup: number;

    constructor(data?: any | FinalCustomerDetail) {
        if (!data) {
            return;
        }

        this.collectionShortname = data.collectionShortname;
        this.customerType = data.customerType;
        this.isActive = data.isActive;
        this.showNetPrice = data.showNetPrice;
        this.customerNr = data.customerNr;
        this.customerName = data.customerName;
        this.salutation = data.salutation;
        this.surName = data.surName;
        this.firstName = data.firstName;
        this.street = data.street;
        this.addressOne = data.addressOne;
        this.addressTwo = data.addressTwo;
        this.poBox = data.poBox;
        this.postcode = data.postcode;
        this.place = data.place;
        this.phone = data.phone;
        this.fax = data.fax;
        this.email = data.email;
        this.perms = (data.perms || []).map(item => new PermissionConfigurationModel(item));
        this.wssDeliveryProfileDto = data.wssDeliveryProfileDto ? new DeliveryProfileModel(data.wssDeliveryProfileDto) : null;
        this.deliveryId = data.deliveryId;
        this.wssMarginGroup = data.wssMarginGroup;
    }
}
