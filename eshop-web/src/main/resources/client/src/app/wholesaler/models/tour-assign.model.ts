import * as moment from 'moment';
import { DateUtil } from 'src/app/core/utils/date.util';

export class TourAssignModel {
    id: string;
    tourName: string;
    description: string;
    wssBranchId: string;
    supplierTourDay: string;
    supplierTourTime: string;
    isOverNight: boolean;
    wssTourId: string;
    pickupWaitDuration: number;
    wssDeliveryProfileTourId: string;
    tourDepartureTime: string;
    durationDisplayText = '';
    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.tourName = data.tourName;
        this.description = data.description;
        this.wssBranchId = data.wssBranchId;
        this.supplierTourDay = data.supplierTourDay;
        this.supplierTourTime = data.supplierTourTime;
        this.isOverNight = data.overNight;
        this.wssTourId = data.wssTourId;
        this.pickupWaitDuration = data.pickupWaitDuration;
        this.wssDeliveryProfileTourId = data.wssDeliveryProfileTourId;
        this.tourDepartureTime = data.tourDepartureTime;

        if (typeof (this.pickupWaitDuration) !== "undefined" && this.pickupWaitDuration !== null) {
            this.durationDisplayText = moment.utc(this.pickupWaitDuration * 1000).format('HH:mm');
        }
    }
}