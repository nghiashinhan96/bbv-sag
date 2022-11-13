export class ArticleAvailLocationItemModel {
    locationId: number;
    locationName: string;
    quantity: number;
    locationPhoneNr: string;
    locationType: string;

    constructor (data = null) {
        if (data) {
            this.locationId = data.locationId;
            this.locationName = data.locationName;
            this.quantity = data.quantity;
            this.locationPhoneNr = data.locationPhoneNr;
            this.locationType = data.locationType;
        }
    }
}