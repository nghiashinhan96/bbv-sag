export class CustomerSettingsOffer {
    footerText: string;
    formatAlign: string;
    generalCrossing: string;
    printVendorAddr: string;

    constructor(data?: any) {
        if (data) {
            this.footerText = data.footerText;
            this.formatAlign = data.formatAlign;
            this.generalCrossing = data.generalCrossing;
            this.printVendorAddr = data.printVendorAddr;
        }
    }
}
