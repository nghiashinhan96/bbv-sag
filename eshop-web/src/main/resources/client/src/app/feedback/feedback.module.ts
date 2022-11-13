import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeedbackComponent } from './feedback.component';
import { FeedbackModalComponent } from './components/feedback-modal/feedback-modal.component';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { FeedbackService } from './services/feedback.service';
import { CustomerBusinessService } from './services/customer-business.service';
import { SalesOnBehalfBusinessService } from './services/sales-onbehalf-business.service';
import { SalesNotOnBehalfBusinessService } from './services/sales-not-onbehalf-business.service';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { SagCurrencyModule } from 'sag-currency';

@NgModule({
    declarations: [
        FeedbackComponent,
        FeedbackModalComponent
    ],
    entryComponents: [
        FeedbackModalComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule,
        NgSelectModule,
        ConnectCommonModule,
        SagCurrencyModule
    ],
    providers: [
        CustomerBusinessService,
        SalesOnBehalfBusinessService,
        SalesNotOnBehalfBusinessService,
        FeedbackService
    ],
    exports: [
        FeedbackComponent
    ]
})
export class FeedbackModule { }
