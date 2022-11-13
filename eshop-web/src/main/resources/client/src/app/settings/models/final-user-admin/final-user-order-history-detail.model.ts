export class FinalUserOrderHistoryDetail {
    address: string;
    articleDesc: string;
    companyName: string;
    customerNumber: string;
    id: number;
    orderDate: string;
    orgId: number;
    postCode: string;
    totalGrossPrice: number;
    username: string;
    vehicleDescs: string;
    reference: string;
    positionReferences: string;
    status: string;

    constructor(data?: any) {
        if (data) {
            this.address = data.address;
            this.articleDesc = data.articleDesc;
            this.companyName = data.companyName;
            this.customerNumber = data.customerNumber;
            this.id = data.id;
            this.orderDate = data.orderDate;
            this.orgId = data.orgId;
            this.postCode = data.postCode;
            this.totalGrossPrice = data.totalGrossPrice;
            this.username = data.username;
            this.vehicleDescs = data.vehicleDescs;
            this.reference = data.reference;
            this.positionReferences = data.positionReferences;
            this.status = data.status;
        }
    }
}
