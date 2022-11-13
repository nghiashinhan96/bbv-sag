import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';

export class ArticleListEvent extends MetadataLogging {
    eventType = AnalyticEventType.ARTICLE_LIST_EVENT.toString();
    artlistArticleId: number;
    artlistDetailsClicked: boolean;
    artlistAdvertisementClicked: boolean;
    artlistPriceType: string;
    artlistPriceVehBrand: string;
    artlistVehicleId: string;

    constructor(metadata: MetadataLogging | any, userDetail: UserDetail, data: any) {
        super(metadata, userDetail);
        if (data) {
            this.artlistArticleId = +data.artid || null;
            this.artlistDetailsClicked = data.isShownSpecDetail;
            this.artlistPriceType = data.artlistPriceType;
            this.artlistPriceVehBrand = data.artlistPriceVehBrand;
            this.artlistVehicleId = data.artlistVehicleId;
        }
    }

    toAdsEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            artlist_advertisement_clicked: this.artlistAdvertisementClicked
        };
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            artlist_article_id: this.artlistArticleId,
            artlist_details_clicked: this.artlistDetailsClicked,
            artlist_price_type: this.artlistPriceType,
            artlist_price_veh_brand: this.artlistPriceVehBrand,
            artlist_vehicle_id: this.artlistVehicleId
        };
    }
}
