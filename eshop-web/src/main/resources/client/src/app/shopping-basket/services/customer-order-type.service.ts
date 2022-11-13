import { Injectable } from '@angular/core';

import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { AbstractOrderType } from './abstract-order-type.service';
import { PAYMENT_METHOD } from '../../core/enums/shopping-basket.enum';
import { AbsSetting } from 'src/app/models/setting/abs-setting.model';
import { ShoppingBasketContextModel } from '../models/shopping-basket-context.model';

@Injectable()
export class CustomerOrderTypeService extends AbstractOrderType {

    isValidForABSOrderType(basketContext: ShoppingBasketContextModel) {
        const deliveryType = basketContext.deliveryType;
        const paymentMethod = basketContext.paymentMethod;
        return deliveryType && this.isPickDelivery(deliveryType)
            && (paymentMethod.descCode === PAYMENT_METHOD[PAYMENT_METHOD.CREDIT]
                || paymentMethod.descCode === PAYMENT_METHOD[PAYMENT_METHOD.CASH]);
    }

    protected isABSOrder(shoppingBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel, absSetting: AbsSetting): boolean {
        if (!absSetting.customerAbsEnabled) {
            return false;
        }
        const isAllItemsAvailable = shoppingBasket.isAllVailAvail();
        return isAllItemsAvailable && this.isValidForABSOrderType(basketContext);
    }
}
