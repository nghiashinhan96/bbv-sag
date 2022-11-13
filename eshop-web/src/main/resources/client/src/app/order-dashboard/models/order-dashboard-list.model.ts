import { OrderDashboardListItemModel } from './order-dashboard-list-item.model';

export class OrderDashboardListModel {
    orderList: Array<OrderDashboardListItemModel> = [];
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    size: number;

    totalElements: number;
    totalPages: number;
    isLoaded: boolean;
    pageable: any;
    sort: any;

    constructor(data?) {
        this.first = data.first;
        this.last = data.last;
        this.number = data.number;
        this.numberOfElements = data.numberOfElements;
        this.size = data.size;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalElements;
        this.isLoaded = data.isLoaded;

        this.orderList = (data.content || []).map(item => new OrderDashboardListItemModel(item));

        this.pageable = data.pageable;
        this.sort = data.sort;
    }
}
