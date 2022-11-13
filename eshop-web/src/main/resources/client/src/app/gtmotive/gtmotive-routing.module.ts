import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GtmotiveComponent } from './gtmotive.component';


const routes: Routes = [
    {
        path: '',
        component: GtmotiveComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class GtmotiveRoutingModule { }
