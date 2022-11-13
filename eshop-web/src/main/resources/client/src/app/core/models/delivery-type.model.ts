export class DeliveryType {
    id: number;
    descCode: string;
    deliveryType: string;
    description: string;
    allowChoose: boolean;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descCode = data.descCode;
            this.deliveryType = data.deliveryType;
            this.description = data.description;
            this.allowChoose = data.allowChoose;
        }
    }
}
