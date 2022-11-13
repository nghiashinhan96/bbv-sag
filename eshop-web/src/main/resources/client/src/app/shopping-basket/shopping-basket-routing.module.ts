import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ShoppingBasketComponent } from './shopping-basket.component';
import { ShoppingCartComponent } from './pages/shopping-cart/shopping-cart.component';
import { ShoppingOrderComponent } from './pages/shopping-order/shopping-order.component';
import { ShoppingCheckoutComponent } from './pages/shopping-checkout/shopping-checkout.component';
import { ShoppingBasketResolver } from './shopping-basket.resolver';
import { CanDeactivateGuard } from '../core/services/deactive-guard.service';
import { CanDeactivateCheckoutGuard } from './pages/shopping-checkout/checkout-deactivate';


const routes: Routes = [{
    path: '',
    component: ShoppingBasketComponent,
    resolve: {
        basket: ShoppingBasketResolver
    },
    canDeactivate: [CanDeactivateGuard],
    children: [
        {
            path: '',
            pathMatch: 'full',
            redirectTo: 'cart'
        },
        {
            path: 'cart',
            component: ShoppingCartComponent
        },
        {
            path: 'order',
            component: ShoppingOrderComponent
        },
        {
            path: 'checkout',
            component: ShoppingCheckoutComponent,
            canDeactivate: [CanDeactivateCheckoutGuard]
        }
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ShoppingBasketRoutingModule { }
