export class GtmotiveSubmittedDataModel {
    operations = [];
    cupis = [];
    partCodes = [];

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.operations = data.operations;
        this.cupis = data.cupis;
        this.partCodes = data.partCodes;
    }
}
