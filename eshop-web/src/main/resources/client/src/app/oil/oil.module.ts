import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OilRoutingModule } from './oil-routing.module';
import { OilComponent } from './oil.component';
import { TranslateModule } from '@ngx-translate/core';
import { OilFiltersComponent } from './components/oil-filters/oil-filters.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { NgSelectModule } from '@ng-select/ng-select';
import { OilService } from './services/oil.service';
import { FormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';

@NgModule({
    declarations: [
        OilComponent,
        OilFiltersComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        OilRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        NgSelectModule,
        NgxWebstorageModule,
        ConnectCommonModule,
        AnalyticLoggingModule
    ],
    providers: [
        OilService
    ]
})
export class OilModule { }
