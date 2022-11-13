export class DeliveryType {
    allowChoose: boolean;
    descCode: string;
    description: string;
    id: number;
    type: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.allowChoose = data.allowChoose;
        this.descCode = data.descCode;
        this.description = data.description;
        this.id = data.id;
        this.type = data.type;
    }
}