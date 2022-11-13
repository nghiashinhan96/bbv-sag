import { Component, Input, OnInit } from '@angular/core';

import { get } from 'lodash';

import { ShoppingBasketContextModel } from '../../models/shopping-basket-context.model';
import { ShoppingConditionHeaderModel } from '../../models/shopping-condition-header.model';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';
import { ShoppingBasketInfo } from 'src/app/core/models/shopping-basket.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AffiliateEnum } from 'sag-common';
import { FinalCustomerService } from 'src/app/final-customer/services/final-customer.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize } from 'rxjs/operators';
import { FinalCustomerAddressModel } from 'src/app/final-customer/models/final-customer-address.model';

@Component({
    selector: 'connect-shopping-order-condition',
    templateUrl: './shopping-order-condition.component.html',
    styleUrls: ['./shopping-order-condition.component.scss']
})
export class ShoppingOrderConditionComponent implements OnInit {
    @Input() basketContext: ShoppingBasketContextModel;
    @Input() userDetail: UserDetail;
    @Input() deliverySummary: ShoppingBasketInfo = null;
    @Input() finalOrder: OrderDashboardListItemModel = null;

    conditionHeader: ShoppingConditionHeaderModel = new ShoppingConditionHeaderModel();

    finalCustomerAddress = '';
    collectiveType = 'COLLECTIVE_DELIVERY2';
    cz = AffiliateEnum.CZ;
    
    constructor(
        public finalCustomerService: FinalCustomerService
    ) { }

  

    ngOnInit() {
        if (this.basketContext) {
            this.conditionHeader = new ShoppingConditionHeaderModel(this.basketContext);
        }
        if (this.userDetail && this.userDetail.isFinalUserRole) {
            this.generateFinalCustomerAddress();
        }
        this.generateCollectiveType();
    }

    generateCollectiveType() {
        if (!this.userDetail.isFinalUserRole) {
            this.collectiveType = get(this.basketContext, 'collectionDelivery.descCode');
        }
    }

    generateFinalCustomerAddress() {
        SpinnerService.start();
        this.finalCustomerService.getFinalCustomerAddress().pipe(
            finalize(() => {
                SpinnerService.stop();
            })
        ).subscribe((res: FinalCustomerAddressModel) => {
            this.finalCustomerAddress = [
                res.customerName,
                res.street,
                res.address1,
                res.address2,
                res.postCode,
                res.place
            ].filter(item => (item || '').trim()).join(', ');
        });
    }

}
