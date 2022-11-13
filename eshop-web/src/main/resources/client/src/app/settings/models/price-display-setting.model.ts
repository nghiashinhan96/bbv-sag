export class PriceDisplaySetting {
    id: number;
    descriptionCode: string;
    editable: boolean;
    enable: boolean;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descriptionCode = data.descriptionCode;
            this.editable = data.editable;
            this.enable = data.enable;
        }
    }
}
