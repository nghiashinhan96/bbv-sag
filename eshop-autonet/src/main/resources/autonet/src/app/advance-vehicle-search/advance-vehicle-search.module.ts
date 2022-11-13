import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { AutonetCommonModule } from 'src/app/shared/autonet-common/autonet-common.module';

import { AdvanceVehicleSearchRoutingModule } from './advance-vehicle-search-routing.module';
import { AdvanceVehicleSearchComponent } from './advance-vehicle-search.component';
import { AdvanceVehicleSearchMakesComponent } from './pages/advance-vehicle-search-makes/advance-vehicle-search-makes.component';
import { AdvanceVehicleSearchModelsComponent } from './pages/advance-vehicle-search-models/advance-vehicle-search-models.component';
import { AdvanceVehicleSearchTypesComponent } from './pages/advance-vehicle-search-types/advance-vehicle-search-types.component';

import { SagVehicleListModule } from 'sag-vehicle-list';

@NgModule({
    declarations: [
        AdvanceVehicleSearchComponent,
        AdvanceVehicleSearchMakesComponent,
        AdvanceVehicleSearchModelsComponent,
        AdvanceVehicleSearchTypesComponent,
    ],
    imports: [
        CommonModule,
        TranslateModule,
        AutonetCommonModule,
        SagVehicleListModule,
        AdvanceVehicleSearchRoutingModule,
    ]
})
export class AdvanceVehicleSearchModule { }
