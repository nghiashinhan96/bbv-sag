import { UserDetail } from "src/app/core/models/user-detail.model";
import { AnalyticEventType } from "../enums/event-type.enums";
import { MetadataLogging } from "./analytic-metadata.model";
import { BasketItemSource } from "./basket-item-source.model";

export class FavoriteArticleEvent extends MetadataLogging {
    eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
    artSearchType: string;
    artIdResult: string;
    artNameResult: string;
    artNumberOfResult: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.artSearchType = data.artSearchType;
            this.artIdResult = data.artIdResult;
            this.artNameResult = data.artNameResult;
            this.artNumberOfResult = data.artNumberOfResult;
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
            art_article_id_result: this.artIdResult,
            art_article_name_result: this.artNameResult,
            art_number_of_result: this.artNumberOfResult,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
