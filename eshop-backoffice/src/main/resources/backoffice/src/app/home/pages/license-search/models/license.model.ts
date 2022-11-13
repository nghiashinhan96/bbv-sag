import  * as moment from 'moment';

export class LicenseModel {
    id: number;
    customerNr: string;
    packName: string;
    beginDate: string;
    endDate: string;
    typeOfLicense: string;
    quantity: number;
    quantityUsed: number;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.customerNr = data.customerNr;
        this.packName = data.packName || '';
        this.beginDate = this.getCurrentTimeZoneDateString(data.beginDate) || '';
        this.endDate = this.getCurrentTimeZoneDateString(data.endDate) || '';
        this.typeOfLicense = data.typeOfLicense || '';
        this.quantity = data.quantity || 0;
        this.quantityUsed = data.quantityUsed || 0;
    }

    private getCurrentTimeZoneDateString(date) {
        if (!date) {
            return;
        }
        return moment(date).format('DD.MM.YYYY');
    }
}