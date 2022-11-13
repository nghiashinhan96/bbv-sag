import { LanguageEnum } from '../enums/language.enum';

export class AutonetAuthModel {
    traderId: number;
    user: string;
    uid: string;
    customerId: string;
    customerFirstName: string;
    privileges: string;
    startPagePosition: number;
    hkat: number;
    timestamp: string;
    lid: number;
    startPage: number;
    token: string;
    constructor(json: AutonetAuthModel | any) {
        if (json) {
            this.traderId = json.traderId || json.TraderID;
            this.user = json.user || json.User;
            this.uid = json.uid || json.UID;
            this.customerId = json.customerId || json.CustomerID;
            this.customerFirstName = json.customerFirstName || json.CustomerFirstName;
            this.privileges = json.privileges || json.Privileges || '';
            this.startPagePosition = json.startPagePosition || json.StartPagePosition;
            this.hkat = json.hkat || json.HKat;
            this.timestamp = json.timestamp || json.timestamp;
            this.lid = json.lid || json['14'];
            this.startPage = json.startPage || json.StartPage;
            this.token = json.token || json.Token;
        }
    }

    get requestDto() {
        return {
            customerFirstName: this.customerFirstName,
            customerId: this.customerId,
            hkat: this.hkat,
            lid: LanguageEnum[this.lid],
            privileges: this.privileges,
            startPage: this.startPage,
            startPagePosition: this.startPagePosition,
            traderId: this.traderId,
            uid: this.uid,
            user: this.user
        };
    }
}
