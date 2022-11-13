export class CourierServiceModel {
    courierServiceCode: string;
    description: string;

    constructor(data?: any) {
        if (data) {
            this.courierServiceCode = data.courierServiceCode;
            this.description = data.description;
        }
    }
}
