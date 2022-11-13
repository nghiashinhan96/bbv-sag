import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';

import { SagTableModule } from 'sag-table';

import { OrderDashboardRoutingModule } from './order-dashboard-routing.module';
import { NewOrderComponent } from './pages/new-order/new-order.component';
import { MyCustomerOrderComponent } from './pages/my-customer-order/my-customer-order.component';
import { OrderedComponent } from './pages/ordered/ordered.component';
import { OrderDashboardListComponent } from './components/order-dashboard-list/order-dashboard-list.component';

import { OrderDashboardRefComponent } from './components/order-dashboard-ref/order-dashboard-ref.component';
import { OrderedDetailComponent } from './pages/ordered-detail/ordered-detail.component';
import { SagCommonSliderModule } from 'sag-common';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { SagCurrencyModule } from 'sag-currency';

@NgModule({
    declarations: [
        NewOrderComponent,
        MyCustomerOrderComponent,
        OrderedComponent,
        OrderDashboardListComponent,
        OrderDashboardRefComponent,
        OrderedDetailComponent
    ],
    imports: [
        CommonModule,
        OrderDashboardRoutingModule,
        TranslateModule,
        FormsModule,
        SagTableModule,
        AngularMyDatePickerModule,
        PopoverModule.forRoot(),
        ConnectCommonModule,
        SagCommonSliderModule,
        SagCurrencyModule
    ]
})
export class OrderDashboardModule { }
