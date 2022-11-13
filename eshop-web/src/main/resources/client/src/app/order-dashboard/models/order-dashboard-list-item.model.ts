export class OrderDashboardListItemModel {
    id: number;
    orderDate: Date;
    vehicleDescs: string;
    articleDesc: string;
    companyName: string;
    address: string;
    postcode: string;
    customerNumber: string;
    orgId: number;
    reference?: string;
    branchRemark?: string;
    positionReferences?: string;
    customerInfo: string;
    totalGrossPrice: number;
    totalGrossPriceWithVat?: number;
    totalFinalCustomerNetPrice: number;
    totalFinalCustomerNetPriceWithVat?: number;

    constructor(data?) {
        if (data) {
            this.id = data.id;
            this.orderDate = data.orderDate;
            this.companyName = data.companyName;
            this.address = data.address;
            this.postcode = data.postcode;
            this.customerNumber = data.customerNumber;
            this.vehicleDescs = data.vehicleDescs;
            this.articleDesc = data.articleDesc;
            this.orgId = data.orgId;
            this.reference = data.reference;
            this.branchRemark = data.branchRemark;
            this.positionReferences = data.positionReferences;
            this.customerInfo = `${this.companyName} ${this.address} ${this.postcode} ${this.customerNumber}`;
            this.totalGrossPrice = data.totalGrossPrice;
            this.totalGrossPriceWithVat = data.totalGrossPriceWithVat;
            this.totalFinalCustomerNetPrice = data.totalFinalCustomerNetPrice;
            this.totalFinalCustomerNetPriceWithVat = data.totalFinalCustomerNetPriceWithVat;
        }
    }
}
