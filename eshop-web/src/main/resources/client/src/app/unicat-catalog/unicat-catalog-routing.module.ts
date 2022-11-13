import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UnicatCatalogComponent } from './unicat-catalog.component';


const routes: Routes = [
    {
        path: '',
        component: UnicatCatalogComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UnicatCatalogRoutingModule { }
