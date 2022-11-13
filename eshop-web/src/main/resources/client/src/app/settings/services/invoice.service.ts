import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { InvoiceSearch } from '../models/invoice/invoice-search.model';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { InvoiceResponse } from '../models/invoice/invoice-response.model';
import { InvoiceDetail } from '../models/invoice/invoice-detail.model';
import { Observable, of } from 'rxjs';
import { BasketItemSource } from 'src/app/analytic-logging/models/basket-item-source.model';

@Injectable({
    providedIn: 'root'
})
export class InvoiceService {
    private readonly SEARCH_INVOICES_URL = 'invoice/archives/search';
    private readonly SEARCH_INVOICES_DETAIL_URL = 'invoice/archives/';

    private readonly BASE_URL = environment.baseUrl;

    constructor(private http: HttpClient) { }

    searchInvoices(body: InvoiceSearch): Observable<InvoiceResponse> {
        const url = `${this.BASE_URL}${this.SEARCH_INVOICES_URL}`;
        return this.http.post(url, body).pipe(
            catchError(() => of(null)),
            map(response => new InvoiceResponse(response))
        );
    }

    getInvoiceDetail(invoiceNr: string, orderNr: string, simpleMode: boolean, oldInvoice = false) {
        const url = `${this.BASE_URL}${this.SEARCH_INVOICES_DETAIL_URL}${invoiceNr}`;
        const data = {
            simpleMode: `${simpleMode}`,
            orderNr: `${orderNr}`,
            oldInvoice: `${oldInvoice}`
        };

        return this.http.get(url, { params: data }).pipe(map((response: any) => new InvoiceDetail(response)));
    }

    addInvoiceToBasket(invoiceNr: string, orderNr: string, orderHistoryId: number, basketItemSource: BasketItemSource) {
        const url = `${this.BASE_URL}${this.SEARCH_INVOICES_DETAIL_URL}${invoiceNr}/addToCart`;
        const data = {
            orderNr,
            orderHistoryId: orderHistoryId ? orderHistoryId.toString() : '',
            basketItemSourceId: basketItemSource.basketItemSourceId,
            basketItemSourceDesc: basketItemSource.basketItemSourceDesc
        };

        return this.http.post(url, {}, { params: data });
    }
}
