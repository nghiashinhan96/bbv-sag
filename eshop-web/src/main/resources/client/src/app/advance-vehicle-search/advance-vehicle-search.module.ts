import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { SagVehicleListModule } from 'sag-vehicle-list';
import { AdvanceVehicleSearchRoutingModule } from './advance-vehicle-search-routing.module';
import { AdvanceVehicleSearchMakesComponent } from './pages/advance-vehicle-search-makes/advance-vehicle-search-makes.component';
import { AdvanceVehicleSearchModelsComponent } from './pages/advance-vehicle-search-models/advance-vehicle-search-models.component';
import { AdvanceVehicleSearchTypesComponent } from './pages/advance-vehicle-search-types/advance-vehicle-search-types.component';


@NgModule({
    declarations: [
        AdvanceVehicleSearchMakesComponent,
        AdvanceVehicleSearchModelsComponent,
        AdvanceVehicleSearchTypesComponent,
    ],
    imports: [
        CommonModule,
        ConnectCommonModule,
        AdvanceVehicleSearchRoutingModule,
        SagVehicleListModule
    ]
})
export class AdvanceVehicleSearchModule { }
