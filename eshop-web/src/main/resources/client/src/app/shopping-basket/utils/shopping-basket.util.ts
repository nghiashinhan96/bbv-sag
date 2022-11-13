import { CurrencyUtil } from 'sag-currency';

export class ShoppingBasketUtil {
    public static refreshTotalGrossInclVat(basketItems) {
        const price = basketItems.newTotalWithVat;
        return CurrencyUtil.roundHalfEvenTo2digits(price);
    }

    public static groupBasketItemsByVehicleId(items) {
        return items.sort((item1, item2) => {
            if (item1.vehicleId < item2.vehicleId) {
                return -1;
            }
            if (item1.vehicleId > item2.vehicleId) {
                return 1;
            }
            return 0;
        });
    }
}
