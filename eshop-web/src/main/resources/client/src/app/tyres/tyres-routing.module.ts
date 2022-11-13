import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TyresComponent } from './tyres.component';


const routes: Routes = [
    {
        path: '',
        component: TyresComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TyresRoutingModule { }
