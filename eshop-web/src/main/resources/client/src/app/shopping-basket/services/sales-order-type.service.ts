import { Injectable } from '@angular/core';
import { ShoppingBasketContextModel } from '../models/shopping-basket-context.model';
import { AbstractOrderType } from './abstract-order-type.service';
import { AbsSetting } from 'src/app/models/setting/abs-setting.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';

@Injectable()
export class SalesOrderTypeService extends AbstractOrderType {

    isValidForABSOrderType(basketContext: ShoppingBasketContextModel) {
        const deliveryType = basketContext.deliveryType;
        // Not check payment method: https://app.assembla.com/spaces/sag-eshop/tickets/4004/details?comment=1590974171
        return deliveryType && this.isPickDelivery(deliveryType);
    }

    protected isABSOrder(shoppingBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel, absSetting: AbsSetting): boolean {
        if (!absSetting.salesAbsEnabled) {
            return false;
        }
        const isAllItemsAvailable = shoppingBasket.isAllVailAvail();
        return isAllItemsAvailable && this.isValidForABSOrderType(basketContext);
    }
}
