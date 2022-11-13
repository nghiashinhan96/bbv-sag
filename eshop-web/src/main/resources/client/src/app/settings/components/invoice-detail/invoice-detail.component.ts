import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { SagTableColumn } from 'sag-table';
import { BasketItemSourceDesc } from 'src/app/analytic-logging/enums/basket-item-source-desc.enum';
import { AnalyticEventType, ShoppingBasketEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { ResponseMessage } from 'src/app/core/models/response-message.model';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { InvoiceDetail } from '../../models/invoice/invoice-detail.model';
import { InvoiceBusinessService } from '../../services/invoice-business.service';
import { InvoiceService } from '../../services/invoice.service';


@Component({
    selector: 'connect-invoice-detail',
    templateUrl: './invoice-detail.component.html',
    styleUrls: ['./invoice-detail.component.scss']
})
export class InvoiceDetailComponent implements OnInit, OnDestroy {
    @Input() set detail(invoiceDetail: Observable<InvoiceDetail>) {
        if (this.subcripstion) {
            this.subcripstion.unsubscribe();
        }
        this.responseMessage = null;
        this.subcripstion = invoiceDetail.subscribe(val => {
            this.invoiceDetail = val;
        })
    };
    columns: SagTableColumn[] = [];
    responseMessage: ResponseMessage;
    invoiceDetail: InvoiceDetail;

    private subcripstion: Subscription;

    constructor(
        private analyticService: AnalyticLoggingService,
        private invoiceBusinessService: InvoiceBusinessService,
        private invoiceService: InvoiceService,
        private shoppinBasketService: ShoppingBasketService
    ) { }

    ngOnInit() {
        this.columns = this.invoiceBusinessService.buildInvoiceDetailColumns();
    }

    onAddToBasket(invoiceDetail: InvoiceDetail) {
        this.responseMessage = null;
        const spinner = SpinnerService.start();
        let basketItemSource = this.analyticService.createBasketItemSource(BasketItemSourceDesc.INVOICE_ARCHIVE);
        this.invoiceService
            .addInvoiceToBasket(invoiceDetail.invoiceNr, invoiceDetail.orderNr, invoiceDetail.orderHistoryId, basketItemSource)
            .pipe(finalize(() => SpinnerService.stop(spinner)))
            .subscribe(response => {
                this.responseMessage = new ResponseMessage({ key: 'BASKET_HISTORY.ADDING_SUCCESSFULLY', isError: false });
                this.sendSavedBasketEventData(invoiceDetail, response);
                this.shoppinBasketService.updateOtherProcess(response);
            }, error => this.responseMessage = new ResponseMessage({ key: 'BASKET_HISTORY.ADDING_FAILED', isError: true }));
    }

    ngOnDestroy(): void {
        this.subcripstion.unsubscribe();
    }

    private sendSavedBasketEventData(invoiceDetail: InvoiceDetail, res: any) {
        setTimeout(() => {
            const selectedArticleIds = invoiceDetail.positions.map(pos => pos.articleId);

            const items = [];

            (res.items || []).forEach(item => {
                const invoiceItem = invoiceDetail.positions.find(p =>
                    p.articleId === item.articleItem.artid &&
                    (p.vehicleInfo || '').trim() === (item.vehicle.vehicleInfo || '').trim()
                );
                if (invoiceItem) {
                    items.push(item);
                    item.quantity = invoiceItem.quantity;
                }
            });
            res.items = items;

            const eventData = this.analyticService.createShoppingBasketEventData(
                res,
                {
                    source: ShoppingBasketEventType.INVOICE,
                    selectedArticles: selectedArticleIds,
                    totalPrice: invoiceDetail.amount
                }
            );
            this.analyticService.postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }
}
