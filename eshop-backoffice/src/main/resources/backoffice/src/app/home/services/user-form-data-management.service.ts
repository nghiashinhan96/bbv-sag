import { Injectable, Injector } from '@angular/core';
import { Subject } from 'rxjs';

import { CustomerQuery } from '../pages/user-management/models/customer-query.model';
import { CustomerInfo } from '../models/customer/customer-info';
import { CustomerService } from './customer/customer.service';

@Injectable()
export class UserFormDataManagementService {
    component;

    customerQuery: CustomerQuery;

    constructor(
        private customerService: CustomerService,
        private injector: Injector
    ) {
        this.customerQuery = new CustomerQuery();
    }

    createInstance(component) {
        const userFormDataService = this.injector.get(UserFormDataManagementService);
        userFormDataService.component = component;
        return userFormDataService;
    }

    setCustomerQuery(query) {
        this.customerQuery = query;
    }

    mergeUserQuery(query) {
        this.customerQuery = this.customerQuery || new CustomerQuery();
        Object.assign(this.customerQuery, query);
    }

    fetchCustomer() {
        const customerSource = new Subject<any>();
        this.customerService.getCustomerInfo(this.customerQuery).subscribe(
            (res) => {
                customerSource.next(new CustomerInfo(res));
            },
            (e) => customerSource.error(e),
            () => customerSource.complete()
        );
        return customerSource.asObservable();
    }

    fetchCustomers() {
        const customerSource = new Subject<any>();
        this.customerService.getCustomerInfo(this.customerQuery).subscribe(
            (result) => {
                const res: any = result;
                customerSource.next(new CustomerInfo(res.content));
            },
            (e) => customerSource.error(e),
            () => customerSource.complete()
        );
        return customerSource.asObservable();
    }
}
