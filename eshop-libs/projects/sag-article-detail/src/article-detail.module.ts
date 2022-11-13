import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { TabsModule } from 'ngx-bootstrap/tabs';

import {
    SagCommonModule,
    SagCommonSliderModule,
    BroadcastService
} from 'sag-common';
import { SagCurrencyModule } from 'sag-currency';
import { SagCustomPricingModule } from 'sag-custom-pricing';

import { SagArticleDetailDescriptionComponent } from './components/article-detail-description/article-detail-description.component';
import { SagArticleDetailImageComponent } from './components/article-detail-image/article-detail-image.component';
import { SagArticleDetailManufacturerComponent } from './components/article-detail-manufacturer/article-detail-manufacturer.component';

import { ArticleDetailConfigService } from './services/article-detail-config.service';
import { ArticlesService } from './services/articles.service';

import { SagArticleDetailComponent } from './components/article-detail/article-detail.component';
import { SagArticleDetailSpecComponent } from './components/article-detail-spec/article-detail-spec.component';
import { SagArticleDetailPdfComponent } from './components/article-detail-pdf/article-detail-pdf.component';
import { SagArticleDetailRefrencesComponent } from './components/article-detail-refrences/article-detail-refrences.component';
import { SagArticleDetailAvailPopupComponent } from './components/article-avail-popup/article-avail-popup.component';
import {
    SagArticleDetailSpecVehicleUsageComponent
} from './components/article-detail-spec-vehicle-usage/article-detail-spec-vehicle-usage.component';
import { SagArticleDetailSingleComponent } from './components/single-article/single-article.component';
import {
    SagArticleDetailRefrencesContentComponent
} from './components/article-detail-refrences-content/article-detail-refrences-content.component';
import { SagArticleDetailIntegrationService } from './services/article-detail-integration.service';
import { SagArticlePccNumberComponent } from './components/article-pcc-number/article-pcc-number.component';
import { SagPccPopoverComponent } from './components/article-pcc-number/components/pcc-popover/pcc-popover.component';
import { SagArticleAvailComponent } from './components/article-avail/article-avail.component';
import { SagArticleNetPriceComponent } from './components/article-net-price/article-net-price.component';
import { SagArticleDetailStorageService } from './services/article-detail-storage.service';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { SagArticleViewLocationPopoverComponent } from './components/article-view-location-popover/article-view-location-popover.component';
import { SagArticleWarningAvailPopupComponent } from './components/article-warning-avail-popup/article-warning-avail-popup.component';
import { SagArticleDetailMerkmaleComponent } from './components/article-detail-merkmale/article-detail-merkmale.component';
import { SagArticleFavoriteEditModalComponent } from './components/article-favorite-edit-modal/article-favorite-edit-modal.component';
import { SagArticleFavoriteListComponent } from './components/article-favorite-list/article-favorite-list.component';
import { SagArticleFavoriteIconComponent } from './components/article-favorite-icon/article-favorite-icon.component';
import { SagArticleFavoriteListContentComponent } from './components/article-favorite-list-content/article-favorite-list-content.component';
import { SagArticleInfoItemComponent } from './components/article-info-item/article-info-item.component';
import { SagArticleShowPriceComponent } from './components/article-show-price/article-show-price.component';
import { SagArticleCartNetPriceComponent } from './components/article-cart-net-price/article-cart-net-price.component';
import { SagArticleTotalPriceComponent } from './components/article-total-price/article-total-price.component';
import { SagArticleDetailAvailPopupPricePdpComponent } from './components/article-avail-popup-price-pdp/article-avail-popup-price-pdp.component';
import { SagArticleDetailAvailPopupPriceComponent } from './components/article-avail-popup-price/article-avail-popup-price.component';
import { SagArticleDetailAvailPopupPriceFinalComponent } from './components/article-avail-popup-price-final/article-avail-popup-price-final.component';
import { SagArticleDetailAvailPopupShowPriceComponent } from './components/article-avail-popup-show-price/article-avail-popup-show-price.component';
import { SagArticleDetailAvailPopupPriceDefaultComponent } from './components/article-avail-popup-price-default/article-avail-popup-price-default.component';
import { SagArticleGrossPriceComponent } from './components/article-gross-price/article-gross-price.component';
import { SagArticleAvailPopupAvailabilityComponent } from './components/article-avail-popup-availability/article-avail-popup-availability.component';
import { SagArticleAvailPopupDepositArticleComponent } from './components/article-avail-popup-deposit-article/article-avail-popup-deposit-article.component';

@NgModule({
    declarations: [
        SagArticleDetailDescriptionComponent,
        SagArticleDetailImageComponent,
        SagArticleDetailManufacturerComponent,
        SagArticleDetailComponent,
        SagArticleDetailSpecComponent,
        SagArticleDetailPdfComponent,
        SagArticleDetailRefrencesComponent,
        SagArticleDetailAvailPopupComponent,
        SagArticleDetailSpecVehicleUsageComponent,
        SagArticleDetailRefrencesContentComponent,
        SagArticleDetailSingleComponent,
        SagArticlePccNumberComponent,
        SagPccPopoverComponent,
        SagArticleAvailComponent,
        SagArticleNetPriceComponent,
        SagArticleViewLocationPopoverComponent,
        SagArticleWarningAvailPopupComponent,
        SagArticleDetailMerkmaleComponent,
        SagArticleFavoriteEditModalComponent,
        SagArticleFavoriteListComponent,
        SagArticleFavoriteListContentComponent,
        SagArticleFavoriteIconComponent,
        SagArticleInfoItemComponent,
        SagArticleTotalPriceComponent,
        SagArticleCartNetPriceComponent,
        SagArticleShowPriceComponent,
        SagArticleDetailAvailPopupPriceComponent,
        SagArticleDetailAvailPopupPriceFinalComponent,
        SagArticleDetailAvailPopupPricePdpComponent,
        SagArticleDetailAvailPopupShowPriceComponent,
        SagArticleDetailAvailPopupPriceDefaultComponent,
        SagArticleGrossPriceComponent,
        SagArticleAvailPopupAvailabilityComponent,
        SagArticleAvailPopupDepositArticleComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule,
        PopoverModule.forRoot(),
        TabsModule.forRoot(),
        SagCommonModule,
        SagCommonSliderModule,
        SagCurrencyModule.forRoot(),
        SagCustomPricingModule,
        NgxWebstorageModule
    ],
    exports: [
        SagArticleDetailDescriptionComponent,
        SagArticleDetailImageComponent,
        SagArticleDetailManufacturerComponent,
        SagArticleDetailComponent,
        SagArticleDetailSpecComponent,
        SagArticleDetailPdfComponent,
        SagArticleDetailRefrencesComponent,
        SagArticleDetailAvailPopupComponent,
        SagArticleDetailSpecVehicleUsageComponent,
        SagArticleDetailSingleComponent,
        SagArticlePccNumberComponent,
        SagPccPopoverComponent,
        SagArticleAvailComponent,
        SagArticleNetPriceComponent,
        SagArticleViewLocationPopoverComponent,
        SagArticleWarningAvailPopupComponent,
        SagArticleDetailMerkmaleComponent,
        SagArticleFavoriteEditModalComponent,
        SagArticleFavoriteListComponent,
        SagArticleFavoriteIconComponent,
        SagArticleTotalPriceComponent,
        SagArticleCartNetPriceComponent,
        SagArticleGrossPriceComponent,
        SagArticleAvailPopupAvailabilityComponent,
        SagArticleAvailPopupDepositArticleComponent
    ],
    entryComponents: [
        SagArticleDetailRefrencesComponent,
        SagArticleDetailSpecVehicleUsageComponent,
        SagPccPopoverComponent,
        SagArticleViewLocationPopoverComponent,
        SagArticleWarningAvailPopupComponent,
        SagArticleFavoriteEditModalComponent
    ]
})
export class SagArticleDetailModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagArticleDetailModule,
            providers: [
                ArticleDetailConfigService,
                SagArticleDetailIntegrationService,
                SagArticleDetailStorageService,
                ArticlesService,
                BroadcastService
            ]
        };
    }
}
