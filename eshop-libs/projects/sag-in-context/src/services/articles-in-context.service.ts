import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/internal/operators/map';
import { uniqBy } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import {
    ArticleModel,
    CategoryModel,
    ArticleBasketModel,
    BarFilter
} from 'sag-article-detail';
import {
    ArticleGroupModel,
    BrandFilterItem,
    OilService
} from 'sag-article-list';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { SagInContextSearchRequest } from '../interfaces/article-in-context-search-request.interface';
import { SagInContextVinPopupComponent } from '../components/vin-popup/vin-popup.component';
import { SagInContextConfigService } from './articles-in-context-config.service';
import { SagInContextIntegrationService } from './articles-in-context-integration.service';
import { SubSink } from 'subsink';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Injectable()
export class ArticleInContextService {
    private dataSubject$ = new BehaviorSubject(undefined);
    data$ = this.dataSubject$.asObservable();
    articles: ArticleModel[] = [];
    private grouppedData;

    private brandsDataSubject$ = new BehaviorSubject(undefined);
    brandsData$ = this.brandsDataSubject$.asObservable();
    brandsData: BrandFilterItem[] = [];


    private barFilterDataSubject$ = new BehaviorSubject(undefined);
    barFilterData$ = this.barFilterDataSubject$.asObservable();
    barFilterData: BarFilter[] = [];

    directMatches = [];

    subs = new SubSink();
    bsModalRef: BsModalRef;
    hasPositionWithCvpValueIsNull = false;

    constructor(
        private http: HttpClient,
        private config: SagInContextConfigService,
        private integrationService: SagInContextIntegrationService,
        private oilService: OilService
    ) { }

    isGlassBodyWork(data: ArticleBasketModel, modalService: BsModalService) {
        return new Promise(resolve => {
            if (data.category && data.category.check && AffiliateUtil.isAffiliateCH(this.config.affiliate)) { // isGlassBodyWork
                if (data.cartKey || (data.vehicle && data.vehicle.vinSearch)) {
                    resolve(null);
                } else {
                    // show confirmation popup.
                    this.bsModalRef = modalService.show(SagInContextVinPopupComponent, {
                        class: 'modal-md',
                        ignoreBackdropClick: true,
                        initialState: {
                            vinCode: ''
                        }
                    });

                    this.subs.sink = modalService.onHidden.subscribe(() => {
                        if (this.bsModalRef && this.bsModalRef.content && this.bsModalRef.content instanceof SagInContextVinPopupComponent) {
                            this.subs.unsubscribe();
                            const vin = this.bsModalRef.content.vinCode;
                            data.article.glassOrBody = true;
                            data.vehicle.vin = vin;
                            resolve(vin);
                        }
                    })
                }
            } else {
                resolve(null);
            }
        });
    }

    getCateInfoByPimId(pimId: string) {
        if (this.grouppedData) {
            let result: any = {};
            this.grouppedData.forEach(g => {
                (g.values || []).forEach(sub => {
                    const matchedArt = (sub.values || []).find(art => art.pimId === pimId);
                    if (!!matchedArt) {
                        result = sub.cate;
                    }
                });
            });
            return result;
        }
        return null;
    }

    emitData(data: any[]) {
        this.recordFeedbackArticle(data);
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
        this.directMatches = [];
        this.dataSubject$.next(undefined);
    }

    getArticles(body: SagInContextSearchRequest) {
        const url = `${this.config.baseUrl}search/v2/parts`;
        return this.http.post(url, body, { observe: 'body' }).pipe(map(({ oilTypeIds, articles }: any) => {
            if (oilTypeIds) {
                const { data, selectedOil } = this.oilService.filterOilGroup(oilTypeIds);
                const selectedCateId = (oilTypeIds).map(oil => oil.cateId);
                return {
                    type: 'oilTypeIds',
                    data,
                    selectedCateId,
                    selectedOil
                };

            } else if (articles) {
                return {
                    type: 'article',
                    data: (articles.content || []).map(art => new ArticleModel(art))
                };
            }
        }));
    }

    groupArticle(articles: ArticleModel[], selectedCate: CategoryModel[], uncheckCateIds: string[], vehId: string) {
        const totalArticles = uniqBy(this.articles.concat(articles), 'pimId');
        this.articles = [];
        totalArticles.forEach((article: ArticleModel) => {
            selectedCate.forEach(cate => {
                // For debug
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
                    this.setGroupData(new ArticleModel(article), cate, vehId);
                }
            });
        });

        if (uncheckCateIds && uncheckCateIds.length > 0) {
            this.articles = this.articles.filter(art => uncheckCateIds.indexOf(art.cateId) === -1);
        }

        const result = [];
        if (this.directMatches && this.directMatches.length > 0) {
            this.directMatches.forEach(item => {
                result.push({
                    key: item.pseudoCategoryName,
                    pseudo: true,
                    values: [{
                        pseudo: true,
                        values: item.directMatchesArticles
                    }]
                })
            })
        }
        selectedCate.forEach(cate => {
            const arts = this.articles.filter(art => art.cateId === cate.id);
            if (arts.length > 0) {
                this.addToResult(result, cate, arts);
            }
        });
        // // group again by GAID
        // result.forEach(g => {
        //     g.values.forEach(data => {
        //         const gaidGroup = ArticleSortUtil.groupBy(data.values, (article: any) => [article.gaID]);
        //         gaidGroup.sort((g1, g2) => {
        //             return g1[0].gaID - g2[0].gaID;
        //         });
        //         data.values = gaidGroup.map(gaGroup => {
        //             return {
        //                 key: '',
        //                 values: gaGroup,
        //                 cate: data.cate,
        //                 cateBrands: data.cateBrands
        //             };
        //         });
        //     });
        // });
        // keep this result for search later
        this.grouppedData = result;
        return result;
    }

    updateArticleData(id, attrs: { availabilities: any, cartKey: string }) {
        const i = (this.articles || []).findIndex(art => art.pimId === id);
        if (i !== -1) {
            if (attrs.availabilities) {
                this.articles[i].availabilities = attrs.availabilities;
            }
        }
    }

    removePseudoGroup(key) {
        this.directMatches = this.directMatches.filter(item => item.pseudoCategoryName !== key);
    }

    private setGroupData(article: ArticleModel, cate: CategoryModel, vehId: string) {
        article.cateId = cate.id;
        article.group = cate.getDescriptionByLevel([1]);
        article.root = cate.rootDescription;
        const isExistedArticle = this.articles.find(art => art.id === article.id);
        if (!isExistedArticle) {
            this.articles.push(article);
        }
    }

    private addToResult(result, cate: CategoryModel, articles: ArticleModel[]) {
        const root = result.find(r => r.key === cate.rootDescription);
        const cateDesc = cate.getDescriptionByLevel([1]);
        if (root) {
            const group = root.values.find(g => g.key === cateDesc);
            if (group) {
                group.values = uniqBy(group.values.concat(articles), 'pimId');
            } else {
                root.values.push({
                    key: cateDesc,
                    values: articles,
                    cate,
                    cateBrands: this.getCateBrands(cate)
                } as ArticleGroupModel);
            }
        } else {
            result.push({
                key: cate.rootDescription,
                values: [{
                    key: cateDesc,
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
                return [...subBrands, ...item.brands];
            }, []);
            return [...brands, ...sub];
        }, []);
    }

    private recordFeedbackArticle(data) {
        this.integrationService.clearFeedbackArticleResults();
        for (const root of data || []) {
            for (const g of root.values || []) {
                for (const article of g.values || []) {
                    this.integrationService.recordFeedbackArticleResult(article);
                }
            }
        }
    }
}
