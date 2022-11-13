import { Location } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { SagTableColumn } from 'sag-table';
import { ResponseMessage } from 'src/app/core/models/response-message.model';
import { FinalCustomerOrder } from '../../models/final-customer/final-customer-order.model';
import { FinalCustomerService } from '../../services/final-customer.service';
import { OrderHistoryBusinessService } from '../../services/order-history-business.service';

@Component({
    selector: 'connect-final-user-admin-order-detail',
    templateUrl: './final-user-admin-order-detail.component.html',
    styleUrls: ['./final-user-admin-order-detail.component.scss'],
})
export class FinalUserAdminOrderDetailComponent implements OnInit, OnDestroy {
    @ViewChild('articleNumber', { static: true }) articleNumber: TemplateRef<any>;
    @ViewChild('vehicleInfo', { static: true }) vehicleInfo: TemplateRef<any>;
    @ViewChild('price', { static: true }) price: TemplateRef<any>;
    @ViewChild('amountNumber', { static: true }) amountNumber: TemplateRef<any>;
    @ViewChild('deliveryInfo', { static: true }) deliveryInfo: TemplateRef<any>;
    @ViewChild('additionalText', { static: true }) additionalText: TemplateRef<any>;

    routerParamsSub: Subscription;

    @Input() set orderDetail(data: FinalCustomerOrder) {
        if (data) {
            this.responseMessage = null;
            this.orderData = data;
        }
    };
    @Output() addToCart = new EventEmitter();

    columns: SagTableColumn[];
    invoiceTypeInformation: string;
    param = 'CONDITION.PARTIAL_DELIVERY_TITLE';
    routerState = null;
    orderData: FinalCustomerOrder;

    responseMessage: ResponseMessage;

    constructor(
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private finalCustomerService: FinalCustomerService,
        private orderHistoryBusiness: OrderHistoryBusinessService
    ) { }

    ngOnInit() {
        this.columns = this.orderHistoryBusiness.buildFinalUserAdminOrderDetailItemsColumn([
            this.articleNumber,
            this.deliveryInfo
        ]);

        this.routerParamsSub = this.activatedRoute.params.subscribe(params => {
            if (!params) {
                return;
            }
        });
    }

    ngOnDestroy() {
        this.routerParamsSub.unsubscribe();
    }

    getOrderDetail(id) {
        this.finalCustomerService.getOrderDetail(id).subscribe(res => {
            this.orderDetail = res;
        });
    }

    onBackOrderHistoryList() {
        this.location.back();
    }

    getDeliveryInfos(item: any): string[] {
        return this.orderHistoryBusiness.buildFCDeliveryInfo(item.availabilities);
    }

    addOrderToCart() {
        this.responseMessage = null;
        this.addToCart.emit((success) => {
            if (success) {
                this.responseMessage = new ResponseMessage({ key: 'BASKET_HISTORY.ADDING_SUCCESSFULLY', isError: false });
            } else {
                this.responseMessage = new ResponseMessage({ key: 'BASKET_HISTORY.ADDING_FAILED', isError: true });
            }
        })
    }
}
