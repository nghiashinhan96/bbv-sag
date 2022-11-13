import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class FulltextSearchEvent extends MetadataLogging {
    eventType = AnalyticEventType.FULL_TEXT_SEARCH_EVENT;
    ftsFilterSelected: string;
    ftsSearchTermEntered: string;
    ftsNumberOfHits: number;
    ftsShowMoreClicked?: number;
    ftsNameClicked?: string;
    ftsArticleIdClicked?: number;
    ftsHitListPosition?: number;
    ftsVehicleNameClicked?: string;
    ftsVehicleClicked?: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.ftsFilterSelected = data.ftsFilterSelected;
            this.ftsSearchTermEntered = data.ftsSearchTermEntered;
            this.ftsNumberOfHits = data.ftsNumberOfHits;
            this.ftsShowMoreClicked = data.ftsShowMoreClicked;
            this.ftsNameClicked = data.ftsNameClicked;
            this.ftsArticleIdClicked = data.ftsArticleIdClicked;
            this.ftsHitListPosition = data.ftsHitListPosition;
            this.ftsVehicleNameClicked = data.ftsVehicleNameClicked;
            this.ftsVehicleClicked = data.ftsVehicleClicked;
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
            fts_filter_selected: this.ftsFilterSelected,
            fts_search_term_entered: this.ftsSearchTermEntered,
            fts_number_of_hits: this.ftsNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }

    toSelectedItemEventRequest(isArticle: boolean, isShowMore) {
        const request = super.toRequest();

        if (isArticle) {
            if (isShowMore) {
                return {
                    ...request,
                    fts_filter_selected: this.ftsFilterSelected,
                    fts_search_term_entered: this.ftsSearchTermEntered,
                    fts_number_of_hits: this.ftsNumberOfHits,
                    fts_show_more_clicked: 1,
                    fts_name_clicked: '',
                    fts_article_id_clicked: '',
                    fts_hit_list_position: '',
                    basket_item_source_id: this.basketItemSourceId,
                    basket_item_source_desc: this.basketItemSourceDesc
                };
            } else {
                return {
                    ...request,
                    fts_filter_selected: this.ftsFilterSelected,
                    fts_show_more_clicked: 0,
                    fts_name_clicked: this.ftsNameClicked,
                    fts_article_id_clicked: this.ftsArticleIdClicked,
                    fts_hit_list_position: this.ftsHitListPosition,
                    basket_item_source_id: this.basketItemSourceId,
                    basket_item_source_desc: this.basketItemSourceDesc
                };
            }
        } else {
            if (isShowMore) {
                return {
                    ...request,
                    fts_filter_selected: this.ftsFilterSelected,
                    fts_search_term_entered: this.ftsSearchTermEntered,
                    fts_number_of_hits: this.ftsNumberOfHits,
                    fts_show_more_clicked: 1,
                    fts_vehicle_name_clicked: '',
                    fts_vehicle_clicked: '',
                    fts_hit_list_position: '',
                    basket_item_source_id: this.basketItemSourceId,
                    basket_item_source_desc: this.basketItemSourceDesc
                };
            } else {
                return {
                    ...request,
                    fts_filter_selected: this.ftsFilterSelected,
                    fts_show_more_clicked: 0,
                    fts_vehicle_name_clicked: this.ftsVehicleNameClicked,
                    fts_vehicle_clicked: this.ftsVehicleClicked,
                    fts_hit_list_position: this.ftsHitListPosition,
                    basket_item_source_id: this.basketItemSourceId,
                    basket_item_source_desc: this.basketItemSourceDesc
                };
            }
        }
    }
}
