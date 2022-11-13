import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VehicleFilteringRoutingModule } from './vehicle-filtering-routing.module';
import { VehicleFilteringComponent } from './vehicle-filtering.component';
import { AutonetCommonModule } from 'src/app/shared/autonet-common/autonet-common.module';
import { SagVehicleListModule } from 'sag-vehicle-list';


@NgModule({
  declarations: [VehicleFilteringComponent],
  imports: [
    CommonModule,
    VehicleFilteringRoutingModule,
    AutonetCommonModule,
    SagVehicleListModule
  ]
})
export class VehicleFilteringModule { }
