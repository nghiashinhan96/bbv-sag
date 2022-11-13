import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { STGInfoComponent } from './stg-info.component';


const routes: Routes = [
    {
        path: '',
        component: STGInfoComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class STGInfoRoutingModule { }
