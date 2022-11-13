import { CommonModule } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { SagCurrencyModule } from 'sag-currency';
import { HaynesLinkHandleService } from './services/haynes-link-handle.service';
import { SagHaynesProReturnModalComponent } from './components/haynespro-return-modal/haynespro-return-modal.component';
import { SagHaynesProComponent } from './components/haynespro/haynespro.component';
import { SagHaynessproModalComponent } from './components/haynespro-modal/haynespro-modal.component';
import { SagHaynesproErrorModalComponent } from './components/haynespro-error-modal/haynespro-error-modal.component';
import { HaynesProService } from './services/haynespro.service';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { SagLabourTimeComponent } from './components/labour-time/labour-time.component';
import { LabourTimeService } from './services/labour-time.service';
import { HaynesProIntegrationService } from './services/haynes-pro-inregration.service';

@NgModule({
    imports: [
        FormsModule,
        CommonModule,
        SagCurrencyModule,
        NgSelectModule,
        TranslateModule
    ],
    declarations: [
        SagHaynesProComponent,
        SagHaynessproModalComponent,
        SagHaynesproErrorModalComponent,
        SagHaynesProReturnModalComponent,
        SagLabourTimeComponent
    ],
    entryComponents: [
        SagHaynessproModalComponent,
        SagHaynesproErrorModalComponent,
        SagHaynesProReturnModalComponent
    ],
    exports: [
        SagHaynesProReturnModalComponent,
        SagHaynesProComponent,
        SagLabourTimeComponent
    ]
})
export class SagHaynesProModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagHaynesProModule,
            providers: [
                HaynesLinkHandleService,
                LabourTimeService,
                HaynesProIntegrationService,
                HaynesProService
            ]
        };
    }
}
