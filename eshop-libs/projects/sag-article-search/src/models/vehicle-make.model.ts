export class VehicleMake {
    makeId: number;
    make: string;
    gtMake: any;
    gtVin: boolean;
    normauto: boolean;

    constructor(data: any) {
        if (data) {
            this.makeId = data.makeId;
            this.make = data.make;
            this.gtMake = data.gtMake;
            this.gtVin = data.gtVin;
            this.normauto = data.normauto;
        }
    }
}