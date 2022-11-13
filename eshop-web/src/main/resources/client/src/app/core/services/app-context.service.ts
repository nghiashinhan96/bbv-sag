import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { catchError, map, switchMap } from 'rxjs/operators';
import { of, BehaviorSubject } from 'rxjs';
import { SEARCH_MODE } from 'sag-article-list';

import { AppContextEnum } from '../enums/app-context.enum';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { environment } from 'src/environments/environment';
import { AppStorageService } from './app-storage.service';

@Injectable({
    providedIn: 'root'
})
export class AppContextService {
    private baseurl = environment.baseUrl;

    public shoppingBasketContextSub = new BehaviorSubject<ShoppingBasketContextModel>(new ShoppingBasketContextModel());
    shoppingBasketContext: ShoppingBasketContextModel = new ShoppingBasketContextModel();
    constructor(
        private http: HttpClient,
        private appStorage: AppStorageService,
    ) { }

    get shoppingBasketContext$() {
        return this.shoppingBasketContextSub.asObservable();
    }

    clearContextInCache() {
        const url = `${environment.baseUrl}context/cache/clear`;
        return this.http.delete(url);
    }

    initAppContext() {
        const url = `${this.baseurl}context/init`;
        return this.http.post(url, null, { observe: 'body' }).pipe(
            catchError(err => {
                console.log('context init fail: ', err);
                // TODO: shown warning and could not login
                return of(null);
            }),
            map(res => {
                this.shoppingBasketContext = new ShoppingBasketContextModel(res && res.eshopBasketContext);
                this.shoppingBasketContextSub.next(this.shoppingBasketContext);
                return res;
            }));
    }

    getAppContext() {
        const url = `${this.baseurl}context/`;
        return this.http.get(url).pipe(
            catchError(err => {
                console.log('context init fail: ', err);
                // TODO: shown warning and could not login
                return of(null);
            }),
            map(res => {
                this.shoppingBasketContext = new ShoppingBasketContextModel(res && res.eshopBasketContext);
                this.shoppingBasketContextSub.next(this.shoppingBasketContext);
                return true;
            }));
    }

    updateShoppingBasketContext(basketContext: ShoppingBasketContextModel) {
        const body = {
            contextDto: {
                eshop_basket_setting: basketContext
            }
        };
        const url = `${this.baseurl}context/update/${AppContextEnum.ESHOP_BASKET_CONTEXTS}`;
        return this.http.post(url, body, { observe: 'body' }).pipe(
            switchMap(res => this.getAppContext()),
            catchError(err => {
                console.log('update shopping basket context fail: ', err);
                return of(null);
            }));
    }

    updateVehicleContext(vehicleDoc?: any) {
        if (!this.appStorage.appToken) {
            return of(null);
        }
        let vehSearchTerm = '';
        let vehSearchMode = '';
        let fromOffer = false;
        if (vehicleDoc) {
            vehSearchTerm = vehicleDoc.keywords;
            vehSearchMode = vehicleDoc.searchMode ? vehicleDoc.searchMode : SEARCH_MODE.VEHICLE_DESC;
            fromOffer = vehicleDoc.fromOffer;
            delete vehicleDoc.keywords;
            delete vehicleDoc.searchMode;
        }
        const body = {
            contextDto: {
                vehicleDoc,
                vehSearchTerm,
                vehSearchMode,
                fromOffer
            },
        };
        const url = `${this.baseurl}context/update/${AppContextEnum.VEHICLE_CONTEXT}`;
        return this.http.post(url, body, { observe: 'body' }).pipe(catchError(err => {
            console.log('update vehicle context fail: ', err);
            return of(false);
        }));
    }

    refreshNextWorkingDateCache() {
        const url = `${this.baseurl}context/cache/nextWorkingDate/refresh`;
        return this.http.post(url, null);
    }
}
