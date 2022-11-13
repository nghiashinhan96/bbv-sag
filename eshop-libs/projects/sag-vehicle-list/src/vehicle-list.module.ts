import { NgModule, ModuleWithProviders, InjectionToken } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SagLibVehicleListComponent } from './vehicle-list/vehicle-list.component';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagCommonModule } from 'sag-common';

import { VehicleListService } from './services/vehicle-list.service';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { SagCurrencyModule } from 'sag-currency';
import { RouterModule } from '@angular/router';
import { SagVehicleListConfigService } from './services/vehicle-list-config.service';
import { SagAdvanceVehicleSearchMakesComponent } from './pages/advance-vehicle-search-makes/advance-vehicle-search-makes.component';
import { SagAdvanceVehicleSearchBreadcrumbComponent } from './components/advance-vehicle-search-breadcumb/advance-vehicle-search-breadcumb.component';
import { SagAdvanceVehicleSearchModelsComponent } from './pages/advance-vehicle-search-models/advance-vehicle-search-models.component';
import { SagTableListSearchComponent } from './components/table-list-search/table-list-search.component';
import { SagAdvanceVehicleSearchTypesComponent } from './pages/advance-vehicle-search-types/advance-vehicle-search-types.component';


@NgModule({
  declarations: [
    SagLibVehicleListComponent,
    SagAdvanceVehicleSearchMakesComponent,
    SagAdvanceVehicleSearchBreadcrumbComponent,
    SagAdvanceVehicleSearchModelsComponent,
    SagTableListSearchComponent,
    SagAdvanceVehicleSearchTypesComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule,
    SagCommonModule,
    FormsModule,
    NgSelectModule,
    SagCurrencyModule,
    PopoverModule.forRoot(),
  ],
  exports: [
    SagLibVehicleListComponent,
    SagAdvanceVehicleSearchMakesComponent,
    SagAdvanceVehicleSearchBreadcrumbComponent,
    SagAdvanceVehicleSearchModelsComponent,
    SagTableListSearchComponent,
    SagAdvanceVehicleSearchTypesComponent,
  ]
})
export class SagVehicleListModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SagVehicleListModule,
      providers: [
        SagVehicleListConfigService,
        VehicleListService
      ]
    };
  }
}
