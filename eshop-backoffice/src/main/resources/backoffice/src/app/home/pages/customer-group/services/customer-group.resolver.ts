import { Injectable } from '@angular/core';
import {
    Resolve,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
} from '@angular/router';

import { forkJoin, of } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';

import { CustomerGroupDetailModel } from '../models/customer-group-detail.model';
import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { CustomerGroupService } from '../../../services/customer-group/customer-group.service';

@Injectable()
export class CustomerGroupResolver implements Resolve<any> {
    constructor(
        private affiliateService: AffiliateService,
        private customerGroupService: CustomerGroupService
    ) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params.collectionShortName;
        const affiliate = route.queryParams.affiliate || ' ';
        let detail$;
        if (id === 'new') {
            detail$ = of(new CustomerGroupDetailModel());
        } else {
            detail$ = this.customerGroupService.getCustomerGroup(id);
        }
        return forkJoin([this.affiliateService.getShortInfos(), detail$]).pipe(
            map((res) => {
                return {
                    affiliates: res[0],
                    customerGroupDetail: res[1],
                    affiliate,
                };
            })
        );
    }
}
