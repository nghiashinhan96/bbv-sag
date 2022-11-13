import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { of } from "rxjs";
import { catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { AnalyticalCardFilterRequest } from "../models/analytical-card/analytical-card-filter.model";
import { AnalyticalCardItem } from "../models/analytical-card/analytical-card-item.model";

@Injectable({
    providedIn: 'root'
})
export class AnalyticalCardService {
    constructor(
        private http: HttpClient
    ) { }

    getAnalyticalCardAmount(paymentMethod: string) {
        const url = `${environment.baseUrl}financial-cards/amount?paymentMethod=${paymentMethod}`;
        return this.http.get(url).pipe(
            catchError(() => of(null))
        );
    }
    
    searchAnalyticalCard(body: any) {
        const url = `${environment.baseUrl}financial-cards/history`;
        return this.http.post(url, body).pipe(
            catchError(() => of(null))
        );
    }

    getAnalyticalCardDetail(documentNr: string, body: any) {
        const url = `${environment.baseUrl}financial-cards/detail?documentNr=${documentNr}`;
        return this.http.post(url, body).pipe(
            catchError(() => of(null))
        );
    }
}