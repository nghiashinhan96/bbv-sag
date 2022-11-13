import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { tap } from 'rxjs/operators';
import { FinalCustomerAddressModel } from 'src/app/final-customer/models/final-customer-address.model';
import { FinalCustomerService } from 'src/app/final-customer/services/final-customer.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-final-customer-address',
    templateUrl: './final-customer-address.component.html',
    styleUrls: ['./final-customer-address.component.scss']
})
export class FinalCustomerAddressComponent implements OnInit, OnDestroy {
    @Input() orgId: number = null;

    customerAddress$: Observable<FinalCustomerAddressModel>;
    subs = new SubSink();

    constructor(
        private finalCustomerService: FinalCustomerService,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        if (this.orgId) {
            this.customerAddress$ = this.finalCustomerService.getFinalCustomer(this.orgId, true).pipe(
                tap(() => SpinnerService.stop('.connect-final-customer-address')),

            );
            this.subs.sink = this.finalCustomerService.getFinalCustomer(this.orgId).subscribe(res => {
                this.appStorage.goodReceiver = res;
            });
        } else {
            this.customerAddress$ = this.finalCustomerService.getFinalCustomerAddress().pipe(
                tap(() => SpinnerService.stop('.connect-final-customer-address'))
            );
        }

    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
