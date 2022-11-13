import { ShoppingBasketContextModel } from './shopping-basket-context.model';
export class ShoppingOrderConditionContextModel {
    invoiceTypeCode: string;
    paymentMethod: string;
    deliveryTypeCode: string;
    collectiveDeliveryCode: string;
    pickupBranchId: string;
    billingAddressId: string;
    deliveryAddressId: string;

    constructor(context: ShoppingBasketContextModel) {
        this.invoiceTypeCode = context.invoiceType.descCode || null;
        this.paymentMethod = context.paymentMethod.descCode || null;
        this.deliveryTypeCode = context.deliveryType.descCode || null;
        this.collectiveDeliveryCode = context.collectionDelivery.descCode || null;
        this.pickupBranchId = context.pickupBranch.branchId || null;
        this.billingAddressId = context.billingAddress && context.billingAddress.id && context.billingAddress.id.toString() || null;
        this.deliveryAddressId = context.deliveryAddress && context.deliveryAddress.id && context.deliveryAddress.id.toString() || null;
    }
}
