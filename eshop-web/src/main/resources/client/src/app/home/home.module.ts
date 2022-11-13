import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { SagCurrencyModule } from 'sag-currency';
import { SagHaynesProModule } from 'sag-haynespro';
import { ArticleSearchModule } from 'sag-article-search';
import { SagTableModule } from 'sag-table';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { VehicleHistoryComponent } from './components/vehicle-history/vehicle-history.component';
import { ArticleSearchComponent } from './components/article-search/article-search.component';
import { VehicleSearchComponent } from './components/vehicle-search/vehicle-search.component';
import { SaleControlComponent } from './components/sale-control/sale-control.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { CustomerInformationComponent } from './components/customer-information/customer-information.component';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { ArticleHistoryComponent } from './components/article-history/article-history.component';

@NgModule({
    declarations: [
        HomeComponent,
        VehicleHistoryComponent,
        ArticleSearchComponent,
        VehicleSearchComponent,
        SaleControlComponent,
        CustomerInformationComponent,
        ArticleHistoryComponent
    ],
    imports: [
        CommonModule,
        HomeRoutingModule,
        TranslateModule,
        ArticleSearchModule,
        NgSelectModule,
        PopoverModule.forRoot(),
        ReactiveFormsModule,
        ConnectCommonModule,
        TabsModule,
        FormsModule,
        ModalModule.forRoot(),
        SagCurrencyModule,
        SagHaynesProModule,
        AngularMyDatePickerModule,
        SagTableModule
    ],
    exports: [
        VehicleHistoryComponent,
        ArticleSearchComponent,
        VehicleSearchComponent,
        SaleControlComponent,
        CustomerInformationComponent,
        ArticleHistoryComponent
    ]
})
export class HomeModule { }
