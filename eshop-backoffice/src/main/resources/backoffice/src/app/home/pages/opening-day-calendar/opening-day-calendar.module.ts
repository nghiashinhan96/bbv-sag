import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { SagTableModule } from 'sag-table';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';

import { OpeningDayCalendarRoutingModule } from './opening-day-calendar.routes';
import { FilterDateService } from './service/filter-date.service';
import { OpeningDayDetailResolverService } from './service/opening-day-detail-resolver.service';
import { OpeningDayCalendarService } from './service/opening-day-calendar.service';
import { OpeningDayPageComponent } from './components/opening-day-page/opening-day-page.component';
import { OpeningDayDateFilterComponent } from './components/opening-day-date-filter/opening-day-date-filter.component';
import { OpeningDayListComponent } from './components/opening-day-list/opening-day-list.component';
import { OpeningDayFormComponent } from './components/opening-day-form/opening-day-form.component';
import { OpeningDayListItemComponent } from './components/opening-day-list-item/opening-day-list-item.component';
import { OpeningDayFormPageComponent } from './components/opening-day-form-page/opening-day-form-page.component';
import { OpeningDayListResolverService } from './service/opening-day-list-resolver.service';
import { SharedModules } from 'src/app/shared/components/shared-components.module';

@NgModule({
    imports: [
        CommonModule,
        OpeningDayCalendarRoutingModule,
        SharedModules,
        TranslateModule,
        AngularMyDatePickerModule,
        NgSelectModule,
        FormsModule,
        ReactiveFormsModule,
        AngularMyDatePickerModule,
        SagTableModule
    ],
    declarations: [
        OpeningDayPageComponent,
        OpeningDayDateFilterComponent,
        OpeningDayListComponent,
        OpeningDayFormComponent,
        OpeningDayListItemComponent,
        OpeningDayFormPageComponent
    ],
    providers: [FilterDateService, OpeningDayDetailResolverService, OpeningDayCalendarService, OpeningDayListResolverService]
})
export class OpeningDayCalendarModule { }
