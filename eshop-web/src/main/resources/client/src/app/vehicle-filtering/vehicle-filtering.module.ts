import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VehicleFilteringRoutingModule } from './vehicle-filtering-routing.module';
import { VehicleFilteringComponent } from './vehicle-filtering/vehicle-filtering.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { SagVehicleListModule } from 'sag-vehicle-list';


@NgModule({
    declarations: [VehicleFilteringComponent],
    imports: [
        CommonModule,
        ConnectCommonModule,
        VehicleFilteringRoutingModule,
        SagVehicleListModule
    ]
})
export class VehicleFilteringModule { }
