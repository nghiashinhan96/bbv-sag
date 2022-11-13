import { ShoppingOrderTypeModel } from '../models/shopping-order-type.model';
import { AbsSetting } from 'src/app/models/setting/abs-setting.model';
import { ShoppingBasketContextModel } from '../models/shopping-basket-context.model';
import { ORDER_TYPE, DELIVERY_TYPE } from '../../core/enums/shopping-basket.enum';
import { DeliveryType } from 'src/app/core/models/delivery-type.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';

export abstract class AbstractOrderType implements ShoppingOrderTypeModel {
    public get(shoppingBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel, absSetting: AbsSetting): ORDER_TYPE {
        if (shoppingBasket.isKSOAUTOrder()) {
            return ORDER_TYPE.KSO_AUT;
        }

        if (this.isCounterOrder(basketContext, shoppingBasket.isAllVailAvail())) {
            return ORDER_TYPE.COUNTER;
        }

        if (this.isABSOrder(shoppingBasket, basketContext, absSetting)) {
            return ORDER_TYPE.ABS;
        }

        if (this.isSTDOrder(basketContext)) {
            return ORDER_TYPE.STD;
        }

        return ORDER_TYPE.ORDER;
    }

    private isCounterOrder(basketContext: ShoppingBasketContextModel, isAllItemsAvailable: boolean): boolean {
        return basketContext.showKSLMode
            && basketContext.orderType === ORDER_TYPE[ORDER_TYPE.COUNTER]
            && isAllItemsAvailable;
    }

    private isSTDOrder(basketContext: ShoppingBasketContextModel): boolean {
        return this.isTourDelivery(basketContext.deliveryType);
    }

    private isTourDelivery(deliveryType: DeliveryType): boolean {
        return deliveryType && deliveryType.descCode === DELIVERY_TYPE[DELIVERY_TYPE.TOUR];
    }

    protected isPickDelivery(deliveryType: DeliveryType): boolean {
        return deliveryType && deliveryType.descCode === DELIVERY_TYPE[DELIVERY_TYPE.PICKUP];
    }

    protected abstract isValidForABSOrderType(basketContext: ShoppingBasketContextModel): boolean;

    protected abstract isABSOrder(shoppingBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel, absSetting: AbsSetting): boolean;

}
