import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdvanceVehicleSearchMakesComponent } from './pages/advance-vehicle-search-makes/advance-vehicle-search-makes.component';
import { AdvanceVehicleSearchModelsComponent } from './pages/advance-vehicle-search-models/advance-vehicle-search-models.component';
import { AdvanceVehicleSearchTypesComponent } from './pages/advance-vehicle-search-types/advance-vehicle-search-types.component';


const routes: Routes = [
  { path: 'makes', component: AdvanceVehicleSearchMakesComponent },
  { path: 'models', component: AdvanceVehicleSearchModelsComponent },
  { path: 'types', component: AdvanceVehicleSearchTypesComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdvanceVehicleSearchRoutingModule { }
