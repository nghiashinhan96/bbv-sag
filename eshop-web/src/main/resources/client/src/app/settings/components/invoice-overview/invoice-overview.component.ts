import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { AffiliateUtil, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { environment } from 'src/environments/environment';
import { InvoiceDetail } from '../../models/invoice/invoice-detail.model';
import { InvoiceBusinessService } from '../../services/invoice-business.service';

@Component({
    selector: 'connect-invoice-overview',
    templateUrl: './invoice-overview.component.html',
    styleUrls: ['./invoice-overview.component.scss']
})
export class InvoiceOverviewComponent implements OnInit {
    @Input() detail: InvoiceDetail;
    @Input() isShownOldInvoice = false;

    dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;

    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);

    constructor(public bsModalRef: BsModalRef, private invoiceBusinessService: InvoiceBusinessService) { }

    ngOnInit() {
    }

    async getInvoicePDF(event: any, invoiceOverview: InvoiceDetail) {
        event.preventDefault();
        event.stopPropagation();
        this.invoiceBusinessService.openInvoicePDF(
            invoiceOverview.invoiceNr,
            invoiceOverview.orderNr,
            this.isShownOldInvoice
        );
    }

}
