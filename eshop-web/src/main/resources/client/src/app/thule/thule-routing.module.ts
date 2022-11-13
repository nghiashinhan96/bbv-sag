import { ThuleComponent } from './thule.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [{
    path: 'add-buyers-guide',
    component: ThuleComponent,
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ThuleRoutingModule { }
