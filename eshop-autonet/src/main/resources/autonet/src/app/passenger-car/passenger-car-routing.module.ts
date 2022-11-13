import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PassengerCarComponent } from './pages/passenger-car/passenger-car.component';
import { CarContainerComponent } from './pages/car-container/car-container.component';


const routes: Routes = [
    {
        path: '',
        component: CarContainerComponent,
        children: [
            {
                path: 'article',
                loadChildren: () => import('./pages/articles-non-context/articles-non-context.module').then(m => m.ArticlesNonContextModule)
            }, {
                path: 'vehicle/filtering',
                loadChildren: () => import('./pages/vehicle-filtering/vehicle-filtering.module').then(m => m.VehicleFilteringModule)
            }, {
                path: 'vehicle/:vehicleId',
                loadChildren: () => import('./pages/articles-in-context/articles-in-context.module').then(m => m.ArticlesInContextModule)
            },
            {
                path: '',
                component: PassengerCarComponent
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PassengerCarRoutingModule { }
