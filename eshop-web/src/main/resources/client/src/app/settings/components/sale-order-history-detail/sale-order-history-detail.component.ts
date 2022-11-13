import { Component, OnInit, Input } from '@angular/core';

import { SagTableColumn } from 'sag-table';

import { OrderHistoryBusinessService } from '../../services/order-history-business.service';

@Component({
    selector: 'connect-sale-order-history-detail',
    templateUrl: './sale-order-history-detail.component.html',
    styleUrls: ['./sale-order-history-detail.component.scss']
})
export class SaleOrderHistoryDetailComponent implements OnInit {
    @Input() articles: any;

    columns: SagTableColumn[] = [];
    orderTypeOptions: any[];
    tableCallback: any;
    tableRequest: any;

    constructor(
        private orderHistoryBusiness: OrderHistoryBusinessService,
    ) { }

    ngOnInit() {
        this.buildColumns();
    }

    buildColumns() {
        this.columns = this.orderHistoryBusiness.buildSaleOrderDetailTable();
    }
}
