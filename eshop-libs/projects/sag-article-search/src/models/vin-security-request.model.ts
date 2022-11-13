export class VinSecurityRequest {
    vin: string;
    requestMode: string;
    language: string;

    constructor(vin: string, mode: string, lang: string) {
        this.vin = vin;
        this.requestMode = mode;
        this.language = lang;
    }
}
