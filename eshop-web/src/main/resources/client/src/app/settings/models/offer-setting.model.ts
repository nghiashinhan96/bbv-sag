export class OfferSetting {
    group: string;
    title: string;
    options: string[];
    orgSettings: string;

    constructor(data?: any) {
        if (data) {
            this.group = data.group;
            this.title = data.title;
            this.options = data.options;
            this.orgSettings = data.orgSettings;
        }
    }
}
