import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgSelectModule } from '@ng-select/ng-select';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';

import { SagTableModule } from 'sag-table';
import { SagCommonModule } from 'sag-common';
import { SagHaynesProModule } from 'sag-haynespro';
import { SagCurrencyModule } from 'sag-currency';
import { ArticlesService } from 'sag-article-detail';

import { OffersRoutingModule } from './offers-routing.module';
import { OffersComponent } from './offers.component';
import { OfferListComponent } from './pages/offer-list/offer-list.component';
import { OffersService } from './services/offers.services';
import { OfferArticlesComponent } from './pages/offer-articles/offer-articles.component';
import { OfferCustomersComponent } from './pages/offer-customers/offer-customers.component';
import { OfferHeaderComponent } from './components/offer-header/offer-header.component';
import { SpinnerService } from '../core/utils/spinner';
import { OfferDetailComponent } from './pages/offer-detail/offer-detail.component';
import { OfferFilterPipe } from './utils/offer-filter.pipe';
import { OfferItemComponent } from './components/offer-item/offer-item.component';
import { OfferDiscountItemComponent } from './components/offer-discount-item/offer-discount-item.component';
import { OfferDiscountService } from './services/offer-discount.service';
import { OfferDiscountModalComponent } from './components/offer-discount-modal/offer-discount-modal.component';
import { OfferPersonModalComponent } from './components/offer-person-modal/offer-person-modal.component';
import { OfferPersonFormModalComponent } from './components/offer-person-form-modal/offer-person-form-modal.component';
import { OfferArticleFormModalComponent } from './components/offer-article-form-modal/offer-article-form-modal.component';
import { OfferArticleModalComponent } from './components/offer-article-modal/offer-article-modal.component';
import { CanDeactivateGuard } from '../core/services/deactive-guard.service';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';


@NgModule({
    declarations: [
        OffersComponent,
        OfferListComponent,
        OfferDetailComponent,
        OfferArticlesComponent,
        OfferCustomersComponent,
        OfferHeaderComponent,
        OfferItemComponent,
        OfferDiscountItemComponent,
        OfferPersonModalComponent,
        OfferPersonFormModalComponent,
        OfferArticleModalComponent,
        OfferArticleFormModalComponent,
        OfferDiscountModalComponent,
        OfferFilterPipe
    ],
    entryComponents: [
        OfferPersonModalComponent,
        OfferPersonFormModalComponent,
        OfferArticleModalComponent,
        OfferArticleFormModalComponent,
        OfferDiscountModalComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule,
        ModalModule.forRoot(),
        NgSelectModule,
        AngularMyDatePickerModule,
        SagCurrencyModule,
        SagTableModule,
        OffersRoutingModule,
        SagCommonModule,
        ConnectCommonModule,
        SagHaynesProModule
    ],
    providers: [
        CanDeactivateGuard,
        OffersService,
        OfferDiscountService,
        ArticlesService
    ]
})
export class OffersModule { }
