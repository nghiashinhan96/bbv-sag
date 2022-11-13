import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { SagTableModule } from 'sag-table';

import { CustomerResultsComponent } from './customer-results.component';
import { CustomerResultsRoutes } from './customer-results.routes';
import { HomeMenuModule } from 'src/app/home/home-menu/home-menu.module';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
@NgModule({
    declarations: [CustomerResultsComponent],
    imports: [
        CustomerResultsRoutes,
        CommonModule,
        SharedModules,
        SagTableModule,
        HomeMenuModule,
        TranslateModule
    ],
    exports: [CustomerResultsComponent],
})
export class CustomerResultsModule { }
