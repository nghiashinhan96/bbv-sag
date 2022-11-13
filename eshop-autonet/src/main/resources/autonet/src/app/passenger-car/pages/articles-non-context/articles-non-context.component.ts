import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { get, cloneDeep, isEqual } from 'lodash';
import { map } from 'rxjs/internal/operators/map';
import { finalize } from 'rxjs/internal/operators/finalize';
import { Observable } from 'rxjs/internal/Observable';
import { ArticleNonContextService } from './services/article-non-context.service';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { takeUntil, tap, debounceTime } from 'rxjs/operators';
import { Subject } from 'rxjs/internal/Subject';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';
import { Subscription } from 'rxjs';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { ARTICLE_LIST_TYPE, FilterCategory, MerkmaleCategory, MultiLevelSelectedFilter, MultiLevelSelectedFilterModel, NonMerkmaleCategory, SEARCH_MODE } from 'sag-article-list';
import { BroadcastService } from 'sag-common';
import { ArticleBasketModel, ArticleBroadcastKey, ArticleModel, FavoriteItem } from 'sag-article-detail';
import { MerkmaleGaids } from 'sag-article-list/models/merkmale-gaid.model';
import { ArticleNotContextConst } from './article-not-context-result-list.constant';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { BsModalService } from 'ngx-bootstrap/modal';
import { CommonModalService } from 'src/app/shared/autonet-common/services/common-modal.service';
@Component({
    selector: 'autonet-articles-non-context',
    templateUrl: './articles-non-context.component.html',
    styleUrls: ['./articles-non-context.component.scss']
})
export class ArticlesNonContextComponent implements OnInit, OnDestroy {

    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
    data: any;
    hasMasterData = true;
    moreData: any;
    hasMoreData: boolean;
    filterData: any;
    nonMerkmaleFilterData: any;
    subSelectedCategory: any;
    selectedRootCategoryName: string;
    filterDataOnBadge: MultiLevelSelectedFilter;
    resetAllFilters: boolean;
    uncheckMerkmale: any;
    useMerkmaleFilter: boolean;
    attributes: any;
    showList = false;
    keywords: string;

    numberOfSearchedArticles = 0;
    search$ = new Subject();

    url = `${UrlUtil.autonetServer(this.storage.country)}portal/Products.aspx`;
    params: any;
    groupByRelevance = false;
    merkmaleBrandFilterData = [];
    private categoryLevel: FilterCategory;
    private additionalRequest: NonMerkmaleCategory;
    private originalAdditionalRequest: NonMerkmaleCategory;
    private contextKey = '';

    private queryParams: any;
    private typeMode: string;
    private destroy$ = new Subject();
    private searchingSubs: Subscription;
    constructor(
        private articleNonContextService: ArticleNonContextService,
        private route: ActivatedRoute,
        private articleListBroadcastService: BroadcastService,
        private autonetCommonService: AutonetCommonService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        public storage: AppStorageService,
        private modalService: BsModalService,
        private cdr: ChangeDetectorRef,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        this.autonetCommonService.isInArticleListPage = true;
        const autonet = this.storage.autonet as AutonetAuthModel;

        let spinner;
        this.route.queryParams.pipe(
            tap((queryParams) => {
                const type = queryParams.type;
                this.groupByRelevance = type === SEARCH_MODE.FREE_TEXT || type === SEARCH_MODE.ARTICLE_NUMBER || type === SEARCH_MODE.ID_SAGSYS;
                this.showList = false;
                this.cdr.detectChanges();
                this.subSelectedCategory = null;
                this.uncheckMerkmale = null;
                this.filterDataOnBadge = null;
                this.filterData = null;
                this.nonMerkmaleFilterData = null;
                this.attributes = null;
                this.categoryLevel = null;
                this.additionalRequest = null;
                this.data = null;
                this.moreData = null;
                this.hasMoreData = false;
                spinner = SpinnerService.start('body', { css: 'non-context' });
            }),
            switchMap(queryParams => this.searchArticle(queryParams).pipe(finalize(() => SpinnerService.stop(spinner)))),
            map(filterAndData => this.extractData(filterAndData))
        ).subscribe(res => {
            this.hasMasterData = this.data && this.data.length > 0;
            this.params = {
                sid: '',
                14: autonet.lid,
                uid: autonet.uid,
                SearchFor: this.queryParams.articleId || this.queryParams.articleNr || ''
            };
            const pageUrl = AppHelperUtil.objectToUrl(this.url, this.params);
            this.params['400'] = pageUrl;
        });

        this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'ADD':
                        this.autonetCommonService.addToCart(data.article);
                        break;
                }
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((code: string) => {
                this.router.navigate([], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code },
                    relativeTo: this.activatedRoute
                });
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((vehicleId: string) => {
                this.router.navigate(['../../../', 'vehicle', vehicleId], { relativeTo: this.activatedRoute });
            });

        this.search$.pipe(debounceTime(600)).subscribe(({filterSpinner, containerSpinner, hasChanged}) => {
            if (this.searchingSubs) {
                this.searchingSubs.unsubscribe();
            }
            this.searchingSubs = this.articleNonContextService.getNonMerkmaleCategory(this.additionalRequest, hasChanged).pipe(
                finalize(() => {
                    SpinnerService.stop(filterSpinner);
                    SpinnerService.stop(containerSpinner);
                })
            ).subscribe(filter => {
                const articles = get(filter, 'articles.content');
                this.data = this.convertData(articles);
                this.hasMoreData = !get(filter, 'articles.last');
                this.numberOfSearchedArticles = get(filter, 'articles.totalElements') || 0;
                this.contextKey = get(filter, 'context_key');
            });
        });
        setTimeout(() => {
            this.autonetCommonService.enableFreetextSearch(true);
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
        this.search$.complete();
        if (this.searchingSubs) {
            this.searchingSubs.unsubscribe();
        }
        this.autonetCommonService.enableFreetextSearch(false);
        this.autonetCommonService.isInArticleListPage = false;
    }

    get hasChanged() {
        if (this.useMerkmaleFilter) {
            const hasMultipleLevelGaids = get(this.categoryLevel, 'multipleLevelGaids.length', 0) > 0;
            const hasBrandFilter = get(this.categoryLevel, 'suppliers.length', 0) > 0;
            return hasMultipleLevelGaids || hasBrandFilter;
        } else {
            return !isEqual(this.additionalRequest, this.originalAdditionalRequest);
        }
    }

    goBack() {
        this.router.navigate(['/cars']);
    }

    selectRootCategory({ multipleLevelGaids, gaId, nodeId }) {
        SpinnerService.start('autonet-articles-non-context .result');
        this.articleNonContextService.getSubMerkmaleCategory(gaId, this.categoryLevel, true).pipe(
            finalize(() => SpinnerService.stop()),
            map(subCategory => get(subCategory, 'gen_arts'))
        ).subscribe(data => {
            if (data && data.length) {
                this.subSelectedCategory = {
                    filter: data[0].children,
                    selectedGaid: multipleLevelGaids,
                    selectedNodeId: nodeId
                };
            }
        });
    }

    selectCriteriaValue(data: { multipleLevelGaids: MerkmaleGaids[], filterBadge: MultiLevelSelectedFilter }) {
        this.categoryLevel.offset = 0;
        this.categoryLevel = this.categoryLevel.setCategory(data.multipleLevelGaids);
        this.setKeepDirectAndOriginalMatchParam();
        const spinner = SpinnerService.start('autonet-articles-non-context .result');
        this.articleNonContextService.getMerkmaleCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => SpinnerService.stop(spinner))
            ).subscribe(result => {
                const articles = get(result, 'articles.content');
                this.hasMoreData = !get(result, 'articles.last');
                this.numberOfSearchedArticles = get(result, 'articles.totalElements') || 0;
                this.data = this.convertData(articles);
                this.filterDataOnBadge = new MultiLevelSelectedFilterModel(data.filterBadge);
                this.contextKey = get(result, 'context_key');
            });
    }

    resetFilter() {
        this.contextKey = '';
        if (this.useMerkmaleFilter) {
            this.categoryLevel.suppliers = [];
            this.selectCriteriaValue({ multipleLevelGaids: [], filterBadge: new MultiLevelSelectedFilterModel() });
            this.resetAllFilters = true;
            this.merkmaleBrandFilterData = cloneDeep(this.merkmaleBrandFilterData);
        } else {
            const filterSpinner = SpinnerService.start('autonet-articles-non-context .sag-article-filter');
            const containerSpinner = SpinnerService.start('autonet-articles-non-context .result');
            this.articleNonContextService.getNonMerkmaleCategory(this.categoryLevel).pipe(
                finalize(() => {
                    SpinnerService.stop(filterSpinner);
                    SpinnerService.stop(containerSpinner);
                })
            ).subscribe(filter => {
                const articles = get(filter, 'articles.content');
                this.data = this.convertData(articles);
                this.hasMoreData = !get(filter, 'articles.last');
                this.nonMerkmaleFilterData = get(filter, 'filters');
                this.numberOfSearchedArticles = get(filter, 'articles.totalElements') || 0;
                this.sortAndSetNonMerkmaleFilterData();
                this.additionalRequest = cloneDeep(this.categoryLevel) as NonMerkmaleCategory;
            });
        }
    }

    deselectMerkmale(val) {
        this.uncheckMerkmale = val;
    }

    goToPreviousRoute() {

    }

    showMoreArticles(callback) {
        const spinner = SpinnerService.start('autonet-articles-non-context .result');
        let obserRes$;
        if (this.useMerkmaleFilter) {
            this.categoryLevel.offset += 1;
            const categoryLevel = cloneDeep(this.categoryLevel);
            categoryLevel.contextKey = this.hasChanged ? this.contextKey : categoryLevel.contextKey;
            obserRes$ = this.articleNonContextService.getMerkmaleCategory(categoryLevel);
        } else {
            this.additionalRequest.offset += 1;
            const additionalRequest = cloneDeep(this.additionalRequest);
            additionalRequest.contextKey = this.contextKey || additionalRequest.contextKey;
            obserRes$ = this.articleNonContextService.getMerkmaleCategory(additionalRequest);
        }
        obserRes$.pipe(
            finalize(() => {
                SpinnerService.stop(spinner);
                if (callback) {
                    callback();
                }
            })
        ).subscribe(result => {
            const articles = get(result, 'articles.content');
            this.hasMoreData = !get(result, 'articles.last');
            this.moreData = this.convertData(articles);
        });
    }

    handleAdditionalFilter(additionalFields: any) {
        const filterSpinner = SpinnerService.start('autonet-articles-non-context .sag-article-filter');
        const containerSpinner = SpinnerService.start('autonet-articles-non-context .result');
        this.additionalRequest.offset = 0;
        this.setAdditionalFields(additionalFields);
        this.setSuppliersAndGaids(additionalFields);
        const hasChanged = this.hasChanged;
        this.search$.next({filterSpinner, containerSpinner, hasChanged});
    }

    onSelectFavoriteItem(item: FavoriteItem) {
        const queryParams: any = {
            type: SEARCH_MODE.FREE_TEXT,
            articleId: item.articleId
        };
        this.router.navigate(['../../../', 'article'], {
            queryParams: queryParams,
            relativeTo: this.activatedRoute
        });
    }

    private convertData(articles) {
        return AppHelperUtil.convertAutonetData(articles);
    }

    private setSuppliersAndGaids(additionalFields: any) {
        if (additionalFields.type === 'suppliers') {
            this.additionalRequest.suppliers = additionalFields.isChecked ?
                [...this.additionalRequest.suppliers, additionalFields.filterId] :
                this.additionalRequest.suppliers.filter(supplier => supplier !== additionalFields.filterId);
        }

        if (additionalFields.type === 'gen_arts') {
            this.additionalRequest.gaids = additionalFields.isChecked ?
                [...this.additionalRequest.gaids, additionalFields.filterId] :
                this.additionalRequest.gaids.filter(gaid => gaid !== additionalFields.filterId);
        }
    }

    private setAdditionalFields(additionalFields: any) {
        switch (this.typeMode) {
            case SEARCH_MODE.BATTERY_SEARCH:
                this.additionalRequest.batterySearchRequest
                    .setAdditionalFields(additionalFields.isChecked, additionalFields.type, additionalFields.filterId);
                break;
            case SEARCH_MODE.OIL_SEARCH:
                this.additionalRequest.oilSearchRequest
                    .setAdditionalFields(additionalFields.isChecked, additionalFields.type, additionalFields.filterId);
                break;
            case SEARCH_MODE.BULB_SEARCH:
                this.additionalRequest.bulbSearchRequest
                    .setAdditionalFields(additionalFields.isChecked, additionalFields.type, additionalFields.filterId);
                break;
            case SEARCH_MODE.TYRES_SEARCH:
                this.additionalRequest.tyreSearchRequest
                    .setAdditionalFields(additionalFields.isChecked, additionalFields.type, additionalFields.filterId);
                break;
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
                this.additionalRequest.motorTyreSearchRequest
                    .setAdditionalFields(additionalFields.isChecked, additionalFields.type, additionalFields.filterId);
                break;
        }
    }

    private searchArticle(queryParams: any): Observable<any> {
        this.showList = true;
        this.queryParams = queryParams;
        this.typeMode = queryParams.type;
        if (this.typeMode === SEARCH_MODE.FREE_TEXT.toString()) {
            this.keywords = queryParams.articleId;
            this.useMerkmaleFilter = true;
            this.categoryLevel = new MerkmaleCategory(SEARCH_MODE.FREE_TEXT);
            this.categoryLevel.setKeyword(queryParams.articleId);
            return this.articleNonContextService.getMerkmaleCategory(this.categoryLevel);
        } else {
            this.useMerkmaleFilter = false;
            if (queryParams.articleNr) {
                this.keywords = queryParams.articleNr;
                this.categoryLevel = new NonMerkmaleCategory(SEARCH_MODE.ARTICLE_NUMBER);
                this.categoryLevel.setKeyword(queryParams.articleNr);
                return this.articleNonContextService.getNonMerkmaleCategory(this.categoryLevel);
            } else {
                this.categoryLevel = new NonMerkmaleCategory(this.typeMode);
                this.categoryLevel.setFilterType(queryParams);
                return this.articleNonContextService.getNonMerkmaleCategory(this.categoryLevel);
            }
        }
    }

    private extractData(filterAndData: any) {
        this.categoryLevel.contextKey = get(filterAndData, 'context_key', '');
        const articles = get(filterAndData, 'articles.content');
        this.numberOfSearchedArticles = get(filterAndData, 'articles.totalElements') || 0;
        this.data = this.convertData(articles);
        this.hasMoreData = !get(filterAndData, 'articles.last');
        if (this.useMerkmaleFilter) {
            this.filterData = get(filterAndData, 'filters.gen_arts') || null;
            this.getMerkmaleSuppliers(filterAndData);
        } else {
            this.nonMerkmaleFilterData = get(filterAndData, 'filters');
            this.sortAndSetNonMerkmaleFilterData();
            const lib = this.articleNonContextService.composeAttributes(this.queryParams);
            this.additionalRequest = cloneDeep(this.categoryLevel) as NonMerkmaleCategory;
            this.originalAdditionalRequest = cloneDeep(this.categoryLevel) as NonMerkmaleCategory;
            this.attributes = lib;
        }
    }

    sortAndSetNonMerkmaleFilterData() {
        if (!this.nonMerkmaleFilterData) {
            return;
        }
        const filterData = this.nonMerkmaleFilterData;
        this.nonMerkmaleFilterData = [];
        // tslint:disable-next-line: forin
        for (const key in filterData) {
            if (filterData[key].length > 0) {
                this.nonMerkmaleFilterData.push({
                    key,
                    value: filterData[key]
                });
            }
        }
        this.removeUnusedNonMerkmaleFilterItem();
    }

    removeUnusedNonMerkmaleFilterItem() {
        if (!this.attributes) {
            return;
        }
        this.attributes.attributes.map(attr => {
            if (ArticleNotContextConst.ATTR_FILTER_MAP.hasOwnProperty(attr.key)) {
                const filterKey = ArticleNotContextConst.ATTR_FILTER_MAP[attr.key];
                this.nonMerkmaleFilterData = this.nonMerkmaleFilterData.filter(item => item.key !== filterKey);
            }
        });
    }

    onArticleNumberClick(article: ArticleModel) {
        this.commonModalService.showReplaceModal(article);
    }

    onShowAccessories(data: any) {
        this.commonModalService.showAccessoriesModal(data);
    }

    onShowPartsList(data: any) {
        this.commonModalService.showPartsListModal(data);
    }

    onShowCrossReference(data: any) {
        this.commonModalService.showCrossReferenceModal(data);
    }

    onMerkmaleBrandFilterChange(brandFilters: string[]) {
        this.categoryLevel.suppliers = brandFilters;
        this.categoryLevel.offset = 0;
        this.setKeepDirectAndOriginalMatchParam();
        const spinner = SpinnerService.start('autonet-articles-non-context .result');
        this.articleNonContextService.getMerkmaleCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => SpinnerService.stop(spinner))
            ).subscribe(result => {
                const articles = get(result, 'articles.content');
                this.hasMoreData = !get(result, 'articles.last');
                this.numberOfSearchedArticles = get(result, 'articles.totalElements') || 0;
                this.data = this.convertData(articles);
                this.contextKey = get(result, 'context_key');
            });
    }

    private getMerkmaleSuppliers(filterAndData) {
        this.merkmaleBrandFilterData = get(filterAndData, 'filters.suppliers');
    }

    private setKeepDirectAndOriginalMatchParam() {
        this.categoryLevel.setKeepDirectAndOriginalMatch(true);
    }
}
