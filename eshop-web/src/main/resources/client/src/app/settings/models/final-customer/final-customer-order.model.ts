import { FinalCustomerOrderItem } from './final-customer-order-item.model';

export class FinalCustomerOrder {
    id: number;
    items: FinalCustomerOrderItem[] = [];
    orderDate: string;
    reference: string;
    branchRemark: string;
    customerInfo: string;

    constructor(data?) {
        if (data) {
            this.id = data.id;
            this.orderDate = data.orderDate;
            this.reference = data.reference;
            this.branchRemark = data.branchRemark;
            this.customerInfo = data.customerInfo;

            if (data.items) {
                data.items.forEach(element => {
                    this.items.push(new FinalCustomerOrderItem(element));
                });
            }
        }
    }
}
