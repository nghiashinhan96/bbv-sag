export class CustomerGroupDetailModel {
    affiliateShortName: string;
    name: string;
    description: string;
    permissions: any[];
    settings: any;
    constructor(json?) {
        if (!json) {
            return;
        }
        this.affiliateShortName = json.affiliateShortName;
        this.name = json.name;
        this.description = json.description;
        this.permissions = json.permissions;
        this.settings = json.settings;
    }
}
