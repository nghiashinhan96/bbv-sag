import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DigiInvoiceService {
    private readonly DIGI_INVOICE_SEND_CODE_URL = 'digi-invoice/security-code/send';
    private readonly DIGI_INVOICE_VERIFY_CODE_URL = 'digi-invoice/security-code/check';
    private readonly DIGI_INVOICE_REQUEST_CHANGE_EMAIL_URL = 'digi-invoice/submission/change-mail';

    private readonly BASE_URL = environment.baseUrl;


    constructor(private http: HttpClient) { }

    sendCode(email: string) {
        const url = `${this.BASE_URL}${this.DIGI_INVOICE_SEND_CODE_URL}`;
        const body = { invoiceRecipientEmail: email};
        return this.http.post(url, body, { responseType: 'text' });
    }

    checkCode(code: string, hash: string) {
        const url = `${this.BASE_URL}${this.DIGI_INVOICE_VERIFY_CODE_URL}`;
        const body = { token: code, hashUsernameCode: hash };
        return this.http.post(url, body, { responseType: 'text' });
    }

    requestToChangeEmail(code: string, hash: string, invoiceRecipientEmail: string, invoiceRequestEmail: string) {
        const url = `${this.BASE_URL}${this.DIGI_INVOICE_REQUEST_CHANGE_EMAIL_URL}`;
        const body = {
            token: code,
            hashUsernameCode: hash,
            invoiceRecipientEmail,
            invoiceRequestEmail
        };
        return this.http.post(url, body, { responseType: 'text' });
    }
}
