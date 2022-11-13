import { Component, OnInit } from '@angular/core';

import { AffiliateUtil } from 'sag-common';

import { environment } from 'src/environments/environment';
import { InvoiceSearch } from '../../models/invoice/invoice-search.model';
import { InvoiceService } from '../../services/invoice.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { InvoiceResponse } from '../../models/invoice/invoice-response.model';
import { Observable } from 'rxjs';
import { InvoiceDetail } from '../../models/invoice/invoice-detail.model';

@Component({
    selector: 'connect-invoice',
    templateUrl: './invoice.component.html',
    styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit {
    isCHAffilicates = AffiliateUtil.isAffiliateCH(environment.affiliate);
    invoices: InvoiceResponse;
    errorMessage: string;
    isShownOldInvoice = false;
    detail: Observable<InvoiceDetail> = null;

    constructor (private invoiceService: InvoiceService) { }

    ngOnInit() {
    }

    async searchInvoice(invoiceFilter: InvoiceSearch) {
        SpinnerService.start();
        this.detail = null;
        this.invoices = await this.invoiceService.searchInvoices(invoiceFilter).toPromise();
        this.errorMessage = null;
        SpinnerService.stop();
    }
}
