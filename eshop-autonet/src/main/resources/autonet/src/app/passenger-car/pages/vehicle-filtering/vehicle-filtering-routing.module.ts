import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { VehicleFilteringComponent } from './vehicle-filtering.component';

const routes: Routes = [{
  path: '',
  component: VehicleFilteringComponent
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VehicleFilteringRoutingModule { }
