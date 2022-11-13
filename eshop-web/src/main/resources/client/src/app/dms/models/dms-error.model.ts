export class DmsError {

    articlesNotFound: string[];
    vehicleNotFound: string;
    vehicleValidationFailed: boolean;
    offerIdFailed: number;

    public static builder(): DmsErrorBuilder {
        return new DmsErrorBuilder();
    }
}

class DmsErrorBuilder {

    private dmsError: DmsError;

    constructor() {
        this.dmsError = new DmsError();
    }

    public articlesNotFound(articlesNotFound: string[]) {
        this.dmsError.articlesNotFound = articlesNotFound;
        return this;
    }

    public vehicleNotfound(vehicleNotFound: string) {
        this.dmsError.vehicleNotFound = vehicleNotFound;
        return this;
    }

    public vehicleValidationFailed(vehicleValidationFailed: boolean) {
        this.dmsError.vehicleValidationFailed = vehicleValidationFailed;
        return this;
    }

    public offerIdFailed(offerIdFailed: number) {
        this.dmsError.offerIdFailed = offerIdFailed;
        return this;
    }

    public build() {
        return this.dmsError;
    }

}
