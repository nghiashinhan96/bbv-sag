import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ArticleModel } from 'sag-article-detail';
import { BasketItemSource } from './basket-item-source.model';

export class ArticleResultEvent extends MetadataLogging {
    eventType = AnalyticEventType.ARTICLE_RESULTS;
    articles: [];
    artResultsVehicleId: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.articles = (data.articles || []).map(
                (article: ArticleModel) => {
                    return {
                        artresults_article_id: article.artid,
                        artresults_article_no: article.artnr
                    }
                }
            )
            this.artResultsVehicleId = data.vehicleId || null;
        }
        if (basketItemSource) {
            this.basketItemSourceId = basketItemSource.basketItemSourceId;
            this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
        }
    }

    toEventRequest() {
        const request = super.toRequest();
        if (this.artResultsVehicleId) {
            return {
                ...request,
                articles: this.articles,
                artresults_vehicle_id: this.artResultsVehicleId,
                basket_item_source_id: this.basketItemSourceId,
                basket_item_source_desc: this.basketItemSourceDesc
            };
        }

        return {
            ...request,
            articles: this.articles,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
