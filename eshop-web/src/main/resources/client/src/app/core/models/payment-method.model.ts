export class PaymentMethod {
    id: number;
    descCode: string;
    paymentType: string;
    description: string;
    allowChoose: boolean;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descCode = data.descCode;
            this.paymentType = data.paymentType;
            this.description = data.description;
            this.allowChoose = data.allowChoose;
        }
    }
}
