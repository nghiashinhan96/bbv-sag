export class VehicleRequestTermModel {
    vehicleDesc?: string;
    vehicleData?: string;
    freeText?: string;
    motorCodeDesc?: string;
    makeId?: string;
    modelId?: string;
    
    constructor(
        json?: VehicleRequestTermModel
    ) {

        if (json) {
            this.vehicleData = json.vehicleData;
            this.vehicleDesc = json.vehicleDesc;
            this.freeText = json.freeText;
            this.motorCodeDesc = json.motorCodeDesc;
            this.makeId = json.makeId;
            this.modelId = json.modelId;
        }
    }
}
