
export class MessageSearchCriteriaModel {

    title: string;
    type: string;
    area: string;
    subArea: string;
    locationValue: string;
    active: boolean;
    startDate: number;
    endDate: number;
    orderDescByTitle: boolean;
    orderDescByType: boolean;
    orderDescByArea: boolean;
    orderDescBySubArea: boolean;
    orderDescByActive: boolean;
    orderDescByDateValidFrom: boolean;
    orderDescByDateValidTo: boolean;

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
        this.startDate = data.startDate;
        this.endDate = data.endDate;
        this.orderDescByTitle = data.orderDescByTitle;
        this.orderDescByType = data.orderDescByType;
        this.orderDescByArea = data.orderDescByArea;
        this.orderDescByActive = data.orderDescByActive;
        this.orderDescByDateValidFrom = data.orderDescByDateValidFrom;
        this.orderDescByDateValidTo = data.orderDescByDateValidTo;
    }

    public static getEmptyModel(): MessageSearchCriteriaModel {
        return new MessageSearchCriteriaModel();
    }
}
