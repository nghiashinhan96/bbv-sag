import { ShoppingBasketContextModel } from '../shopping-basket-context.model';
import { DELIVERY_TYPE } from 'src/app/core/enums/shopping-basket.enum';
export class SerbiaShoppingOrderConditionContextModel {
    invoiceTypeCode: string;
    paymentMethod: string;
    deliveryTypeCode: string;
    collectiveDeliveryCode: string;
    pickupBranchId: string;
    billingAddressId: string;
    deliveryAddressId: string;

    location: object;
    referenceTextByLocation: string;
    messageToBranch: string;
    courier: object;

    constructor(context: ShoppingBasketContextModel) {
        this.invoiceTypeCode = context.invoiceType.descCode || null;
        this.paymentMethod = context.paymentMethod.descCode || null;
        this.deliveryTypeCode = context.deliveryType.descCode || null;
        this.collectiveDeliveryCode = null;
        this.billingAddressId = context.billingAddress && context.billingAddress.id && context.billingAddress.id.toString() || null;
        this.deliveryAddressId = context.deliveryAddress && context.deliveryAddress.id && context.deliveryAddress.id.toString() || null;
        this.referenceTextByLocation = context.referenceTextByLocation || null;
        this.messageToBranch = context.messageToBranch || null;
        this.pickupBranchId = context.pickupLocation && context.pickupLocation.branchId || null;
        this.courier = (context.deliveryType.descCode === DELIVERY_TYPE.COURIER && context.courierService) || null;
        this.location = context.location || null;
    }
}
