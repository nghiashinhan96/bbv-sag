import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { FinalCustomerSearchRequestModel } from '../models/final-customer-search-request.model';
import { map, catchError } from 'rxjs/operators';
import { of, Observable } from 'rxjs';
import { FinalCustomerModel } from '../models/final-customer.model';
import { FinalCustomerAddressModel } from '../models/final-customer-address.model';

@Injectable({
    providedIn: 'root'
})
export class FinalCustomerService {

    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient) { }

    searchFinalCustomers(req: FinalCustomerSearchRequestModel) {
        const url = `${this.baseUrl}final-customer/search?page=${req.pageNr}&size=${req.size}`;
        return this.http.post(url, req.criteria).pipe(
            catchError(err => {
                return of({});
            })
        );
    }

    getFinalCustomer(orgId: number, modeViewAddress = false) {
        const url = `${this.baseUrl}final-customer/${orgId}/info?fullMode=${true}`;
        return this.http.get(url).pipe(
            catchError(error => of(null)),
            map(res => {
                if (modeViewAddress) {
                    const finalUser = new FinalCustomerModel(res);
                    return this.getAddress(finalUser);
                } else {
                    return new FinalCustomerModel(res);
                }
            }), catchError(error => of(null)));
    }

    getFinalCustomerAddress() {
        const url = `${this.baseUrl}final-customer/info?fullMode=${true}`;
        return this.http.get(url).pipe(
            map(res => {
                const finalUser = new FinalCustomerModel(res);
                return this.getAddress(finalUser);
            }),
            catchError(error => of(null))
        );
    }

    private getAddress(finalUser: FinalCustomerModel) {
        const address = new FinalCustomerAddressModel();

        Object.assign(address, {
            address1: finalUser.customerProperties.address1,
            address2: finalUser.customerProperties.address2,
            customerName: finalUser.name,
            place: finalUser.customerProperties.place,
            postCode: finalUser.customerProperties.postCode,
            street: finalUser.customerProperties.street
        });

        return address;
    }
}
