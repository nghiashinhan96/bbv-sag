import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { MultiLevelCategoryRequest } from '../models/multi-level-category.request.model';
import { LevelCategory } from '../models/level-category.model';
import { SingleLevelCategoryRequest } from '../models/single-level-category.request.model';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs/internal/observable/of';

@Injectable({
    providedIn: 'root'
})
export class FreetextArticleService {

    private readonly CATEGORY_FULTER_URL = 'search/articles/filter';
    private readonly SUB_LEVEL_CATEGORY_URL = 'categories/filter';
    private readonly BASE_URL = environment.baseUrl;

    constructor (
        private http: HttpClient,
        public appStorage: AppStorageService,
        private shoppingBasketService: ShoppingBasketService,
    ) { }

    getMultipleLevelCategory(requestBody: LevelCategory, hasChanged = false, multipleLevelGaids?) {
        const url = `${this.BASE_URL}${this.CATEGORY_FULTER_URL}`;
        if(multipleLevelGaids) {
            requestBody.multipleLevelGaids = multipleLevelGaids;
        }

        const request = new MultiLevelCategoryRequest(requestBody, hasChanged);
        if (this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.FINAL) {
            request.finalCustomerId = this.shoppingBasketService.finalCustomerId;
        }
        return this.http.post(url, request).pipe(
            catchError(() => of({}))
        );
    }

    getSubCategory(gaId: string, requestBody: any, hasChanged = false) {
        const params = { gaId };
        const url = `${this.BASE_URL}${this.SUB_LEVEL_CATEGORY_URL}`;
        const request = new MultiLevelCategoryRequest(requestBody, hasChanged);
        return this.http.post(url, request, { params }).pipe(
            catchError(() => of([]))
        );
    }

    getSingleLevelCategory(requestBody: LevelCategory, hasChanged = false) {
        const url = `${this.BASE_URL}${this.CATEGORY_FULTER_URL}`;
        const request = new SingleLevelCategoryRequest(requestBody, hasChanged);
        request.toRequest(request.filter_mode, requestBody);
        return this.http.post(url, request).pipe(
            catchError(() => of({}))
        );
    }
}
