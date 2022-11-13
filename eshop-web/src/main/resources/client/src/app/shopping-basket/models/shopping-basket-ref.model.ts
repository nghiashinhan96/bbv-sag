export class ShoppingBasketRefModel {
    id: string;
    reference: string;
    branchRemark: string;
    items: {
        vehicleId: string;
        articleId: string;
        reference: string;
    }[] = [];

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.reference = data.reference;
            this.branchRemark = data.branchRemark;
            this.items = data.items;
        }
    }
}
