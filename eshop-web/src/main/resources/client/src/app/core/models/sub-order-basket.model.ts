import { ShoppingBasketItemModel } from './shopping-basket-item.model';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';

export interface SubOrderBasketModel {
    finalOrder: OrderDashboardListItemModel;
    items: ShoppingBasketItemModel[];
    isOrigin: boolean;
}