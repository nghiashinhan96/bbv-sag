import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { FilterCategory, MerkmaleCategoryRequest, NonMerkmaleAttribute, NonMerkmaleCategoryRequest, SEARCH_MODE } from 'sag-article-list';

@Injectable({
    providedIn: 'root'
})
export class ArticleNonContextService {

    private readonly CATEGORY_FULTER_URL = 'search/articles/filter';
    private readonly SUB_LEVEL_CATEGORY_URL = 'categories/filter';
    private readonly BASE_URL = environment.baseUrl;

    constructor(private http: HttpClient) { }

    getMerkmaleCategory(requestBody: FilterCategory, hasChanged = false) {
        const url = `${this.BASE_URL}${this.CATEGORY_FULTER_URL}`;
        const request = new MerkmaleCategoryRequest(requestBody, hasChanged);
        return this.http.post(url, request).pipe(catchError(err => {
            return of({
                articles: {
                    content: [],
                    last: true,
                    totalElements: 0
                }
            });
        }));
    }

    getSubMerkmaleCategory(gaId: string, requestBody: any, hasChanged = false) {
        const params = { gaId };
        const url = `${this.BASE_URL}${this.SUB_LEVEL_CATEGORY_URL}`;
        const request = new MerkmaleCategoryRequest(requestBody, hasChanged);
        return this.http.post(url, request, { params });
    }

    getNonMerkmaleCategory(requestBody: FilterCategory, hasChanged = false) {
        const url = `${this.BASE_URL}${this.CATEGORY_FULTER_URL}`;
        const request = new NonMerkmaleCategoryRequest(requestBody, hasChanged);
        request.toRequest(request.filter_mode, requestBody);
        return this.http.post(url, request).pipe(catchError(err => {
            return of({
                articles: {
                    content: [],
                    last: true,
                    totalElements: 0
                }
            });
        }));
    }

    composeAttributes(attribute: any) {
        const typeMode = attribute.type;

        switch (typeMode) {
            case SEARCH_MODE.TYRES_SEARCH:
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
                return new NonMerkmaleAttribute(attribute).toTyreAttribute(attribute);
            case SEARCH_MODE.BATTERY_SEARCH:
                return new NonMerkmaleAttribute(attribute).toBatteryAttribute(attribute);
            case SEARCH_MODE.BULB_SEARCH:
                return new NonMerkmaleAttribute(attribute).toBulbAttribute(attribute);
            case SEARCH_MODE.OIL_SEARCH:
                return new NonMerkmaleAttribute(attribute).toOilAttribute(attribute);
        }
    }
}
