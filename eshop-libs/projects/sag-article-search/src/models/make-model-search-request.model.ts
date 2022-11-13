import { MOTORBIKE_SUB_CLASS } from "../enums/make-model-search.enum";

export class MakeModelSearchRequest {
    vehicleType: string;
    makeCode: string;
    modelCode: string;
    cubicCapacity: string;
    vehicleSubClass: MOTORBIKE_SUB_CLASS[] = [];

    constructor(data?: any) {
        if (data) {
            this.vehicleType = data.vehicleType;
            this.makeCode = data.makeCode;
            this.modelCode = data.modelCode;
            this.cubicCapacity = data.cubicCapacity;
            if (data.road) {
                this.vehicleSubClass.push(MOTORBIKE_SUB_CLASS.ROAD);
            }
            if (data.mx) {
                this.vehicleSubClass.push(MOTORBIKE_SUB_CLASS.MX);
            }
            if (data.scooter) {
                this.vehicleSubClass.push(MOTORBIKE_SUB_CLASS.SCOOTER);
            }
            if (data.atv) {
                this.vehicleSubClass.push(MOTORBIKE_SUB_CLASS.ATV);
            }
        }
    }
}
