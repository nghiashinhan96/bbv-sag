import { Component, OnInit, Input, Output, EventEmitter, TemplateRef, OnDestroy } from '@angular/core';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { get } from 'lodash';
import { ArticlesService, ArticleModel, LibUserSetting, ArticleUtil, ArticleBroadcastKey, ArticleBasketModel, RELEVANCE_GROUP } from 'sag-article-detail';

import { NUMBER_ITEMS_REQUEST_AVAILABILITY } from '../../consts/article-list.const';
import { MultiLevelSelectedFilter } from '../../models/multi-level-selected-filter.model';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { catchError, map } from 'rxjs/operators';
import { BehaviorSubject, Observable, of, Subscription } from 'rxjs';
import { ArticleSort } from '../../models/article-sort.model';
import { TyreArticleSortUtil } from '../../utils/tyre-article-sort.utils';
import { ArticleSortUtil } from '../../utils/article-sort.util';
import { AffiliateUtil, BroadcastService, ProjectId } from 'sag-common';
import { ArticleListStorageService } from '../../services/article-list-storage.service';
import { ArticleGroupUtil } from '../../utils/article-group.util';
import { ArticleGroupSortService } from '../../services/article-group-sort.service';
import { cloneDeep } from 'lodash';
import { SagArticleListIntegrationService } from '../../services/article-list-integration.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'sag-article-list-non-group',
    templateUrl: './article-non-group.component.html',
    styleUrls: ['./article-non-group.component.scss']
})
export class SagArticleListNonGroupComponent implements OnInit, OnDestroy {

    @Input() enableRequestAvail = true;
    @Input() groupByRelevance = false;

    @Output() sendArticleResultData = new EventEmitter();
    @Output() sendArticlesGaData = new EventEmitter();

    @Input() set rootArticles(arts: ArticleModel[]) {
        // arts null or underfined case show nothing
        this.list = undefined;
        if (!!arts) {
            this.list = [];
            this.groups = [];
            if (arts.length > 0) {
                this.list = [...arts];
                this.initialData(arts);
            }
        }
    }

    @Input() set moreArticles(arts: ArticleModel[]) {
        if (!!arts && arts.length > 0) {
            const data = [];
            arts.forEach(art => {
                data.push(art);
            });
            this.list = [...this.list, ...data];
            this.articleIdsIsAddedToCart.clear();
            this.articlesIdIsAddedToCartFromEvent.clear();
            this.initialData(data, true);
        }
    }

    @Input() set hasMoreData(isMore: boolean) {
        this.hasMoreArticles = isMore;
    }

    @Input() articleMode: boolean;
    @Input() userSetting: LibUserSetting;

    @Input() selectedCriteraValues = [];
    @Input() filterDataOnBadge: MultiLevelSelectedFilter;
    @Input() notFoundTempRef: TemplateRef<any>;

    @Output() deselectMerkmaleEmitter = new EventEmitter();
    @Output() showMoreArticleEmitter = new EventEmitter();

    @Input() sortArticle: BehaviorSubject<ArticleSort>;

    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;

    @Input() isSubBasket = false;
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Output() popoversChanged = new EventEmitter();

    @Input() isAccessoryItem = false;
    @Input() isPartsItem = false;
    @Input() hideArticleInfo = false;
    @Input() vehicle: any;
    @Input() category: any;
    @Input() amountFromFileImported: number;
    @Input() rootModalName: string = '';

    list;
    groups = [];
    availabilities = [];
    hasMoreArticles: boolean;
    sort: ArticleSort;

    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate) || AffiliateUtil.isAxCz(this.config.affiliate);

    subs = new Subscription();
    hideNonAvail = false;

    isAutonet = false;
    articlesIdIsAddedToCartFromEvent = new Set<string>();
    articleListUsedForGaEvent: ArticleModel[] = [];
    articleIdsIsAddedToCart = new Set<string>();

    RELEVANCE_GROUP_TYPE = RELEVANCE_GROUP;
    originalPartsHeader = '';

    constructor(
        private translateService: TranslateService,
        private articlesService: ArticlesService,
        public config: ArticleListConfigService,
        private libStorage: ArticleListStorageService,
        private broadcastService: BroadcastService,
        private sorting: ArticleGroupSortService,
        private sagArticleListIntegrationService: SagArticleListIntegrationService
    ) {
        this.hideNonAvail = this.libStorage.hideNonAvailArticle;
        this.isAutonet = this.config.projectId === ProjectId.AUTONET;
    }

    ngOnInit() {
        this.originalPartsHeader = this.getOriginalPartHeaderTextByLangCode();
        this.subs.add(
            this.libStorage.isHideNonAvailArticleChange().subscribe(data => this.hideNonAvail = data)
        );
        this.subs.add(
            this.broadcastService.on(ArticleBroadcastKey.MINI_BASKET_CONDITION_EVENT)
                .subscribe(() => {
                    this.availabilities = [];
                    (this.list || []).forEach(art => {
                        art.availRequested = false;
                    });
                    this.requestErp(this.list);
                })
        );
        if (this.sortArticle) {
            this.subs.add(
                this.sortArticle.subscribe((sort: ArticleSort) => {
                    this.sort = sort;
                    this.sortArticlesInGroups(this.sort);
                })
            );
        }
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onArticleNumberClick(article: ArticleModel) {
        if (!article) {
            return;
        }
        this.onArticleNumberClickEmitter.emit(article);
    }

    onShowAccessories(article: ArticleModel) {
        this.onShowAccessoriesEmitter.emit(article);
    }

    onShowPartsList(article: ArticleModel) {
        this.onShowPartsListEmitter.emit(article);
    }

    onShowCrossReference(article: ArticleModel) {
        this.onShowCrossReferenceEmitter.emit(article);
    }

    showMoreArticles(callback) {
        this.showMoreArticleEmitter.emit(callback);
    }

    deselectMerkmaleValue(value) {
        this.deselectMerkmaleEmitter.emit(value);
    }

    private requestErp(requestErpArticles, callback?) {
        let index = 0;
        const requests = [];
        const stock_requests = [];
        let stock_obs: Observable<any>;

        const requestErpItems = requestErpArticles.filter(art => !art.pseudo);

        const erpRequest = {
            stockRequested: true
        };
        let filterStock, filterAvailAndPrice;
        if (this.isAutonet) {
            filterStock = [];
            filterAvailAndPrice = requestErpItems.filter(item => !item.autonetRequested);
        } else {
            filterStock = requestErpItems.filter(item => !item.stockRequested);
            filterAvailAndPrice = requestErpItems.filter(item => !item.priceRequested || !item.availRequested);
        }

        if (filterStock.length > 0) {
            stock_requests.push(this.articlesService.getArticlesInfoWithBatch(filterStock, filterStock.length, erpRequest));
        }

        if (stock_requests.length > 0) {
            stock_obs = forkJoin(stock_requests).pipe(
                map(respones => {
                    let allInfos = []
                    respones.forEach(res => {
                        const infos = this.articlesService.getDisplayedInfo(res.items);
                        allInfos = [...allInfos, ...infos];
                    });
                    requestErpArticles.forEach(article => {
                        const artInfo = (allInfos || []).find(p => p.key === article.pimId);
                        ArticleUtil.assignArticleErpInfo(article, artInfo, erpRequest, false);
                    });
                }),
                catchError(() => of(null))
            );
        } else {
            stock_obs = of(null);
        }

        stock_obs.subscribe(() => {
            const availRequest = {
                availabilityRequested: true,
                priceRequested: true
            };
            while (index < filterAvailAndPrice.length) {
                const requestItemsBatch = filterAvailAndPrice.slice(index, index + NUMBER_ITEMS_REQUEST_AVAILABILITY);
                requests.push(this.articlesService.getArticlesInfoWithBatch(requestItemsBatch, requestItemsBatch.length, availRequest));
                index += NUMBER_ITEMS_REQUEST_AVAILABILITY;
            }
            if (requests.length) {
                forkJoin(requests).subscribe(respones => {
                    let allInfos = []
                    respones.forEach(res => {
                        const infos = this.articlesService.getDisplayedInfo(res.items);
                        allInfos = [...allInfos, ...infos];
                    });
                    const avails = allInfos.filter(item => item.availabilities).map(item => {
                        return {
                            key: item.key,
                            value: item.availabilities
                        };
                    });
                    this.availabilities = [...this.availabilities, ...avails];
                    requestErpArticles.forEach(article => {
                        const artInfo = (allInfos || []).find(p => p.key === article.pimId);
                        this.sorting.assignArticleErpInfo(article, artInfo, availRequest, false);
                        const articleUsedForGaEventIdx = this.articleListUsedForGaEvent.findIndex(item => item.pimId === article.pimId);
                        if (articleUsedForGaEventIdx !== -1 && artInfo) {
                            this.articleListUsedForGaEvent[articleUsedForGaEventIdx].availabilities = artInfo.availabilities;
                            this.articleListUsedForGaEvent[articleUsedForGaEventIdx].price = artInfo.price;
                        }
                    });
                    if (callback) {
                        callback();
                    }
                });
            } else {
                if (callback) {
                    callback();
                }
            }
        })
    }

    sortArticlesInGroups(sort: ArticleSort) {
        if (!this.sort) {
            return;
        }

        if (sort.ascessdingSupplier === undefined && sort.ascessdingGrossPrice === undefined && sort.ascessdingNetPrice === undefined) {
            for (let g of this.groups) {
                g.values = [...g.originalValues];
            }
            return;
        }

        for (let g of this.groups) {
            g.values = this.sortArticles(g.values, sort);
        }

    }

    sortArticles(articles: ArticleModel[], sort: ArticleSort) {
        if (sort.ascessdingSupplier !== undefined) {
            return TyreArticleSortUtil.sortTyreBySupplier(articles, sort.ascessdingSupplier);
        }
        if (sort.ascessdingGrossPrice !== undefined) {
            return ArticleSortUtil.sortByGrossPrice(articles, sort.ascessdingGrossPrice);
        }
        if (sort.ascessdingNetPrice !== undefined) {
            return ArticleSortUtil.sortByNetPrice(articles, sort.ascessdingNetPrice);
        }

    }

    private groupArticlesByRelevance(articles: any[]) {
        if (this.groupByRelevance) {
            return ArticleGroupUtil.groupByRelevance(articles);
        }
        return [{
            key: null,
            ignoreNonAvailFilter: false,
            originalValues: [...articles],
            values: [...articles]
        }];
    }

    onSyncArticleEvent(event) {
        if (!this.articleIdsIsAddedToCart.size) {
            return;
        }
        const article = event;
        const articlePimId = article.pimId;
        const articleIdsIsAddedToCart = Array.from(this.articleIdsIsAddedToCart);
        const articleIdIsAddedToCartFound = articleIdsIsAddedToCart.find(item => item === articlePimId);
        if (!articleIdIsAddedToCartFound) {
            return;
        }
        const articlesIdIsAddedToCartFromEvent = Array.from(this.articlesIdIsAddedToCartFromEvent);
        const articleIdIsAddedToCartFromEventFound = articlesIdIsAddedToCartFromEvent.find(item => item === articlePimId);
        if (articleIdIsAddedToCartFromEventFound) {
            return;
        }
        const articleUsedForGaEventIndex = this.articleListUsedForGaEvent.findIndex(item => item.pimId === articlePimId);
        if (articleUsedForGaEventIndex !== -1) {
            this.articleListUsedForGaEvent[articleUsedForGaEventIndex].availabilities = article.availabilities;
            this.articleListUsedForGaEvent[articleUsedForGaEventIndex].price = article.price;
            this.articleListUsedForGaEvent[articleUsedForGaEventIndex].amountNumber = article.amountNumber;
        }
        this.articlesIdIsAddedToCartFromEvent.add(articlePimId);
        if (this.articleIdsIsAddedToCart.size === this.articlesIdIsAddedToCartFromEvent.size) {
            this.sendArticlesGaData.emit(this.articleListUsedForGaEvent);
        }
    }

    private getArticlesIsAddedToCart(articleList: ArticleModel[]) {
        const params: ArticleBasketModel[] = (articleList || []).map(item => {
            const article = {
                pimId: item.pimId,
                vehicle: this.vehicle,
                action: 'LOADED'
            } as ArticleBasketModel;
            return article;
        });
        return this.sagArticleListIntegrationService.getArticleListAreAddedToCart(params);
    }

    private async initialData(arts: ArticleModel[], isShowMoreArticleMode = false) {
        let spinner1, spinner2;
        if (!isShowMoreArticleMode) {
            spinner1 = this.config.spinner.start('connect-article-result-container .filtering');
            spinner2 = this.config.spinner.start('connect-article-result-container .article-list-section');
        }
        const articlesIsAddedToCart = await this.getArticlesIsAddedToCart(arts);
        const articleIdsIsAddedToCart = articlesIsAddedToCart.map(item => item.pimId);
        this.articleIdsIsAddedToCart = new Set(articleIdsIsAddedToCart);
        this.articleListUsedForGaEvent = cloneDeep(this.list);
        this.groups = this.groupArticlesByRelevance(this.list);
        this.sendArticleResultData.emit(arts);
        if (!isShowMoreArticleMode) {
            this.config.spinner.stop(spinner1);
            this.config.spinner.stop(spinner2);
            this.config.spinner.stop();
        }
        this.requestErp(arts, () => {
            if (this.sort) {
                this.sortArticlesInGroups(this.sort);
            }
            if (this.articleIdsIsAddedToCart.size === 0) {
                this.sendArticlesGaData.emit(this.list);
            }
        });
    }

    private getOriginalPartHeaderTextByLangCode() {
        const languageCode = this.config.appLangCode || this.config.defaultLangCode;
        const headerTexts = get(this.userSetting, 'externalPartSettings.headerNames', []);
        const header = headerTexts.find(item => item.langIso.toLocaleLowerCase() === languageCode);
    
        return header && header.content || this.translateService.instant('ARTICLE.GROUPS.ORIGINAL_PART');
      }
}
