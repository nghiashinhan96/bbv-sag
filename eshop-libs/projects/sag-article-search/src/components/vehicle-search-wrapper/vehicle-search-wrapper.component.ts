import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { AffiliateUtil } from 'sag-common';
import { LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE } from '../../constant';
import { SagSearchMakeModelTypeComponent } from '../make-model-type-search/make-model-type-search.component';

@Component({
    selector: 'sag-vehicle-search-wrapper',
    templateUrl: './vehicle-search-wrapper.component.html',
    styleUrls: ['./vehicle-search-wrapper.component.scss']
})
export class VehicleSearchWrapperComponent implements OnInit {
    @Input() customerNumber;
    @Input() hasVinSearchPermission: boolean;
    @Input() isSalesOnBeHalf: boolean;
    @Input() isFinalUserRole;
    @Input() language: string;
    @Input() dmsSearchQuery;
    @Input() autoFilledVinValue = '';

    @Output() vehicleSearchEmitter = new EventEmitter();
    @Output() vinPackagePurchaseEmitter = new EventEmitter();
    @Output() vehicleSearchEventEmitter = new EventEmitter();
    @Output() navigateToModelPageEventEmitter = new EventEmitter();


    isCH = AffiliateUtil.isAffiliateCH(this.config.affiliate);
    isAT = AffiliateUtil.isAffiliateAT(this.config.affiliate);
    searchMakeModelErrorMsg: string;

    readonly VEHICLE_MAKE_MODEL_TYPE_SEARCH = LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE;
    @ViewChild('makeModelType', { static: false }) makeModelType: SagSearchMakeModelTypeComponent;

    constructor(
        private config: ArticleSearchConfigService,
    ) { }

    ngOnInit() {
    }

    onVehicleSearchEmit(event) {
        this.vehicleSearchEmitter.emit(event);
    }

    onVinPackagePurchaseEmit(event) {
        this.vinPackagePurchaseEmitter.emit(event);
    }

    onVehicleSearchEventEmit(event) {
        this.vehicleSearchEventEmitter.emit(event);
    }

    onNavigateToModelPage(make) {
        this.navigateToModelPageEventEmitter.emit(make);
    }

    searchMakeModel(data) {
        this.vehicleSearchEmitter.emit({
            searchType: this.VEHICLE_MAKE_MODEL_TYPE_SEARCH,
            ...data
        });
    }

    onSearchMakeModelBtnClick() {
        if (this.makeModelType.searchForm.valid) {
            this.makeModelType.seachData();
        } else {
            this.makeModelType.checkValidMakeModelTypeInput();
            this.searchMakeModelErrorMsg = 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
        }
    }

}
