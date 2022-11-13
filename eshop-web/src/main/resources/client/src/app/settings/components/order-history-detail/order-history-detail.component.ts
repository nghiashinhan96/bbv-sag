import { Component, OnInit, TemplateRef, ViewChild, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { ArticleBroadcastKey } from 'sag-article-detail';
import { SagTableColumn } from 'sag-table';
import { OrderHistoryDetail } from '../../models/order-history/order-history-detail.model';
import { OrderItem } from '../../models/order-history/order-item.model';
import { OrderHistoryBusinessService } from '../../services/order-history-business.service';
import { BroadcastService, AffiliateEnum, AffiliateUtil } from 'sag-common';
import { SEARCH_MODE } from 'sag-article-list'
import { environment } from 'src/environments/environment';
import { ResponseMessage } from 'src/app/core/models/response-message.model';
@Component({
    selector: 'connect-order-history-detail',
    templateUrl: './order-history-detail.component.html',
    styleUrls: ['./order-history-detail.component.scss']
})
export class OrderHistoryDetailComponent implements OnInit, OnDestroy {
    @ViewChild('articleNumber', { static: true }) articleNumber: TemplateRef<any>;
    @ViewChild('vehicleInfo', { static: true }) vehicleInfo: TemplateRef<any>;
    @ViewChild('price', { static: true }) price: TemplateRef<any>;
    @ViewChild('amountNumber', { static: true }) amountNumber: TemplateRef<any>;
    @ViewChild('deliveryInfo', { static: true }) deliveryInfo: TemplateRef<any>;
    @ViewChild('additionalText', { static: true }) additionalText: TemplateRef<any>;

    @Input() set orderDetail(orderHistoryDetail: Observable<OrderHistoryDetail>) {
        if (this.orderDetailSubscription) {
            this.orderDetailSubscription.unsubscribe();
        }
        this.responseMessage = null;
        this.orderDetailSubscription = orderHistoryDetail.subscribe(val => {
            this.orderItem = val;
        })
    };
    @Output() addToCart = new EventEmitter();

    responseMessage: ResponseMessage;
    orderItem: OrderHistoryDetail;
    columns: SagTableColumn[];
    invoiceTypeInformation: string;
    param = 'CONDITION.PARTIAL_DELIVERY_TITLE';
    sb = AffiliateEnum.SB;
    isSB: boolean;
    affiliateCode = environment.affiliate;

    private dateFrom;
    private dateTo;
    private subs = new Subscription();
    private orderDetailSubscription: Subscription;

    descriptionType = 'ORDER_HISTORY';
    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private orderHistoryBusiness: OrderHistoryBusinessService,
        private articleListBroadcastService: BroadcastService
    ) {
        this.isSB = AffiliateUtil.isSb(environment.affiliate);
    }

    ngOnInit() {
        this.subs.add(this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe((code: string) => {
                this.router.navigate(['/article', 'result'], {
                    queryParams: {
                        type: SEARCH_MODE.ARTICLE_NUMBER,
                        articleNr: code
                    }
                });
            })
        );
        this.columns = this.orderHistoryBusiness.buildOrderDetailItemsColumn([
            this.articleNumber,
            this.vehicleInfo,
            this.price,
            this.amountNumber,
            this.deliveryInfo
        ]);

        if (!this.isSB) {
            this.columns.push({
                id: 'additionalText',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.REFERENCE',
                filterable: true,
                sortable: true,
                width: '163px',
                cellTemplate: this.additionalText
            });
        }

        const { from, to } = this.route.snapshot.queryParams;
        this.dateFrom = from;
        this.dateTo = to;
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
        this.orderDetailSubscription.unsubscribe();
    }

    getDeliveryInfos(item: OrderItem) {
        return this.orderHistoryBusiness.buildDeliveryInfo(item.article.availabilities, item.sendMethodDesc);
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
