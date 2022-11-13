export class AnalyticalCardItem {
    paymentMethod: string;
    customerNr: string;
    documentType: string;
    documentNr: string;
    webOrderNr: string;
    postingDate: string;
    dueDate: string;
    paymentDeadlineNotification: string;
    status: string;
    remainingAmount: number;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.paymentMethod = data.paymentMethod;
        this.customerNr = data.customerNr;
        this.documentType = data.documentType;
        this.documentNr = data.documentNr;
        this.webOrderNr = data.webOrderNr;
        this.postingDate = data.postingDate;
        this.dueDate = data.dueDate;
        this.paymentDeadlineNotification = data.paymentDeadlineNotification;
        this.status = data.status;
        this.remainingAmount = data.remainingAmount;
    }
}