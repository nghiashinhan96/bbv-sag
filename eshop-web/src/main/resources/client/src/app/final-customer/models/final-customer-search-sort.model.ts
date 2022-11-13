export class FinalCustomerSearchSort {
    orderDescByName = false;
    constructor(data?) {
        if (data) {
            this.orderDescByName = data.direction === 'asc';
        }
    }
}
