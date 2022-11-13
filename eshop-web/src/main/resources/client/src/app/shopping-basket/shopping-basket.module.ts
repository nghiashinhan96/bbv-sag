import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';


import { TranslateModule } from '@ngx-translate/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgSelectModule } from '@ng-select/ng-select';

import { SagArticleListModule, ArticleListConfigService } from 'sag-article-list';
import { SagCurrencyModule } from 'sag-currency';
import { SagCustomPricingModule } from 'sag-custom-pricing';

import { ShoppingBasketRoutingModule } from './shopping-basket-routing.module';
import { ShoppingCartComponent } from './pages/shopping-cart/shopping-cart.component';
import { ShoppingOrderComponent } from './pages/shopping-order/shopping-order.component';
import { ShoppingCheckoutComponent } from './pages/shopping-checkout/shopping-checkout.component';
import { ShoppingBasketComponent } from './shopping-basket.component';
import { ArticleInContextListConfigService } from '../article-in-context-result-list/services/article-in-context-list-config.service';
import { CouponService } from './services/coupon.service';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { ShoppingConditionComponent } from './components/shopping-condition/shopping-condition.component';
import { DeliverySummaryPipe } from './pipes/delivery-summary.pipe';
import { DvseListComponent } from './components/dvse-list/dvse-list.component';
import { ShoppingBasketResolver } from './shopping-basket.resolver';
import { ShoppingBasketStepperActivate } from './shopping-basket-stepper.activate';
import { FinalCustomerModule } from '../final-customer/final-customer.module';
import { OffersService } from '../offers/services/offers.services';
import { ShoppingOrderConditionComponent } from './components/shopping-order-condition/shopping-order-condition.component';
import { ShoppingOrderPartListComponent } from './components/shopping-order-part-list/shopping-order-part-list.component';
import { ShoppingOrderPartGroupComponent } from './components/shopping-order-part-group/shopping-order-part-group.component';
import { ShoppingOrderPartHeaderComponent } from './components/shopping-order-part-header/shopping-order-part-header.component';
import { ShoppingOrderArticleDetailComponent } from './components/shopping-order-article-detail/shopping-order-article-detail.component';
import { ArticleShoppingBasketService } from '../core/services/article-shopping-basket.service';
import { CustomerOrderTypeService } from './services/customer-order-type.service';
import { SalesOrderTypeService } from './services/sales-order-type.service';
import { AvailCheckerModalComponent } from './components/avail-checker-modal/avail-checker-modal.component';
import { ShoppingOrderService } from './services/shopping-order.service';
import { ArticleListSearchModule } from '../article-list/article-list-search.module';
import { ArticleListSearchModalComponent } from './components/article-list-search-modal/article-list-search-modal.component';
import { ArticlesService } from 'sag-article-detail';
import { CanDeactivateCheckoutGuard } from './pages/shopping-checkout/checkout-deactivate';
import { ConnectBarCodeDirective } from './directives/bar-code.directive';
import { CzCustomModule } from '../shared/cz-custom/cz-custom.module';
import { SagArticleDetailModule } from 'sag-article-detail';
import { ShoppingConditionSBComponent } from './components/sb/shopping-condition-sb/shopping-condition-sb.component';
import { ShoppingOrderConditionSBComponent } from './components/sb/shopping-order-condition-sb/shopping-order-condition-sb.component';
import { ShoppingChekoutSBComponent } from './components/sb/shopping-chekout-sb/shopping-chekout-sb.component';
import { ShoppingOrderArticleDetailSbComponent } from './components/sb/shopping-order-article-detail-sb/shopping-order-article-detail-sb.component';
import { ShoppingOrderPartHeaderSbComponent } from './components/sb/shopping-order-part-header-sb/shopping-order-part-header-sb.component';
import { ShoppingOrderArticleGrossPriceComponent } from './components/shopping-order-article-gross-price/shopping-order-article-gross-price.component';
import { ShoppingOrderArticleSubtotalComponent } from './components/shopping-order-article-subtotal/shopping-order-article-subtotal.component';
import { ShoppingOrderArticleTotalPriceComponent } from './components/shopping-order-article-total-price/shopping-order-article-total-price.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { HeaderModule } from '../layout/components/header/header.module';

@NgModule({
    declarations: [
        ShoppingCartComponent,
        ShoppingOrderComponent,
        ShoppingCheckoutComponent,
        ShoppingBasketComponent,
        ShoppingConditionComponent,
        DeliverySummaryPipe,
        DvseListComponent,
        ShoppingOrderConditionComponent,
        ShoppingOrderPartListComponent,
        ShoppingOrderPartGroupComponent,
        ShoppingOrderPartHeaderComponent,
        ShoppingOrderArticleDetailComponent,
        AvailCheckerModalComponent,
        ArticleListSearchModalComponent,
        ConnectBarCodeDirective,
        ShoppingConditionSBComponent,
        ShoppingOrderConditionSBComponent,
        ShoppingChekoutSBComponent,
        ShoppingOrderArticleDetailSbComponent,
        ShoppingOrderPartHeaderSbComponent,
        ShoppingOrderArticleGrossPriceComponent,
        ShoppingOrderArticleSubtotalComponent,
        ShoppingOrderArticleTotalPriceComponent
    ],
    entryComponents: [
        AvailCheckerModalComponent,
        ArticleListSearchModalComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ShoppingBasketRoutingModule,
        TranslateModule,
        SagArticleListModule,
        SagCurrencyModule,
        SagCustomPricingModule,
        SagArticleDetailModule,
        ModalModule.forRoot(),
        PopoverModule.forRoot(),
        ReactiveFormsModule,
        ConnectCommonModule,
        NgSelectModule,
        FinalCustomerModule,
        ArticleListSearchModule,
        CzCustomModule,
        HeaderModule
    ],
    providers: [
        { provide: ArticleListConfigService, useClass: ArticleInContextListConfigService },
        CouponService,
        ShoppingBasketResolver,
        ShoppingBasketStepperActivate,
        OffersService,
        ArticleShoppingBasketService,
        CustomerOrderTypeService,
        SalesOrderTypeService,
        ArticlesService,
        ShoppingOrderService,
        CanDeactivateCheckoutGuard
    ],
})
export class ShoppingBasketModule { }
