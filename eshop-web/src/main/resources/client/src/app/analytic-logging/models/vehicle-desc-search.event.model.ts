import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class VehicleDescSearchEvent extends MetadataLogging {
    eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
    vehSearchTermEnteredFzb: string;
    vehSearchTermEnteredJg: string;
    vehSearchNumberOfHits: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.vehSearchTermEnteredFzb = data.vehSearchTermEnteredFzb;
            this.vehSearchTermEnteredJg = data.vehSearchTermEnteredJg;
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
            veh_search_term_entered_fzb: this.vehSearchTermEnteredFzb,
            veh_search_term_entered_jg: this.vehSearchTermEnteredJg,
            veh_search_number_of_hits: this.vehSearchNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
