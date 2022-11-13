import { HttpClient } from "@angular/common/http";
import { Injectable, Injector } from "@angular/core";
import { BsModalService } from "ngx-bootstrap/modal";
import { from, Observable, of } from "rxjs";
import { catchError, map } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { LegalDocument } from "../models/legal-document.model";
import { AppModalService } from "./app-modal.service";

@Injectable({
    providedIn: 'root'
})
export class LegalTermService {
    private baseUrl = environment.baseUrl;

    constructor(
        private injector: Injector,
    ) { }

    get http() {
        return this.injector.get(HttpClient);
    }

    get bsModalService() {
        return this.injector.get(BsModalService);
    }

    get appModal() {
        return this.injector.get(AppModalService);
    }

    getDocuments(token = ''): Observable<LegalDocument[]> {
        const url = `${this.baseUrl}legal-terms`;
        const options = { headers: { token } };
        return this.http.get(url, options).pipe(
            map((res: any) => {
                return (res.legalTerms || []).map(item => new LegalDocument(item));
            }),
            catchError(() => of([]))
        );
    }

    acceptDocuments(document: LegalDocument, token = '') {
        const url = `${this.baseUrl}legal-terms/${document.termId}`;
        const options = { headers: { token } };
        return this.http.put(url, null, options);
    }

    hasExpiredTerms(token = '') {
        const url = `${this.baseUrl}legal-terms/has-expired-terms`;
        const options = { headers: { token } };
        return this.http.post(url, null, options).pipe(
            catchError(() => of(null))
        );
    }

    getUnacceptedTerm(token = '') {
        const url = `${this.baseUrl}legal-terms/unaccepted-terms`;
        const options = { headers: { token } };
        return this.http.post(url, null, options).pipe(
            map((res: any) => {
                return (res.legalTerms || []).map(item => new LegalDocument(item));
            }),
            catchError(() => of([]))
        );
    }

    canProceed(documents: LegalDocument[]) {
        const isAllAccepted = documents.every(d => d.accepted);
        const isOverdue = documents.some(d => !d.accepted && d.daysLeft <= 0);
        return isAllAccepted || !isOverdue;
    }
}