import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class ArticleNrSearchEvent extends MetadataLogging {
    eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
    artSearchType: string;
    artSearchTermEntered: string;
    artNumberOfResult: number;
    fromSource: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.artSearchType = data.artSearchType;
            this.artSearchTermEntered = data.artSearchTermEntered;
            this.artNumberOfResult = data.artNumberOfResult;
            this.fromSource = data.fromSource;
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
            art_search_type: this.artSearchType,
            art_search_term_entered: this.artSearchTermEntered,
            art_number_of_result: this.artNumberOfResult,
            from_source: this.fromSource,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
