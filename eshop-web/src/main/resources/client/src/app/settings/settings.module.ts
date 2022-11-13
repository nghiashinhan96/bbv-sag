import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';

import { SagCurrencyModule } from 'sag-currency';
import { SagArticleDetailModule } from 'sag-article-detail';
import { SagCommonSliderModule } from 'sag-common';
import { SagCustomPricingModule } from 'sag-custom-pricing';
import { SagTableModule } from 'sag-table';

import { AccountDetailFormComponent } from './components/account-detail-form/account-detail-form.component';
import { AdminConfigComponent } from './components/admin-config/admin-config.component';
import { ChangePasswordFormComponent } from './components/change-password-form/change-password-form.component';
import { InvoiceFilterComponent } from './components/invoice-filter/invoice-filter.component';
import { InvoiceOverviewComponent } from './components/invoice-overview/invoice-overview.component';
import { InvoiceSearchResultComponent } from './components/invoice-search-result/invoice-search-result.component';
import { OrderConditionsComponent } from './components/order-conditions/order-conditions.component';
import { OrderHistoryDetailComponent } from './components/order-history-detail/order-history-detail.component';
import { OrderHistoryFilterComponent } from './components/order-history-filter/order-history-filter.component';
import { OrderHistorySearchResultComponent } from './components/order-history-search-result/order-history-search-result.component';
import { UserConfigComponent } from './components/user-config/user-config.component';
import { ConfigComponent } from './pages/config/config.component';
import { InvoiceComponent } from './pages/invoice/invoice.component';
import { OrderHistoryComponent } from './pages/order-history/order-history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { InvoiceBusinessService } from './services/invoice-business.service';
import { OrderHistoryDetailResolverService } from './services/order-history-detail-resolver.service';
import { ProfileBusinessService } from './services/profile-business.service';
import { SettingsRoutingModule } from './settings-routing.module';
import { InvoiceDetailComponent } from './components/invoice-detail/invoice-detail.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { NormalAdminComponent } from './pages/normal-admin/normal-admin.component';
import { CreateUserModalFormComponent } from './components/create-user-modal-form/create-user-modal-form.component';
import { NormalAdminUserListComponent } from './components/normal-admin-user-list/normal-admin-user-list.component';
import { NormalAdminBusinessService } from './services/normal-admin-business.service';
import { DeleteModalComponent } from './components/delete-modal/delete-modal.component';
import { FinalUserAdminComponent } from './pages/final-user-admin/final-user-admin.component';
import { NormalAdminUserModalComponent } from './components/normal-admin-user-modal/normal-admin-user-modal.component';
import { NormalAdminUserProfileFormComponent } from './components/normal-admin-user-profile-form/normal-admin-user-profile-form.component';
import {
    NormalAdminUserSettingsFormComponent
} from './components/normal-admin-user-settings-form/normal-admin-user-settings-form.component';
import {
    NormalAdminUserPasswordFormComponent
} from './components/normal-admin-user-password-form/normal-admin-user-password-form.component';
import { FinalUserAdminOrdersComponent } from './pages/final-user-admin-orders/final-user-admin-orders.component';
import { FinalUserAdminOrderDetailComponent } from './pages/final-user-admin-order-detail/final-user-admin-order-detail.component';
import { SaleOrderHistoryResultComponent } from './components/sale-order-history-result/sale-order-history-result.component';
import { SaleOrderHistoryDetailComponent } from './components/sale-order-history-detail/sale-order-history-detail.component';
import { FinalCustomerModule } from '../shared/final-customer/final-customer.module';
import { DigiInvoiceRequestModalComponent } from './components/digi-invoice-request-modal/digi-invoice-request-modal.component';
import { AnalyticalCardComponent } from './pages/analytical-card/analytical-card.component';
import { AnalyticalCardFilterComponent } from './components/analytical-card-filter/analytical-card-filter.component';
import { AnalyticalCardListComponent } from './components/analytical-card-list/analytical-card-list.component';
import { AnalyticalCardContainerComponent } from './components/analytical-card-container/analytical-card-container.component';
import { AnalyticalCardDetailComponent } from './pages/analytical-card-detail/analytical-card-detail.component';

@NgModule({
    declarations: [
        ProfileComponent,
        ChangePasswordFormComponent,
        AccountDetailFormComponent,
        SettingsComponent,
        ConfigComponent,
        UserConfigComponent,
        AdminConfigComponent,
        OrderConditionsComponent,
        OrderHistoryComponent,
        OrderHistoryFilterComponent,
        OrderHistorySearchResultComponent,
        OrderHistoryDetailComponent,
        InvoiceComponent,
        InvoiceFilterComponent,
        InvoiceSearchResultComponent,
        NormalAdminComponent,
        FinalUserAdminComponent,
        FinalUserAdminOrdersComponent,
        FinalUserAdminOrderDetailComponent,
        CreateUserModalFormComponent,
        InvoiceOverviewComponent,
        InvoiceDetailComponent,
        NormalAdminUserListComponent,
        NormalAdminUserModalComponent,
        NormalAdminUserPasswordFormComponent,
        NormalAdminUserProfileFormComponent,
        NormalAdminUserSettingsFormComponent,
        DeleteModalComponent,
        SaleOrderHistoryResultComponent,
        SaleOrderHistoryDetailComponent,
        DigiInvoiceRequestModalComponent,
        AnalyticalCardComponent,
        AnalyticalCardContainerComponent,
        AnalyticalCardFilterComponent,
        AnalyticalCardListComponent,
        AnalyticalCardDetailComponent
    ],
    imports: [
        CommonModule,
        ConnectCommonModule,
        SettingsRoutingModule,
        TranslateModule,
        ReactiveFormsModule,
        NgSelectModule,
        SagCurrencyModule,
        AngularMyDatePickerModule,
        SagTableModule,
        SagArticleDetailModule,
        SagCustomPricingModule,
        ModalModule.forRoot(),
        TabsModule.forRoot(),
        SagCommonSliderModule,
        ConnectCommonModule,
        FinalCustomerModule
    ],
    providers: [
        OrderHistoryDetailResolverService,
        InvoiceBusinessService,
        ProfileBusinessService,
        NormalAdminBusinessService
    ],
    entryComponents: [
        InvoiceOverviewComponent,
        CreateUserModalFormComponent,
        NormalAdminUserModalComponent,
        DeleteModalComponent,
        DigiInvoiceRequestModalComponent
    ]
})
export class SettingsModule { }
