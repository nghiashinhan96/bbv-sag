import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BatteriesRoutingModule } from './batteries-routing.module';
import { BatteriesComponent } from './batteries.component';
import { TranslateModule } from '@ngx-translate/core';
import { BatteryFiltersComponent } from './components/battery-filters/battery-filters.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { NgSelectModule } from '@ng-select/ng-select';
import { BatteriesService } from './services/batteries.service';
import { FormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';
import { PopoverModule } from 'ngx-bootstrap/popover';

@NgModule({
    declarations: [
        BatteriesComponent,
        BatteryFiltersComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        BatteriesRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        NgSelectModule,
        NgxWebstorageModule,
        ConnectCommonModule,
        AnalyticLoggingModule,
        PopoverModule.forRoot()
    ],
    providers: [
        BatteriesService
    ]
})
export class BatteriesModule { }
