import { Injectable } from '@angular/core';

import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';

import { CustomerSearch } from '../../models/customer/customer-search.model';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CustomerService {
    private static MAX_TIMEOUT_IN_SECOND = 14400;

    constructor(
        private http: HttpClient
    ) { }

    private customerSubj = new BehaviorSubject<CustomerSearch>(
        new CustomerSearch()
    );

    public static validateTimeout(timeoutInMunite): boolean {
        if (isNaN(timeoutInMunite)) {
            return false;
        }
        return (
            60 <= timeoutInMunite &&
            timeoutInMunite <= CustomerService.MAX_TIMEOUT_IN_SECOND
        );
    }

    searchCustomer(searchData: any): Observable<CustomerSearch> {
        const url = ApiUtil.getUrl(`admin/customers/search`);
        return this.http.post(url, searchData).pipe(
            map((res) => {
                const customerData: any = res;
                this.customerSubj.next(customerData);
                return customerData;
            })
        ) as Observable<any>;
    }
    /**
     *
     * @param customer Get customer info by affiliate and customerNr
     */
    getCustomerInfo(customer) {
        const url = ApiUtil.getUrl(`admin/customers/info`);
        return this.http.post(url, customer);
    }

    getCustomerSetting(customerNr) {
        const url = ApiUtil.getUrl(`admin/customers/settings?customerNr=${customerNr}`);
        return this.http.get(url);
    }

    getCustomerLicence(customerNr, pageSize = 10, pageNumber?) {
        pageNumber = pageNumber - 1 || 0; // differences between sv & paging module
        const url = ApiUtil.getUrl(`admin/customers/licenses?customerNr=${customerNr}&pageSize=${pageSize}&pageNumber=${pageNumber}`);
        return this.http.get(url);
    }

    getAllLicenseTypes() {
        const url = ApiUtil.getUrl(`admin/customers/license/packages`);
        return this.http.get(url);
    }

    createCustomerLicense(data) {
        const url = ApiUtil.getUrl(`admin/customers/assign/license`)
        return this.http.post(url, data);
    }

    updateLicense(data, licenceId) {
        const url = ApiUtil.getUrl(`admin/customers/license/${licenceId}/update`)
        return this.http.put(url, data);
    }

    deleteLicense(id: number) {
        const url = ApiUtil.getUrl(`admin/customers/license/${id}/delete`);
        return this.http.delete(url);
    }

    updateCustomerSetting(data) {
        const url = ApiUtil.getUrl('admin/customers/settings/update');
        return this.http.post(url, data);
    }

    extractResponseData(res) {
        return res.json() || [];
    }

    register(values) {
        const url = ApiUtil.getUrl('admin/customers/registration/register');
        return this.http.post(url, values);
    }

    checkRegistrationInfo(values) {
        const url = ApiUtil.getUrl('admin/customers/registration/info');
        return this.http.post(url, values);
    }

    getCustomerCollection(collectionShortName: string) {
        const url = ApiUtil.getUrl(`admin/customers/collection/settings?collectionShortName=${collectionShortName}`);
        return this.http.get(url);
    }
}
