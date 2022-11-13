import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BulbsComponent } from './bulbs.component';


const routes: Routes = [
    {
        path: '',
        component: BulbsComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BulbsRoutingModule { }
