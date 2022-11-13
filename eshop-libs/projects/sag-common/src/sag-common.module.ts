import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TranslateModule } from '@ngx-translate/core';

import { SagCommonTextEllipsisDirective } from './directives/text-ellipsis.directive';
import { GrossPriceKeyPipe } from './pipes/gross-price-key.pipe';
import { SagCommonVinLengthDirective } from './directives/vin-length.directive';
import { SagCommonAlphanumericSpaceDirective } from './directives/alphanumeric-space.directive';
import { SagCommonSortableDirective } from './directives/sortable.directive';
import { SagConfirmationBoxComponent } from './components/sag-confirmation-box/sag-confirmation-box.component';
import { SagMessageComponent } from './components/sag-message/sag-message.component';
import { SagControlMessagesComponent } from './components/sag-control-message/sag-control-message.component';
import { SagSaveButtonComponent } from './components/sag-save-button/save-button.component';
import { InfoUrlPipe } from './pipes/info-url.pipe';
import { CommonConfigService } from './services/common-config.service';
import { SagAdserverComponent } from './components/sag-adserver/sag-adserver.component';
import { SagActionComponent } from './components/sag-action/sag-action.component';
import { BroadcastService } from './services/broadcast.service';
import { SagDropdownButtonComponent } from './components/sag-dropdown-button/sag-dropdown-button.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { SagCommonAutofocusDirective } from './directives/autofocus.directive';
import { SafePipe } from './pipes/safe-html.pipe';
import { MarkedHtmlPipe } from './pipes/marked-html.pipe';
import { SagCommonShowHideByAffiliateDirective } from './directives/show-hide-by-affiliate.directive';
import { ScrollTrackerDirective } from './directives/scroll-tracker.directive';
import { SagLongTextDisplayComponent } from './components/sag-long-text-display/sag-long-text-display.component';
import { SagFlexRowDirective } from './directives/flex-row.directive';
@NgModule({
    declarations: [
        SagCommonAlphanumericSpaceDirective,
        SagCommonSortableDirective,
        SagCommonTextEllipsisDirective,
        GrossPriceKeyPipe,
        InfoUrlPipe,
        SagCommonVinLengthDirective,
        SagMessageComponent,
        SagConfirmationBoxComponent,
        SagControlMessagesComponent,
        SagSaveButtonComponent,
        SagDropdownButtonComponent,
        SagAdserverComponent,
        SagActionComponent,
        SagCommonAutofocusDirective,
        SafePipe,
        MarkedHtmlPipe,
        SagCommonShowHideByAffiliateDirective,
        ScrollTrackerDirective,
        SagLongTextDisplayComponent,
        SagFlexRowDirective
    ],
    entryComponents: [
        SagConfirmationBoxComponent
    ],
    providers: [
        GrossPriceKeyPipe,
        SafePipe,
        MarkedHtmlPipe
    ],
    imports: [
        CommonModule,
        TranslateModule,
        BsDropdownModule.forRoot()
    ],
    exports: [
        SagCommonAlphanumericSpaceDirective,
        SagCommonSortableDirective,
        SagCommonTextEllipsisDirective,
        GrossPriceKeyPipe,
        InfoUrlPipe,
        SagCommonVinLengthDirective,
        SagMessageComponent,
        SagConfirmationBoxComponent,
        SagControlMessagesComponent,
        SagSaveButtonComponent,
        SagDropdownButtonComponent,
        SagAdserverComponent,
        SagActionComponent,
        SagCommonAutofocusDirective,
        SafePipe,
        MarkedHtmlPipe,
        SagCommonShowHideByAffiliateDirective,
        ScrollTrackerDirective,
        SagLongTextDisplayComponent,
        SagFlexRowDirective
    ]
})
export class SagCommonModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagCommonModule,
            providers: [
                CommonConfigService,
                BroadcastService
            ]
        };
    }
}
