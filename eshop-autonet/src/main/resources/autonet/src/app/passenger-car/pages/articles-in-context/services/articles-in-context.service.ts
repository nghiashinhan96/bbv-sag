import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/internal/operators/map';
import { uniqBy } from 'lodash';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { ArticleInContextSearchRequest } from '../article-in-context-search-request.interface';
import { ArticleModel, BarFilter, CategoryModel } from 'sag-article-detail';
import { ArticleGroupModel, BrandFilterItem, OlyslagerModel } from 'sag-article-list';

@Injectable({
    providedIn: 'root'
})
export class ArticleInContextService {
    private baseUrl = environment.baseUrl;
    private articles: ArticleModel[] = [];

    private dataSubject$ = new BehaviorSubject(undefined);
    data$ = this.dataSubject$.asObservable();

    private brandsDataSubject$ = new BehaviorSubject(undefined);
    brandsData$ = this.brandsDataSubject$.asObservable();
    brandsData: BrandFilterItem[] = [];

    private barFilterDataSubject$ = new BehaviorSubject(undefined);
    barFilterData$ = this.barFilterDataSubject$.asObservable();
    barFilterData: BarFilter[] = [];

    constructor(private http: HttpClient) { }

    getVehiclesByVehId(vehId) {
        const url = `${this.baseUrl}search/vehicles/type/Id/${vehId}`;
        return this.http.get(url, { observe: 'body' });
    }

    emitData(data: any[]) {
        this.dataSubject$.next(data);
    }

    emitBrandFilterData(data) {
        this.brandsData = data;
        this.brandsDataSubject$.next(data);
    }

    emitBarFilterData(data) {
        this.barFilterData = data;
        this.barFilterDataSubject$.next(data);
    }

    resetResultList() {
        this.articles = [];
        this.dataSubject$.next(undefined);
    }

    getArticles(data: ArticleInContextSearchRequest) {
        const url = `${this.baseUrl}search/v2/parts`;
        return this.http.post(url, data, { observe: 'body' }).pipe(map((body: any) => {
            const olyslagerPopup = body.olyslagerPopup;
            if (olyslagerPopup) {
                return {
                    type: 'olyslager',
                    data: body.olyslagerTypeIds.map(o => new OlyslagerModel(o))
                };

            } else {
                const articles = body && body.articles && body.articles.content;
                return {
                    type: 'article',
                    data: AppHelperUtil.convertAutonetData(articles)
                };
            }
        }));
    }

    groupArticle(articles: ArticleModel[], selectedCate: CategoryModel[], uncheckCateIds: string[]) {
        const totalArticles = uniqBy(this.articles.concat(articles), 'pimId');
        this.articles = [];
        totalArticles.forEach((article: ArticleModel) => {
            selectedCate.forEach(cate => {
                /**
                 * Debug only
                 */
                // if (!article.isBelongToCate(cate)) {
                //     console.log(article.pimId, ' - is not belong to cate', cate.id);
                // } else {
                //     if (article.noCriteria()) {
                //         console.log(article.pimId, ' - has no criteria');
                //         if (cate.hasCidEqual100()) {
                //             console.log(cate.id, ' - has criteria 100');
                //         }
                //     } else {
                //         if (!article.hasCommonCriteria(cate.criterias)) {
                //             console.log(article.pimId, ' - does not match any criterias -', cate.id);
                //             console.log('articleCriteria:', article.criteria);
                //             console.log('cateCriteria:', cate.criterias);
                //         }
                //     }
                // }

                if (article.isBelongToCate(cate) &&
                    (article.hasCommonCriteria(cate.criterias) || (article.noCriteria() && cate.hasCidEqual100()))) {
                    this.setGroupData(new ArticleModel(article), cate);
                }
            });
        });

        if (uncheckCateIds && uncheckCateIds.length > 0) {
            this.articles = this.articles.filter(art => uncheckCateIds.indexOf(art.cateId) === -1);
        }

        const result = [];
        selectedCate.forEach(cate => {
            const arts = this.articles.filter(art => art.group === cate.description);
            if (arts.length > 0) {
                this.addToResult(result, cate, arts);
            }
        });
        return result;
    }

    private setGroupData(article: ArticleModel, cate: CategoryModel) {
        article.cateId = cate.id;
        article.group = cate.description;
        article.root = cate.rootDescription;
        const isExistedArticle = this.articles.find(art => art.id === article.id);
        if (!isExistedArticle) {
            this.articles.push(article);
        }
    }

    private addToResult(result, cate: CategoryModel, articles: ArticleModel[]) {
        const root = result.find(r => r.key === cate.rootDescription);
        if (root) {
            const group = root.values.find(g => g.key === cate.description);
            if (group) {
                group.values = group.values.concat(articles);
            } else {
                root.values.push({
                    key: cate.description,
                    values: articles,
                    cate,
                    cateBrands: this.getCateBrands(cate)
                } as ArticleGroupModel);
            }
        } else {
            result.push({
                key: cate.rootDescription,
                values: [{
                    key: cate.description,
                    values: articles,
                    cate,
                    cateBrands: this.getCateBrands(cate)
                }]
            });
        }
    }
    // for Autonet logic
    private getCateBrands(cate: any) {
        return (cate.genArts || []).reduce((brands, genArt, i) => {
            const sub = (genArt.sorts || []).reduce((subBrands, item) => {
                return [...subBrands, ...item.brands || []];
            }, []);
            return [...brands, ...sub];
        }, []);
    }
}
