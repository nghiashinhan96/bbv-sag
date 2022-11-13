import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';

import { HeaderSearchComponent } from './components/header-search/header-search.component';
import { HeaderSettingsComponent } from './components/header-settings/header-settings.component';
import { HeaderShoppingBasketComponent } from './components/header-shopping-basket/header-shopping-basket.component';
import { HeaderSavedBasketComponent } from './components/header-saved-basket/header-saved-basket.component';
import { HeaderOrderDashboardComponent } from './components/header-order-dashboard/header-order-dashboard.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HeaderComponent } from './header.component';
import { TranslateModule } from '@ngx-translate/core';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ModalModule } from 'ngx-bootstrap/modal';
import { RouterModule } from '@angular/router';
import { PriceFileComponent } from './dialogs/price-file/price-file.component';
import { HappyPointDialogComponent } from './dialogs/happy-point-dialog/happy-point-dialog.component';
import { ConnectCommonModule } from 'src/app/shared/connect-common/connect-common.module';
import { HeaderShoppingBasketItemComponent } from './components/header-shopping-basket-item/header-shopping-basket-item.component';
import { SagTableModule } from 'sag-table';
import { SagCurrencyModule } from 'sag-currency';
import { HeaderSavedBasketModalComponent } from './components/header-saved-basket-modal/header-saved-basket-modal.component';
import { HeaderSavedBasketDetailComponent } from './components/header-saved-basket-detail/header-saved-basket-detail.component';
import { HeaderSubBasketComponent } from './components/header-sub-basket/header-sub-basket.component';
import { FeedbackModule } from 'src/app/feedback/feedback.module';
import { HeaderCreditLimitComponent } from './components/header-credit-limit/header-credit-limit.component';
import { HeaderSearchDashboardComponent } from './components/header-search-dashboard/header-search-dashboard.component';
import { HeaderReturnArticlesModalComponent } from './components/header-return-articles-modal/header-return-articles-modal.component';
import { HeaderSaveShoppingBasketModalComponent } from './components/header-save-shopping-basket-modal/header-save-shopping-basket-modal.component';
@NgModule({
    declarations: [
        HeaderComponent,
        HeaderSearchComponent,
        HeaderSettingsComponent,
        HeaderShoppingBasketComponent,
        HeaderSavedBasketComponent,
        HeaderOrderDashboardComponent,
        PriceFileComponent,
        HappyPointDialogComponent,
        HeaderShoppingBasketItemComponent,
        HeaderSavedBasketModalComponent,
        HeaderSavedBasketDetailComponent,
        HeaderSubBasketComponent,
        HeaderCreditLimitComponent,
        HeaderSearchDashboardComponent,
        HeaderReturnArticlesModalComponent,
        HeaderSaveShoppingBasketModalComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        ConnectCommonModule,
        NgSelectModule,
        ReactiveFormsModule,
        FormsModule,
        RouterModule,
        ModalModule.forRoot(),
        PopoverModule.forRoot(),
        TabsModule.forRoot(),
        SagCurrencyModule,
        SagTableModule,
        FeedbackModule
    ],
    entryComponents: [
        PriceFileComponent,
        HappyPointDialogComponent,
        HeaderSavedBasketModalComponent,
        HeaderReturnArticlesModalComponent,
        HeaderSaveShoppingBasketModalComponent
    ],
    exports: [
        HeaderComponent,
        HeaderSavedBasketComponent
    ]
})
export class HeaderModule { }
