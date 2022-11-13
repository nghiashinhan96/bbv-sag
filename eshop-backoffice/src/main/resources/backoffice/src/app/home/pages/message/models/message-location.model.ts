export class MessageLocationModel {
    locationTypeId: number;
    locationValues: Array<string>;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.locationTypeId = data.locationTypeId;
        this.locationValues = data.locationValues;
    }
}
