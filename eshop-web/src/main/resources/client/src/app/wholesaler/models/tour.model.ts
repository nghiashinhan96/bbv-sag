export class TourModel {
    id: string;
    name: string;
    wssTourTimesDtos: DurationModel[];
    orgId: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.name = data.name;
        this.orgId = data.orgId;
        this.wssTourTimesDtos = data.wssTourTimesDtos ? data.wssTourTimesDtos.map(duration => new DurationModel(duration)) : [];
    }
}

export class DurationModel {
    weekDay: string;
    departureTime: string;
    id?: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.weekDay = data.weekDay;
        this.departureTime = data.departureTime;
        this.id = data.id;
    }
}

export class TourRequestModel {
    name: string;
    orderDescByTourName: boolean;
}