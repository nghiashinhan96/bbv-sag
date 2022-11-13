import { Component, OnInit, Input } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { CustomerUtil } from 'src/app/core/utils/customer.util';

@Component({
    selector: 'connect-customer-info',
    templateUrl: './customer-info.component.html',
    styleUrls: ['./customer-info.component.scss']
})
export class CustomerInfoComponent implements OnInit {
    @Input() isShownMemo = false;
    customerHeadInfo = '';
    salesHeadInfo = '';
    constructor(public userService: UserService) { }

    ngOnInit() {
        this.customerHeadInfo = this.buildCustomerInfo();
        this.salesHeadInfo = this.buildSaleInfo();
    }

    private buildCustomerInfo() {
        return CustomerUtil.buildCustomerInfo(this.userService.userDetail);
    }

    private buildSaleInfo() {
        const sale = this.userService.employeeInfo;
        return CustomerUtil.buildSaleInfo(sale);
    }
}
