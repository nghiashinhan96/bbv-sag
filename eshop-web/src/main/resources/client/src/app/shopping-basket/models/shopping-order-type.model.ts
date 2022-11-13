import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { ORDER_TYPE } from '../../core/enums/shopping-basket.enum';
import { AbsSetting } from 'src/app/models/setting/abs-setting.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';

export interface ShoppingOrderTypeModel {
    get(shoppingBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel, absSetting: AbsSetting): ORDER_TYPE;
}
