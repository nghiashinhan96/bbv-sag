import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { map, catchError } from 'rxjs/operators';
import { ReturnArticle } from 'src/app/article-return/models/return-article.model';
import { of } from 'rxjs';
import { ArticleSearchHistory, VehicleSearchHistory } from 'sag-article-search';
import { FavoriteItem } from 'sag-article-detail';
@Injectable({
    providedIn: 'root'
})
export class HeaderSearchService {

    private baseUrl = environment.baseUrl;
    constructor(
        private http: HttpClient,
        private fbRecordingService: FeedbackRecordingService
    ) { }

    searchFreetext(body: any, isArticleSeachMode?: boolean) {
        const url = `${this.baseUrl}search/free-text`;
        return this.http.post(url, body).pipe(map((res: any) => {
            if (body) {
                this.fbRecordingService.recordSearchFreeTextAction(body.options, body.keyword, isArticleSeachMode);
            }
            return res;
        }));
    }

    searchArticleByBarCode(code: any) {
        const url = `${this.baseUrl}search/barcode?barCode=${code}`;
        return this.http.post(url, null);
    }

    searchReturnArticles(referenceId: string) {
        const url = `${this.baseUrl}return/order/search?reference=${referenceId}`;
        return this.http.get(url).pipe(
            map((articles: any[]) => {
                return articles.map(article => new ReturnArticle(article))
                    .filter(art => art.returnQuantity > 0);
            })
        );
    }

    getDashboardData(fromSource: string) {
        const url = `${this.baseUrl}search/history?fromSource=${fromSource}`;
        return this.http.get(url).pipe(
            map((res: any) => {
                res.artHistories = (res.artHistories || []).map(item => new ArticleSearchHistory(item));
                res.vehHistories  = (res.vehHistories || []).map(item => new VehicleSearchHistory(item));
                res.unipartFavotite = (res.unipartFavotite || []).map(item => {
                    const favorite = new FavoriteItem(item);
                    return favorite;
                });
                return res;
            }),
            catchError(() => {
                return of(null);
            })
        );
    }

    getArticleDataFromId(id) {
        const url = `${this.baseUrl}search/article/${id}`;
        return this.http.get(url);
    }
}
