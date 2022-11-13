import { OrderLocationModel } from './order-location.model';
import { AllocationType } from './allocation-type.model';
import { PaymentMethod } from './payment-method.model';
import { UserAddress } from './user-address.model';
import { CollectiveType } from './collective-type.model';
import { DeliveryType } from './delivery-type.model';
import { InvoiceType } from './invoice-type.model';

export class PaymentSetting {
    allocationTypes: AllocationType[];
    paymentMethods: PaymentMethod[];
    addresses: UserAddress[];
    billingAddresses: UserAddress[];
    collectiveTypes: CollectiveType[];
    deliveryTypes: DeliveryType[];
    invoiceTypes: InvoiceType[];
    orderLocations: OrderLocationModel[];

    constructor(data?: any) {
        if (data) {
            this.allocationTypes = data.allocationTypes || [];
            this.paymentMethods = data.paymentMethods || [];
            this.addresses = (data.addresses || []).map((address: any) => new UserAddress(address));
            this.billingAddresses = (data.billingAddresses || []).map((address: any) => new UserAddress(address));
            this.collectiveTypes = data.collectiveTypes || [];
            this.deliveryTypes = data.deliveryTypes || [];
            this.invoiceTypes = data.invoiceTypes || [];
            this.orderLocations = (data.orderLocations || []).map((location: any) => new OrderLocationModel(location));
        }
    }
}
