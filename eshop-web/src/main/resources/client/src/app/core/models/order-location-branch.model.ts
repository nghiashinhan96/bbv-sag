export class OrderLocationBranchModel {
    branchId: any;
    locationType: string;
    paymentMethodAllowed: string;
    branchName: string;

    constructor (data = null) {
        if (data) {
            this.branchId = data.branchId;
            this.locationType = data.locationType;
            this.paymentMethodAllowed = data.paymentMethodAllowed;
            this.branchName = data.branchName;
        }
    }
}