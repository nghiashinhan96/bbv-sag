import { ShoppingBasketModel } from "../models/shopping-basket.model";

export class OrderingUtil {
    public static preventOrderingWhenHasItemNotAllowToAddToShoppingCart(basketObj: ShoppingBasketModel) {
        let hasItemNotAllowToAddToShoppingCart = false;
        if (basketObj && basketObj.items && basketObj.items.length && basketObj.items.length > 0) {
            const itemNotAllowToAddToShoppingCart = basketObj.items.find(item => {
                if (item.articleItem && item.articleItem.allowedAddToShoppingCart === false) {
                  return true;
                }
                return false;
            });
            if (itemNotAllowToAddToShoppingCart) {
                hasItemNotAllowToAddToShoppingCart = true;
            }
        }
        return hasItemNotAllowToAddToShoppingCart;
    }
}
