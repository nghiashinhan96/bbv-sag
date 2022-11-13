import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HasPermissionDirective } from './directives/has-permission.directive';
import { EnabledDirective } from './directives/enabled.directive';
import { ResponseMessageDirective } from './directives/response-message.directive';
import { CustomerRefTextComponent } from './components/customer-ref-text/customer-ref-text.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SaveBasketComponent } from './components/save-basket/save-basket.component';
import { ExpandedComponent } from './components/expanded/expanded.component';
import { ControlMessagesComponent } from './control-message/control-message.component';
import { CustomerInfoComponent } from './components/customer-info/customer-info.component';
import { AdditionalInfoComponent } from './components/additional-info/additional-info.component';
import { SystemMessageComponent } from './components/system-message/system-message.component';
import { MarkedPipe } from './pipes/marked.pipe';
import { NotificationComponent } from './components/notification/notification.component';
import { ScrollTopComponent } from './components/scroll-top/scroll-top.component';
import { ConnectActionComponent } from './components/connect-action/action.component';
import { FooterComponent } from './components/footer/footer.component';
import { InputStateDirective } from './directives/input-state.directive';
import { SagCommonModule } from 'sag-common';
import { CustomerMemoComponent } from './components/customer-memo/customer-memo.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { OrderDatePipe } from './pipes/order-date.pipe';
import { NgSelectModule } from '@ng-select/ng-select';
import { SearchHistoryModalComponent } from './components/search-history-modal/search-history-modal.component';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SagTableModule } from 'sag-table';
import { ArticleReplaceModalComponent } from './components/article-replace-modal/article-replace-modal.component';
import { CzCustomModule } from '../cz-custom/cz-custom.module';
import { SagArticleListModule } from 'sag-article-list';
import { AccessoryListModalComponent } from './components/article-accessories-modal/article-accessories-modal.component';
import { PartsListModalComponent } from './components/article-parts-list-modal/article-parts-list-modal.component';
import { TourPlanInfoComponent } from './components/tour-plan-info/tour-plan-info.component';
import { ShoppingExportModalComponent } from './components/shopping-export-modal/shopping-export-modal.component';
import { ArticleCrossReferenceModalComponent } from './components/article-cross-reference-modal/article-cross-reference-modal.component';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { SliderBarChartComponent } from './components/slider-bar-chart/slider-bar-chart.component';
import { ChartsModule, ThemeService } from 'ng2-charts';
import { SagCurrencyModule } from 'sag-currency';
import { SliderBarChartContainerComponent } from './components/slider-bar-chart-container/slider-bar-chart-container.component';
@NgModule({
    declarations: [
        HasPermissionDirective,
        EnabledDirective,
        ResponseMessageDirective,
        CustomerRefTextComponent,
        SaveBasketComponent,
        ExpandedComponent,
        ControlMessagesComponent,
        CustomerInfoComponent,
        AdditionalInfoComponent,
        SystemMessageComponent,
        MarkedPipe,
        OrderDatePipe,
        NotificationComponent,
        ScrollTopComponent,
        ConnectActionComponent,
        FooterComponent,
        InputStateDirective,
        CustomerMemoComponent,
        SearchHistoryModalComponent,
        SearchHistoryModalComponent,
        ArticleReplaceModalComponent,
        AccessoryListModalComponent,
        PartsListModalComponent,
        ArticleCrossReferenceModalComponent,
        TourPlanInfoComponent,
        ShoppingExportModalComponent,
        SliderBarChartComponent,
        SliderBarChartContainerComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule,
        SagCommonModule,
        PopoverModule.forRoot(),
        NgSelectModule,
        AngularMyDatePickerModule,
        SagTableModule,
        CzCustomModule,
        SagArticleListModule,
        NgxSliderModule,
        ChartsModule ,
        SagCurrencyModule
    ],
    exports: [
        HasPermissionDirective,
        EnabledDirective,
        ResponseMessageDirective,
        CustomerRefTextComponent,
        SaveBasketComponent,
        ExpandedComponent,
        ControlMessagesComponent,
        CustomerInfoComponent,
        AdditionalInfoComponent,
        SystemMessageComponent,
        NotificationComponent,
        ScrollTopComponent,
        ConnectActionComponent,
        FooterComponent,
        CustomerMemoComponent,
        SagCommonModule,
        OrderDatePipe,
        SagArticleListModule,
        MarkedPipe,
        TourPlanInfoComponent,
        SliderBarChartComponent,
        SliderBarChartContainerComponent
    ],
    entryComponents: [
        SearchHistoryModalComponent,
        ArticleReplaceModalComponent,
        AccessoryListModalComponent,
        PartsListModalComponent,
        ArticleCrossReferenceModalComponent,
        ShoppingExportModalComponent
    ],
    providers: [
        ThemeService
    ]
})
export class ConnectCommonModule { }
