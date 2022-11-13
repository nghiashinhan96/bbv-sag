import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SagTableModule } from 'sag-table';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { ModalModule } from 'ngx-bootstrap/modal';

import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { NgxPaginationModule } from 'ngx-pagination';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { WholesalerComponent } from './pages/wholesaler/wholesaler.component';
import { WholesalerRoutingModule } from './wholesaler-routing.module';
import { OpeningDayComponent } from './pages/opening-day/opening-day.component';
import { OpeningDayDateFilterComponent } from './components/opening-day-date-filter/opening-day-date-filter.component';
import { OpeningDayListComponent } from './components/opening-day-list/opening-day-list.component';
import { WholesalerCustomersComponent } from './pages/wholesaler-customers/wholesaler-customers.component';
import { WholesalerCustomersUsersComponent } from './pages/wholesaler-customers-users/wholesaler-customers-users.component';
import { OpeningDayCalendarService } from './services/opening-day-calendar.service';
import { FinalCustomerModule } from '../shared/final-customer/final-customer.module';
import { OpeningDayFormModalComponent } from './components/opening-day-form-modal/opening-day-form-modal.component';
import { ImportOpeningDayModalComponent } from './components/import-opening-day-modal/import-opening-day-modal.component';
import { OpeningTimeComponent } from './pages/opening-time/opening-time.component';
import { OpeningTimePickerComponent } from './components/opening-time-picker/opening-time-picker.component';
import { HoursMinutePipe } from './pipe/hours-minute.pipe';
import { OpeningTimeDeleteModalComponent } from './components/opening-time-delete-modal/opening-time-delete-modal.component';
import { OpeningTimeDetailFormComponent } from './components/opening-time-detail-form/opening-time-detail-form.component';
import { BranchService } from './services/branch.service';
import { DeliveryProfileComponent } from './pages/delivery-profile/delivery-profile.component';
import { DeliveryProfileListComponent } from './components/delivery-profile-list/delivery-profile-list.component';
import { DeliveryProfileService } from './services/delivery-profile.service';
import { AssignTourListComponent } from './components/assign-tour-list/assign-tour-list.component';
import { DeliveryProfileEditModalComponent } from './components/delivery-profile-edit-modal/delivery-profile-edit-modal.component';
import { TourManagementComponent } from './pages/tour-management/tour-management.component';
import { TourService } from './services/tour.service';
import { TourListComponent } from './components/tour-list/tour-list.component';
import { TourEditFormModalComponent } from './components/tour-edit-form-modal/tour-edit-form-modal.component';
import { MarginManagementComponent } from './pages/margin-management/margin-management.component';
import { MarginArticleGroupComponent } from './components/margin-article-group/margin-article-group.component';
import { MarginBrandsComponent } from './components/margin-brands/margin-brands.component';
import { MarginService } from './services/margin.service';
import { MarginBrandDefaultComponent } from './components/margin-brand-default/margin-brand-default.component';
import { SagCurrencyModule } from 'sag-currency';
import { MarginInputComponent } from './components/margin-input/margin-input.component';
import { MarginBrandFormModalComponent } from './components/margin-brand-form-modal/margin-brand-form-modal.component';
import { MarginImportModalComponent } from './components/margin-import-modal/margin-import-modal.component';
import { MarginArticleGroupDefaultComponent } from './components/margin-article-group-default/margin-article-group-default.component';
import { MarginArticleGroupTableComponent } from './components/margin-article-group-table/margin-article-group-table.component';
import { MarginArticleGroupTableHeaderComponent } from './components/margin-article-group-table-header/margin-article-group-table-header.component';
import { MarginArticleGroupTableBodyComponent } from './components/margin-article-group-table-body/margin-article-group-table-body.component';
import { MarginArticleGroupFormModalComponent } from './components/margin-article-group-form-modal/margin-article-group-form-modal.component';
import { AngularTreeGridModule } from 'angular-tree-grid';
import { MarginArticleGroupTreeComponent } from './components/margin-article-group-tree/margin-article-group-tree.component';
import { CellActionComponent } from './components/cell-action/cell-action.component';
import { MarginArticleGroupListComponent } from './components/margin-article-group-list/margin-article-group-list.component';

@NgModule({
    declarations: [
        WholesalerCustomersComponent,
        WholesalerCustomersUsersComponent,
        WholesalerComponent,
        OpeningDayComponent,
        OpeningDayDateFilterComponent,
        OpeningDayListComponent,
        OpeningDayFormModalComponent,
        ImportOpeningDayModalComponent,
        OpeningTimeComponent,
        OpeningTimePickerComponent,
        HoursMinutePipe,
        OpeningTimeDeleteModalComponent,
        OpeningTimeDetailFormComponent,
        DeliveryProfileComponent,
        DeliveryProfileComponent,
        DeliveryProfileListComponent,
        AssignTourListComponent,
        DeliveryProfileEditModalComponent,
        TourManagementComponent,
        TourListComponent,
        TourEditFormModalComponent,
        MarginManagementComponent,
        MarginArticleGroupComponent,
        MarginBrandsComponent,
        MarginBrandDefaultComponent,
        MarginInputComponent,
        MarginBrandFormModalComponent,
        MarginImportModalComponent,
        MarginArticleGroupDefaultComponent,
        MarginArticleGroupTableComponent,
        MarginArticleGroupTableHeaderComponent,
        MarginArticleGroupTableBodyComponent,
        MarginArticleGroupFormModalComponent,
        MarginArticleGroupTreeComponent,

        CellActionComponent,

        MarginArticleGroupListComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        SagTableModule,
        TranslateModule,
        AngularMyDatePickerModule,
        WholesalerRoutingModule,
        ReactiveFormsModule,
        NgSelectModule,
        ModalModule.forRoot(),
        TabsModule.forRoot(),
        ConnectCommonModule,
        FinalCustomerModule,
        TimepickerModule.forRoot(),
        PopoverModule.forRoot(),
        NgxPaginationModule,
        PaginationModule.forRoot(),
        SagCurrencyModule,
        AngularTreeGridModule
    ],
    entryComponents: [
        OpeningDayFormModalComponent,
        ImportOpeningDayModalComponent,
        OpeningTimeDetailFormComponent,
        DeliveryProfileEditModalComponent,
        TourEditFormModalComponent,
        OpeningTimeDeleteModalComponent,
        MarginBrandFormModalComponent,
        MarginImportModalComponent,
        MarginArticleGroupFormModalComponent,

        CellActionComponent
    ],
    providers: [OpeningDayCalendarService, BranchService, DeliveryProfileService, TourService, MarginService]
})
export class WholesalerModule { }
