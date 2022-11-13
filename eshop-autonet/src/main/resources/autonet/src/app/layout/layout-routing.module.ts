import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';
import { CateloguesComponent } from './pages/catelogues/catelogues.component';
import { AdministrationComponent } from './pages/administration/administration.component';
import { ShoppingBasketComponent } from './pages/shopping-basket/shopping-basket.component';
import { PriceOfferComponent } from './pages/price-offer/price-offer.component';
import { ShowpageComponent } from './pages/showpage/showpage.component';

const routes: Routes = [{
    path: '',
    component: LayoutComponent,
    children: [{
        path: 'cars',
        loadChildren: () => import('./../passenger-car/passenger-car.module').then(m => m.PassengerCarModule)
    }, {
        path: 'catelogues',
        component: CateloguesComponent
    }, {
        path: 'administration',
        component: AdministrationComponent
    },
    {
        path: 'shopping-basket',
        component: ShoppingBasketComponent
    }, {
        path: 'price-offers',
        component: PriceOfferComponent
    }, {
        path: 'showpage',
        component: ShowpageComponent
    }, {
        path: 'advance-vehicle-search',
        loadChildren: () => import('./../advance-vehicle-search/advance-vehicle-search.module').then(m => m.AdvanceVehicleSearchModule)
    }, {
        path: '',
        redirectTo: 'cars'
    }]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
