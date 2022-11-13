import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class MakeModelTypeSearchEvent extends MetadataLogging {
    eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
    vehSearchTypeSelected: string;
    vehBrandSelected: string;
    vehModelSelected: string;
    vehTypeSelected: string;
    vehVehicleIdResult: string;
    vehVehicleNameResult: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.vehSearchTypeSelected = data.vehSearchTypeSelected
            this.vehBrandSelected = data.vehBrandSelected
            this.vehModelSelected = data.vehModelSelected
            this.vehTypeSelected = data.vehTypeSelected
            this.vehVehicleIdResult = data.vehVehicleIdResult
            this.vehVehicleNameResult = data.vehVehicleNameResult
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
            veh_search_type_selected: this.vehSearchTypeSelected,
            veh_brand_selected: this.vehBrandSelected,
            veh_model_selected: this.vehModelSelected,
            veh_type_selected: this.vehTypeSelected,
            veh_vehicle_id_result: this.vehVehicleIdResult,
            veh_vehicle_name_result: this.vehVehicleNameResult,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
