export class OfferCriteria {
    customerName = '';
    offerDate = '';
    offerNumber = '';
    price = '';
    remark = '';
    status = '';
    username = '';
    vehicleDesc = '';

    orderDescByCustomerName: boolean;
    orderDescByOfferDate: boolean;
    orderDescByOfferNumber: boolean;
    orderDescByPrice: boolean;
    orderDescByRemark: boolean;
    orderDescByStatus: boolean;
    orderDescByUsername: boolean;
    orderDescByVehicleDesc: boolean;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.customerName = data.customerName;
        this.offerDate = data.offerDate;
        this.offerNumber = data.offerNumber;
        this.price = data.price;
        this.remark = data.remark;
        this.status = data.status;
        this.username = data.username;
        this.vehicleDesc = data.vehicleDesc;
    }

    public resetSort() {
        this.orderDescByCustomerName = null;
        this.orderDescByOfferDate = null;
        this.orderDescByOfferNumber = null;
        this.orderDescByPrice = null;
        this.orderDescByRemark = null;
        this.orderDescByStatus = null;
        this.orderDescByUsername = null;
        this.orderDescByVehicleDesc = null;
    }
}
