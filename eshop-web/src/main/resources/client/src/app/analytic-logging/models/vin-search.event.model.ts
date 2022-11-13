import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class VinSearchEvent extends MetadataLogging {
    eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
    vehSearchTermEnteredVinChn: string;
    vehSearchNumberOfHits: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.vehSearchTermEnteredVinChn = data.vehSearchTermEnteredVinChn;
            this.vehSearchNumberOfHits = data.vehSearchNumberOfHits;
        }
        if (basketItemSource) {
            this.basketItemSourceId = basketItemSource.basketItemSourceId;
            this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
        }
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            veh_search_term_entered_vin_chn: this.vehSearchTermEnteredVinChn,
            veh_search_number_of_hits: this.vehSearchNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
