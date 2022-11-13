import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from 'src/environments/environment';
import { SEARCH_MODE } from 'sag-article-list';
import { ARTICLE_SEARCH_MODE, ArticleSearchService } from 'sag-article-search';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class VehicleSearchService {
    private readonly VEHICLE_HISTORY_REVAMP_SEARCH_URL = 'history/vehicles';

    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private router: Router,
        private articleSearchService: ArticleSearchService
    ) { }

    getVehicleHistoryRevampSearch(queryData) {
        const url = `${this.baseUrl}${this.VEHICLE_HISTORY_REVAMP_SEARCH_URL}?page=${queryData.pageNumber}`;
        return this.http.post(url, queryData);
    }

    // =================== business functions =============================================================
    navigateToHistory(data) {
        let request: any;
        switch (data.searchMode) {
            case SEARCH_MODE.FREE_TEXT:
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
            case SEARCH_MODE.VEHICLE_DESC:
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
            case SEARCH_MODE.MAKE_MODEL_TYPE:
                let values = [];
                try {
                    values = JSON.parse(data.jsonSearchTerm || '[]') || [];
                } catch { }
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.jsonSearchTerm, searchMode: data.searchMode } }
                );
                break;
            default:
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
        }
    }

    navigateToArticleSearch(data) {
        let url = ['article'];
        let queryParams: any = {
            keywords: data.searchTerm,
            ref: data.searchMode
        };
        switch (data.searchMode) {
            case ARTICLE_SEARCH_MODE.ARTICLE_NUMBER:
                queryParams.type = SEARCH_MODE.ARTICLE_NUMBER;
                queryParams.articleNr = data.searchTerm;
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_DESC:
                queryParams.type = SEARCH_MODE.FREE_TEXT;
                queryParams.articleId = data.searchTerm;
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_ID:
                queryParams.type = SEARCH_MODE.ID_SAGSYS;
                queryParams.articleId = data.articleId;
                break;
            case ARTICLE_SEARCH_MODE.FREE_TEXT:
            case ARTICLE_SEARCH_MODE.SHOPPING_LIST:
                queryParams.type = SEARCH_MODE.FREE_TEXT;
                queryParams.articleId = data.searchTerm;
                break;
            default:
                url = null;
                break;
        }
        if (!url) {
            return;
        }
        this.articleSearchService.updateHistory(data.id).subscribe();
        this.router.navigate(url, { queryParams });
    }
}
