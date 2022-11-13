import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BatteriesComponent } from './batteries.component';


const routes: Routes = [
    {
        path: '',
        component: BatteriesComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BatteriesRoutingModule { }
