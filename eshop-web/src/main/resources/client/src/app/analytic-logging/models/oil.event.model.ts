import { OilFilter } from 'src/app/oil/models/oil-filter.model';
import { MetadataLogging } from './analytic-metadata.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class OilEvent extends MetadataLogging {
    eventType = Constant.OIL_EVENT;
    oilVehicleType: string;
    oilGear: string;
    oilViscosity: string;
    oilContainersize: number;
    oilRelease: string;
    oilSpecification: string;
    oilAdvertisementClicked: boolean;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: OilFilter, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.oilVehicleType = data.vehicle;
            this.oilGear = data.aggregate;
            this.oilViscosity = data.viscosity;
            this.oilContainersize = +data.bottleSize;
            this.oilRelease = data.approved;
            this.oilSpecification = data.specification;
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
            oil_advertisement_clicked: this.oilAdvertisementClicked
        };
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            oil_vehicle_type: this.oilVehicleType,
            oil_gear: this.oilGear,
            oil_viscosity: this.oilViscosity,
            oil_containersize: this.oilContainersize,
            oil_release: this.oilRelease,
            oil_specification: this.oilSpecification,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
