import { ShoppingBasketContextModel } from './shopping-basket-context.model';
import { AVAILABILITY_INFO } from 'sag-article-detail';

export class ShoppingConditionHeaderModel {
    isTourMode = false;
    isPickupMode = false;

    partialOrCollective: string;
    constructor(basketContext?: ShoppingBasketContextModel) {
        if (!basketContext) {
            return;
        }
        this.partialOrCollective = basketContext && basketContext.collectionDelivery && basketContext.collectionDelivery.descCode;
        const sendMethodCode = basketContext && basketContext.deliveryType && basketContext.deliveryType.descCode;
        if (sendMethodCode === AVAILABILITY_INFO.PICKUP) {
            this.isTourMode = false;
            this.isPickupMode = true;
        } else if (sendMethodCode === AVAILABILITY_INFO.TOUR) {
            this.isTourMode = true;
            this.isPickupMode = false;
        }
    }
}
