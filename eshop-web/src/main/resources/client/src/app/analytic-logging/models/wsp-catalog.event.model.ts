import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class WspCatalogEvent extends MetadataLogging {
    eventType = AnalyticEventType.WSP_CATALOG_EVENT;
    wspCurrentTreeId: number;
    wspCurrentNodeId: number;
    wspSearchTermEntered: string;
    wspNumberOfNodeResults: number;
    wspLeafNodeClicked: string;
    wspStdTileLinkClicked: string;
    wspGenartTileLinkClicked: string;
    wspNumberOfArticleResults: number;
    wspShowMoreClicked: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.wspCurrentTreeId = data.wspCurrentTreeId;
            this.wspCurrentNodeId = data.wspCurrentNodeId;
            this.wspSearchTermEntered = data.wspSearchTermEntered;
            this.wspNumberOfNodeResults = data.wspNumberOfNodeResults;
            this.wspLeafNodeClicked = data.wspLeafNodeClicked;
            this.wspStdTileLinkClicked = data.wspStdTileLinkClicked;
            this.wspGenartTileLinkClicked = data.wspGenartTileLinkClicked;
            this.wspNumberOfArticleResults = data.wspNumberOfArticleResults;
            this.wspShowMoreClicked = data.wspShowMoreClicked;
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
            wsp_current_tree_id: this.wspCurrentTreeId,
            wsp_current_node_id: this.wspCurrentNodeId,
            wsp_search_term_entered: this.wspSearchTermEntered,
            wsp_number_of_node_results: this.wspNumberOfNodeResults,
            wsp_leaf_node_clicked: this.wspLeafNodeClicked,
            wsp_std_tile_link_clicked: this.wspStdTileLinkClicked,
            wsp_genart_tile_link_clicked: this.wspGenartTileLinkClicked,
            wsp_number_of_article_results: this.wspNumberOfArticleResults,
            wsp_show_more_clicked: this.wspShowMoreClicked,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
