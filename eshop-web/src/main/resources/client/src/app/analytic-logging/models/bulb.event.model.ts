import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { BulbFilter } from 'src/app/bulbs/models/bulb-filter.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class BulbEvent extends MetadataLogging {
    eventType = AnalyticEventType.BULB_EVENT.toString();
    bulbsEcecode: string;
    bulbsVolt: number;
    bulbsWatt: number;
    bulbsManufacturer: string;
    bulbsNumberOfHits: number;
    bulbsAdvertisementClicked: boolean;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: BulbFilter, numberOfHits?: number, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.bulbsEcecode = data.code;
            this.bulbsVolt = +data.voltage;
            this.bulbsWatt = +data.watt;
            this.bulbsManufacturer = data.supplier;
            this.bulbsNumberOfHits = numberOfHits;
        }
        if (basketItemSource) {
            this.basketItemSourceId = basketItemSource.basketItemSourceId;
            this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
        }
    }

    toAdsEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            bulbs_advertisement_clicked: this.bulbsAdvertisementClicked
        };
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            bulbs_ececode: this.bulbsEcecode,
            bulbs_volt: this.bulbsVolt,
            bulbs_watt: this.bulbsWatt,
            bulbs_manufacturer: this.bulbsManufacturer,
            bulbs_number_of_hits: this.bulbsNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
