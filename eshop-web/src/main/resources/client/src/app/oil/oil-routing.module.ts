import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OilComponent } from './oil.component';


const routes: Routes = [
    {
        path: '',
        component: OilComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OilRoutingModule { }
