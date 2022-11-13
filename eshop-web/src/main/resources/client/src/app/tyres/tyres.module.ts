import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TyresRoutingModule } from './tyres-routing.module';
import { TyresComponent } from './tyres.component';
import { TranslateModule } from '@ngx-translate/core';
import { PkwTyreFiltersComponent } from './components/pkw-tyre-filters/pkw-tyre-filters.component';
import { MotorTyreFiltersComponent } from './components/motor-tyre-filters/motor-tyre-filters.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { NgSelectModule } from '@ng-select/ng-select';
import { TyresService } from './services/tyres.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';

@NgModule({
    declarations: [
        TyresComponent,
        PkwTyreFiltersComponent,
        MotorTyreFiltersComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TyresRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        PopoverModule.forRoot(),
        NgSelectModule,
        NgxWebstorageModule,
        ConnectCommonModule,
        AnalyticLoggingModule
    ],
    providers: [
        TyresService
    ]
})
export class TyresModule { }
