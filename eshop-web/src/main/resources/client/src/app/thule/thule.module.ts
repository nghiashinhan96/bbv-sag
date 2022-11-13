import { NgModule } from '@angular/core';
import { SagLibThuleModule } from 'sag-thule';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { CommonModule } from '@angular/common';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';
import { ThuleComponent } from './thule.component';
import { ThuleRoutingModule } from './thule-routing.module';

@NgModule({
    declarations: [
        ThuleComponent
    ],
    imports: [
        CommonModule,
        SagLibThuleModule,
        ThuleRoutingModule,
        ConnectCommonModule,
        AnalyticLoggingModule
    ]
})
export class ThuleModule { }
