import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MyCustomerOrderComponent } from './pages/my-customer-order/my-customer-order.component';
import { NewOrderComponent } from './pages/new-order/new-order.component';
import { OrderedComponent } from './pages/ordered/ordered.component';
import { OrderedDetailComponent } from './pages/ordered-detail/ordered-detail.component';

const routes: Routes = [{
    path: '',
    redirectTo: 'new-orders',
    pathMatch: 'full'
}, {
    path: 'new-orders',
    component: NewOrderComponent
}, {
    path: 'my-customer-orders',
    component: MyCustomerOrderComponent
}, {
    path: 'ordered',
    component: OrderedComponent
}, {
    path: 'ordered/:orderId',
    component: OrderedDetailComponent
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OrderDashboardRoutingModule { }
