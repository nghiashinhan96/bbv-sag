import { Component, OnInit, Input, ViewChild, TemplateRef, OnChanges, SimpleChanges, ElementRef, Inject } from '@angular/core';
import { SagTableColumn } from 'sag-table';

import { InvoiceResponse } from '../../models/invoice/invoice-response.model';
import { InvoiceBusinessService } from '../../services/invoice-business.service';
import { InvoiceDetail } from '../../models/invoice/invoice-detail.model';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { InvoiceOverviewComponent } from '../invoice-overview/invoice-overview.component';
import { InvoiceService } from '../../services/invoice.service';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Observable } from 'rxjs';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { PageScrollService } from 'ngx-page-scroll-core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'connect-invoice-search-result',
    templateUrl: './invoice-search-result.component.html',
    styleUrls: ['./invoice-search-result.component.scss']
})
export class InvoiceSearchResultComponent implements OnInit, OnChanges {
    @ViewChild('actions', { static: true }) actions: TemplateRef<any>;
    @ViewChild('invoiceDetailElement', {read: ElementRef, static: false }) invoiceDetailElement: ElementRef;

    @Input() invoices: InvoiceResponse;
    @Input() errorMessage: string;
    @Input() isShownOldInvoice = false;
    @Input() detail: Observable<InvoiceDetail> = null;

    columns: SagTableColumn[] = [];
    modelRef: BsModalRef;

    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);


    constructor(
        private invoiceBusinessService: InvoiceBusinessService,
        private bsModalService: BsModalService,
        private invoiceService: InvoiceService,
        private pageScrollService: PageScrollService,
        @Inject(DOCUMENT) private document: any
    ) { }

    ngOnInit() {
        this.columns = this.invoiceBusinessService.buildInvoiceOverviewColumns([this.actions]);
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.detail && changes.detail.currentValue && !changes.detail.firstChange) {
            this.detail = changes.detail.currentValue;
        } else {
            this.detail = null;
        }
    }

    openInvoiceDetail(invoiceDetail: InvoiceDetail) {
        SpinnerService.start();
        this.detail = null;

        this.detail = this.invoiceService.getInvoiceDetail(
            invoiceDetail.invoiceNr,
            invoiceDetail.orderNr,
            false,
            this.isShownOldInvoice
        ).pipe(
            finalize(() => {
                SpinnerService.stop();
                this.pageScrollService.scroll({
                    document: this.document,
                    scrollTarget: this.invoiceDetailElement && this.invoiceDetailElement.nativeElement || null,
                    scrollInView: true,
                    duration: 400
                });
            })
        );
    }

    async openInvoicePDF(event: any, invoiceOverview: InvoiceDetail) {
        event.preventDefault();
        event.stopPropagation();
        this.invoiceBusinessService.openInvoicePDF(
            invoiceOverview.invoiceNr,
            invoiceOverview.orderNr,
            this.isShownOldInvoice
        );
    }

    openInvoiceOverview(event: any, invoiceDetail: InvoiceDetail) {
        event.preventDefault();
        event.stopPropagation();

        SpinnerService.start();

        this.invoiceService.getInvoiceDetail(
            invoiceDetail.invoiceNr,
            invoiceDetail.orderNr,
            true,
            this.isShownOldInvoice
        ).pipe(
            finalize(() => { SpinnerService.stop(); })
        )
            .subscribe((detail) => {
                this.modelRef = this.bsModalService.show(InvoiceOverviewComponent, {
                    ignoreBackdropClick: true,
                    backdrop: 'static',
                    initialState: {
                        detail,
                        isShownOldInvoice: this.isShownOldInvoice
                    }
                });
            });
    }
}
