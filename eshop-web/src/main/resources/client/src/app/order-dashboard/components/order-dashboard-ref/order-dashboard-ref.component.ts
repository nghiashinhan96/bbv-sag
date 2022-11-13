import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { OrderDashboardRefModel } from '../../models/order-dashboard-ref.model';
import { OrderDashboardListItemModel } from '../../models/order-dashboard-list-item.model';

@Component({
    selector: 'connect-order-dashboard-ref',
    templateUrl: './order-dashboard-ref.component.html',
    styleUrls: ['./order-dashboard-ref.component.scss']
})
export class OrderDashboardRefComponent implements OnInit {

    @Input() set order(val: OrderDashboardListItemModel) {
        if (!!val) {
            this.data = new OrderDashboardRefModel(val);
        }
    }
    @ViewChild('pop', { static: true }) popover: PopoverDirective;
    data: OrderDashboardRefModel = new OrderDashboardRefModel();
    placement = 'top';

    constructor() { }

    ngOnInit() { }

    onMouseenter() {
        if (!this.popover.isOpen) {
            setTimeout(() => {
                this.popover.show();
            });
        }
    }

    onMouseout() {
        this.popover.hide();
    }

}
