import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs/internal/observable/of';
import { get } from 'lodash';
import { BrandFilterItem, SEARCH_MODE } from 'sag-article-list';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { ArticleModel, BarFilter, Criteria, FavoriteItem, FavoriteService, FAVORITE_PROCESS_TYPE } from 'sag-article-detail';
import { WspFilterRequest } from '../models/wsp-filter-request.model';
import { tap } from 'rxjs/operators';
import { WspTreeModel } from '../models/wsp-tree.model';
import { map } from 'rxjs/internal/operators/map';
import { Subscription } from 'rxjs';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { LevelCategoryRequest } from 'src/app/core/models/level-category.request.model';

@Injectable()
export class UniversalPartService {
    private baseUrl = environment.baseUrl;

    private brandsDataSubject$ = new BehaviorSubject(undefined);
    brandsData$ = this.brandsDataSubject$.asObservable();
    brandsData: BrandFilterItem[] = [];
    currentWspFilterRequest: WspFilterRequest;
    hasMoreData: boolean;
    moreData: any;
    articleCount = 0;

    private barFilterDataSubject$ = new BehaviorSubject(undefined);

    barFilterData: BarFilter;
    barFilterData$ = this.barFilterDataSubject$.asObservable();

    currentGetArticleListSub: Subscription = null;

    isFilteringArticle = false;
    currentArticleData: any = {};

    selectedGenArtName = '';
    selectedGenArtIds = [];

    private readonly FILTER_URL = 'search/articles/filter';

    constructor(
        private http: HttpClient,
        private appStorageService: AppStorageService,
        private favoriteService: FavoriteService
    ) { }

    getCompactTrees() {
        if (this.appStorageService.uniTrees !== undefined && this.appStorageService.uniTrees !== null) {
            return of(this.appStorageService.uniTrees);
        }

        return this.http.get(`${this.baseUrl}unitrees/compact-unitrees`).pipe(
            catchError(err => of(null)),
            map((res: any) => {
                this.appStorageService.uniTrees = (res || []).map(item => new WspTreeModel(item));
                return this.appStorageService.uniTrees;
            })
        );
    }

    getUniversalParts(data) {
        return this.http.post(`${this.baseUrl}search/v2/universal-parts/gen-arts`, data).pipe(
            catchError(err => of(null))
        )
    }


    handleCheckedBrandsChanged(data) {
        this.isFilteringArticle = true;
        this.currentWspFilterRequest.suppliers = data;
        this.currentWspFilterRequest.offset = 0;
        this.resetPaginationData();
        return this.getWspArticles(this.currentWspFilterRequest);
    }

    handleCheckedBarFilterChanged(data, criterionFromInclude: Criteria[] = []) {
        this.isFilteringArticle = true;
        if (!data || data.length === 0) {
            data = [...criterionFromInclude];
        } else {
            const filterCidList = data.map(item => item.cid);
            const differentCriterion = criterionFromInclude.filter(item => filterCidList.indexOf(item.cid) === -1);
            data = [...data, ...differentCriterion];
        }
        this.currentWspFilterRequest.wsp_search_request.includeCriteria.criterion = data;
        this.currentWspFilterRequest.offset = 0;
        this.resetPaginationData();
        return this.getWspArticles(this.currentWspFilterRequest);
    }

    loadMoreArticle() {
        this.currentWspFilterRequest.offset += 1;
        return this.getWspArticles(this.currentWspFilterRequest)
            .pipe(
                tap(async (res) => {
                    const data = get(res, 'articles.content');
                    let articles = (data || []).map((article: any) => new ArticleModel(article));
                    this.moreData = articles;
                })
            );
    }

    resetPaginationData() {
        this.hasMoreData = false;
        this.moreData = null;
    }

    emitBrandFilterData(data) {
        this.brandsData = data;
        this.brandsDataSubject$.next(data);
    }

    emitBarFilterData(data) {
        this.barFilterData = data;
        this.barFilterDataSubject$.next(data);
    }

    groupArticle(data: ArticleModel[], selectedCate) {
        data.forEach(article => {
            article.cateId = selectedCate.id;
            article.group = this.selectedGenArtName;
            article.root = this.selectedGenArtName;
            selectedCate.rootDescription = this.selectedGenArtName;
        })
        const result = {
            key: selectedCate.nodeName,
            values: [
                {
                    cate: selectedCate,
                    cateBrands: [],
                    key: this.selectedGenArtName,
                    values: data
                }
            ]
        }
        this.currentArticleData = result;
        return [result]
    }

    getWspFilterOptions(requestBody: WspFilterRequest) {
        const url = `${this.baseUrl}${this.FILTER_URL}`;
        const request = new WspFilterRequest(requestBody);
        request.size = 1;
        return this.http.post(url, request).pipe(
            catchError(err => of(null)),
        );
    }

    getWspArticles(requestBody: WspFilterRequest) {
        const url = `${this.baseUrl}${this.FILTER_URL}`;
        const request = new WspFilterRequest(requestBody);
        this.currentWspFilterRequest = request;

        if (this.currentGetArticleListSub) {
            this.currentGetArticleListSub.unsubscribe();
        }

        return this.http.post(url, request).pipe(
            catchError(err => of(null)),
            tap((res) => {
                this.articleCount = get(res, 'articles.totalElements') || 0;
            })
        );
    }

    getFavoriteListItemById(id, type) {
        switch (type) {
            case FAVORITE_PROCESS_TYPE.LEAF_NODE:
                if (!this.favoriteService.currentGenArtStatus) {
                    return null;
                }
                return this.favoriteService.currentGenArtStatus.gaId == id ? this.favoriteService.currentGenArtStatus : null;
            default:
                return null;
                break;
        }
    }

    getFavoriteArticle(item: FavoriteItem) {
        const request = new LevelCategoryRequest({
            filterMode: SEARCH_MODE.FREE_TEXT,
            keyword: item.articleId,
            offset: 0,
            size: 10
        })
        const url = `${this.baseUrl}${this.FILTER_URL}`;
        return this.http.post(url, request).pipe(
            catchError(err => of(null)),
            map((res) => {
                const articles = get(res, 'articles.content');
                return (articles || []).map(item => new ArticleModel(item));
            })
        );
    }
}
