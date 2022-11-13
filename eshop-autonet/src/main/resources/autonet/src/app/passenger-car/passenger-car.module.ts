import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PassengerCarRoutingModule } from './passenger-car-routing.module';
import { PassengerCarComponent } from './pages/passenger-car/passenger-car.component';
import { PassengerCarArticleSearchComponent } from './component/passenger-car-article-search/passenger-car-article-search.component';
import { PassengerCarVehicleSearchComponent } from './component/passenger-car-vehicle-search/passenger-car-vehicle-search.component';
import {
    PassengerCarArticleHistorySearchComponent
} from './component/passenger-car-article-history-search/passenger-car-article-history-search.component';
import {
    PassengerCarVehicleHistorySearchComponent
} from './component/passenger-car-vehicle-history-search/passenger-car-vehicle-history-search.component';
import { TranslateModule } from '@ngx-translate/core';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AutonetCommonModule } from 'src/app/shared/autonet-common/autonet-common.module';
import { ArticleFreeTextSearchComponent } from './component/article-free-text-search/article-free-text-search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CarContainerComponent } from './pages/car-container/car-container.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ArticleSearchModule } from 'sag-article-search';
import { SearchHistoryModalComponent } from './component/search-history-modal/search-history-modal.component';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';

@NgModule({
    declarations: [
        PassengerCarComponent,
        PassengerCarArticleSearchComponent,
        PassengerCarArticleHistorySearchComponent,
        PassengerCarVehicleSearchComponent,
        PassengerCarVehicleHistorySearchComponent,
        CarContainerComponent,
        SearchHistoryModalComponent
    ],
    imports: [
        CommonModule,
        PassengerCarRoutingModule,
        TranslateModule,
        ArticleSearchModule,
        AutonetCommonModule,
        ReactiveFormsModule,
        TabsModule.forRoot(),
        AngularMyDatePickerModule,
        NgSelectModule,
        SagTableModule
    ],
    entryComponents: [
        SearchHistoryModalComponent
    ]
})
export class PassengerCarModule { }
