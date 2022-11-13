export class MessageModel {

    title: string;
    type: string;
    area: string;
    subArea: string;
    locationValue: string;
    active: boolean;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.type = data.type;
        this.area = data.area;
        this.subArea = data.subArea;
        this.locationValue = data.locationValue;
        this.active = data.active;
    }
}
