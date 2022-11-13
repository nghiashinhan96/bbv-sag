import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs/internal/observable/of';
import { map, tap } from 'rxjs/operators';
import { ErrorCodeEnum } from '../enums/error-code.enum';
interface SavedBasketRequest {
    basketName: string;
    netPriceViewInContext: boolean;
    customerRefText: string;
}
@Injectable({
    providedIn: 'root'
})
export class ShoppingBasketHistoryService {
    private baseUrl = environment.baseUrl;
    private savedBasketQuantitySubject = new BehaviorSubject<number>(0);
    constructor(
        private http: HttpClient
    ) { }

    get savedBasketQuantity$() {
        return this.savedBasketQuantitySubject.asObservable();
    }

    updateSavedBasketQuantity(value: number) {
        this.savedBasketQuantitySubject.next(value);
    }

    loadBasketHistoryQuantity() {
        const url = `${this.baseUrl}basket/histories/count`;
        this.http.get(url).pipe(
            catchError(err => of(0))
        ).subscribe(({ totalItems }: { totalItems: number }) => {
            this.savedBasketQuantitySubject.next(totalItems);
        });
    }

    loadBasketHistory(body) {
        const url = `${this.baseUrl}basket/histories`;
        return this.http.post(url, body).pipe(
            catchError(err => of({ basketHistories: [] })),
            map(({ basketHistories }: { basketHistories: any }) => {
                return basketHistories;
            })
        );
    }

    getBasketHistory(basketId) {
        const url = `${this.baseUrl}basket/${basketId}/history`;
        return this.http.get(url);
    }

    saveBasketHistory(body: SavedBasketRequest, basketType) {
        const url = `${this.baseUrl}basket/history/create?shopType=${basketType}`;
        return this.http.post(url, body).pipe(
            tap(res => {
                this.loadBasketHistoryQuantity();
            }),
            catchError(error => {
                // Due to API return nothing even sucess then to know error return -1
                return of(ErrorCodeEnum.UNKNOWN_ERROR);
            })
        );
    }

    addBasketHistoryToCart(basketId: number) {
        const url = `${this.baseUrl}cart/basket-history/add/${basketId}`;
        return this.http.post(url, null);
    }

    deleteBasketHistory(basketId: number) {
        const url = `${this.baseUrl}basket/${basketId}/history/delete`;
        return this.http.post(url, basketId);
    }
}
