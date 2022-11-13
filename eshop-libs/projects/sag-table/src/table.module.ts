import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { NgxPaginationModule } from 'ngx-pagination';
import { FilterPipeModule } from 'ngx-filter-pipe';

import { SagCommonModule } from 'sag-common';

import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SagCurrencyModule } from 'sag-currency';
import { SagTableComponent } from './pages/table.component';
import { SagTableConfigService } from './services/sag-table.config.service';

@NgModule({
    declarations: [
        SagTableComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        SagCommonModule,
        NgSelectModule,
        FormsModule,
        PaginationModule.forRoot(),
        FilterPipeModule,
        SagCurrencyModule.forRoot(),
        AngularMyDatePickerModule,
        NgxPaginationModule
    ],
    exports: [
        SagTableComponent
    ]
})
export class SagTableModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagTableModule,
            providers: [
                SagTableConfigService
            ]
        };
    }
}
