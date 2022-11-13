export class OrderLocationConditionFormCtrlModel {
    paymentId: number;
    deliveryId: number;
    branchId: string = '';
    deliveryAddressId: number;
    courierServiceCode: string = '';
    defaultBranchId: string = '';
    referenceTextByLocation: string = '';

    constructor(data = null) {
        if(data) {
            this.paymentId = data.paymentId;
            this.deliveryId = data.deliveryId;
            this.branchId = data.branchId;
            this.deliveryAddressId = data.deliveryAddressId;
            this.courierServiceCode = data.courierServiceCode;
            this.defaultBranchId = data.defaultBranchId;
            this.referenceTextByLocation = data.referenceTextByLocation;
        }
    }
}