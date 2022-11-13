import { MetadataLogging } from './analytic-metadata.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class TyreEvent extends MetadataLogging {
    eventType = Constant.TYRES_EVENT;
    tyresSearchTypeSelected: string;
    tyresSeasonCategorySelected: string;
    tyresWidthSelected: number;
    tyresHeightSelected: number;
    tyresDiameterSelected: number;
    tyresSpeedIndexSelected: string;
    tyresLoadIndexSelected: string;
    tyresSegmentSelected: string;
    tyresManufacturerSelected: string;
    tyresRunFlatSelected: boolean;
    tyresSpikeSelected: boolean;
    tyresNumberOfHits: number;
    tyresMatchCodeTermEntered: string;
    tyresAdvertisementClicked: boolean;
    tyresCategorySelected: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, numberOfHits?: number, selectedTab?: string, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.tyresSearchTypeSelected = selectedTab;
            this.tyresSeasonCategorySelected = data.season;
            this.tyresWidthSelected = data.width;
            this.tyresHeightSelected = data.height;
            this.tyresDiameterSelected = data.radius;
            this.tyresSpeedIndexSelected = data.speedIndex;
            this.tyresLoadIndexSelected = data.loadIndex;
            this.tyresSegmentSelected = data.tyreSegment;
            this.tyresManufacturerSelected = data.supplier;
            this.tyresRunFlatSelected = data.runflat;
            this.tyresSpikeSelected = data.spike;
            this.tyresNumberOfHits = numberOfHits;
            this.tyresCategorySelected = data.fzCategory || data.category;
            this.tyresMatchCodeTermEntered = (data.matchCode || '').trim();
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
            tyres_search_type_selected: this.tyresSearchTypeSelected,
            tyres_advertisement_clicked: this.tyresAdvertisementClicked
        };
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            tyres_search_type_selected: this.tyresSearchTypeSelected,
            tyres_season_category_selected: this.tyresSeasonCategorySelected,
            tyres_width_selected: this.tyresWidthSelected,
            tyres_height_selected: this.tyresHeightSelected,
            tyres_diameter_selected: this.tyresDiameterSelected,
            tyres_speedindex_selected: this.tyresSpeedIndexSelected,
            tyres_loadindex_selected: this.tyresLoadIndexSelected,
            tyres_segment_selected: this.tyresSegmentSelected,
            tyres_manufacturer_selected: this.tyresManufacturerSelected,
            tyres_runflat_selected: this.tyresRunFlatSelected,
            tyres_spike_selected: this.tyresSpikeSelected,
            tyres_number_of_hits: this.tyresNumberOfHits,
            tyres_category_selected: this.tyresCategorySelected,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }

    toMatchCodeEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            tyres_matchcode_term_entered: this.tyresMatchCodeTermEntered,
            tyres_search_type_selected: this.tyresSearchTypeSelected,
            tyres_number_of_hits: this.tyresNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
