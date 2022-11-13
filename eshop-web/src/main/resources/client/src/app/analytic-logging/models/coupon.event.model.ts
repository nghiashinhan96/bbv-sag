import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';

export class CouponEvent extends MetadataLogging {
    eventType = AnalyticEventType.COUPON_EVENT;
    couponCodeEntered: string;
    couponValue: number;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any) {
        super(metadata, user);
        if (data) {
            this.couponCodeEntered = data.couponCode;
            this.couponValue = data.discount;
        }
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            coupon_code_entered: this.couponCodeEntered,
            coupon_value: this.couponValue
        };
    }
}
