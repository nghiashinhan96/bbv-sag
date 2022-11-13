import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { SagCommonModule } from 'sag-common';
import { GtmotiveService } from './services/gtmotive.service';
import { SagGtmotiveComponent } from './components/gtmotive/gtmotive.component';
import { SagGtmotivePartsTreeComponent } from './components/gt-parts-tree/gt-parts-tree.component';
import { SagGtmotiveMoreInfoComponent } from './components/gt-more-info/gt-more-info.component';
import { SagGtmotiveMultiPartsModalComponent } from './components/gt-multi-parts-modal/gt-multi-parts-modal.component';
import {
    SagGtmotiveAdditionalEquipmentsModalComponent
} from './components/gt-additional-equipments-modal/gt-additional-equipments-modal.component';
import { SagGtmotiveVinWarningModalComponent } from './components/gt-vin-warning-modal/gt-vin-warning-modal.component';
import { SagGtmotiveMultiGraphicModalComponent } from './components/gt-multi-graphic-modal/gt-multi-graphic-modal.component';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SagGtmotivePageComponent } from './pages/gtmotive/gtmotive.component';
import { SagGtmotiveConfigService } from './services/gtmotive-config.service';
import { ModalModule } from 'ngx-bootstrap/modal';



@NgModule({
    declarations: [
        SagGtmotivePageComponent,
        SagGtmotiveComponent,
        SagGtmotivePartsTreeComponent,
        SagGtmotiveMoreInfoComponent,
        SagGtmotiveMultiPartsModalComponent,
        SagGtmotiveAdditionalEquipmentsModalComponent,
        SagGtmotiveVinWarningModalComponent,
        SagGtmotiveMultiGraphicModalComponent,
    ],
    entryComponents: [
        SagGtmotiveMultiPartsModalComponent,
        SagGtmotiveAdditionalEquipmentsModalComponent,
        SagGtmotiveVinWarningModalComponent,
        SagGtmotiveMultiGraphicModalComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        RouterModule,
        ModalModule.forRoot(),
        ReactiveFormsModule,
        AngularMyDatePickerModule,
        SagCommonModule
    ],
    exports: [
        SagGtmotivePageComponent,
        SagGtmotiveComponent,
        SagGtmotiveMoreInfoComponent,
    ]
})
export class SagGtmotiveModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagGtmotiveModule,
            providers: [
                SagGtmotiveConfigService,
                GtmotiveService
            ]
        };
    }
}
