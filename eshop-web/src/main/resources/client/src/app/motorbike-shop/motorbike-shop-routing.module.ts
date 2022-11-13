import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MotorbikeShopComponent } from './motorbike-shop.component';


const routes: Routes = [
  {
    path: '',
    component: MotorbikeShopComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MotorbikeShopRoutingModule { }
