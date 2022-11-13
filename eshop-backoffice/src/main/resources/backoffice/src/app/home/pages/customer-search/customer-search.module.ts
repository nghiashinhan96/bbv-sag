import { NgModule } from '@angular/core';
import { CustomerSearchRoutes } from './customer-search.routes';
import { CustomerDetailsModule } from './components/customer-details/customer-details.module';

@NgModule({
    imports: [
        CustomerSearchRoutes
    ]
})
export class CustomerSearchModule { }
