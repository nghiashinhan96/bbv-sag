import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ArticleNumberSearchRequest } from '../models/article-number-search-request.model';
import { ArticleDescriptionSearchRequest } from '../models/article-description-search-request.model';

@Injectable({
    providedIn: 'root'
})
export class ArticleSearchService {

    readonly ARTICLE_NUMBER_SEARCH = 'ARTICLE_NUMBER_SEARCH';
    readonly ARTICLE_DESCRIPTION_SEARCH = 'ARTICLE_DESCRIPTION_SEARCH';

    private baseUrl = environment.baseUrl;

    private readonly ARTICLE_DESC_SEARCH_URL = 'search/free-text/';

    constructor(
        private http: HttpClient
    ) { }

    searchAricleByDescription(requestBody: any) {
        const url = `${this.baseUrl}${this.ARTICLE_DESC_SEARCH_URL}`;
        return this.http.post(url, requestBody);
    }

    selectRequestBodyBySearchType({ requestBody, searchType }) {
        switch (searchType) {
            case this.ARTICLE_NUMBER_SEARCH:
                return new ArticleNumberSearchRequest();
            case this.ARTICLE_DESCRIPTION_SEARCH:
                return new ArticleDescriptionSearchRequest(requestBody);
        }
    }
}
