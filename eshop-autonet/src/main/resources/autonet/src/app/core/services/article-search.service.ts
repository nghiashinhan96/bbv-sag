import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppHelperUtil } from '../utils/helper.util';
import { map } from 'rxjs/internal/operators/map';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs/internal/observable/of';
import { SagValidator } from 'sag-common';

@Injectable({ providedIn: 'root' })
export class ArticleSearchService {

    constructor(
        private http: HttpClient
    ) { }

    searchArticleByNumber(articleNumber: string) {
        const url = `${environment.baseUrl}search/articles/${articleNumber}`;
        const noResult = 'SEARCH.ERROR_MESSAGE.NOT_FOUND';
        return this.http.post(url, {}).pipe(
            map((res: any) => {
                const articles = AppHelperUtil.convertAutonetData(res && res.content || []);
                const artnr = SagValidator.removeWhiteSpace(SagValidator.removeNonAlphaCharacter(articleNumber || '')) || '';
                const result = (articles || []).find(art => art.artnr.toLowerCase() === artnr.toLowerCase() || art.artid.toLowerCase() === artnr.toLowerCase());
                return result || noResult;
            }),
            catchError(() => {
                return of(noResult);
            }));
    }
}
