import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BulbsRoutingModule } from './bulbs-routing.module';
import { BulbsComponent } from './bulbs.component';
import { TranslateModule } from '@ngx-translate/core';
import { BulbFiltersComponent } from './components/bulb-filters/bulb-filters.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { NgSelectModule } from '@ng-select/ng-select';
import { BulbsService } from './services/bulbs.service';
import { FormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';

@NgModule({
    declarations: [
        BulbsComponent,
        BulbFiltersComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        BulbsRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        NgSelectModule,
        NgxWebstorageModule,
        ConnectCommonModule,
        AnalyticLoggingModule
    ],
    providers: [
        BulbsService
    ]
})
export class BulbsModule { }
