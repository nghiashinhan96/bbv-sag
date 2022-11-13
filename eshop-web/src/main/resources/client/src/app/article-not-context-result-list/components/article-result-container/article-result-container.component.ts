import { Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationEnd, NavigationStart, Router } from '@angular/router';

import { cloneDeep, get, isEqual } from 'lodash';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable, Subscription } from 'rxjs';
import { finalize, first, map, switchMap, tap } from 'rxjs/operators';
import { SubSink } from 'subsink';

import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { BatteryAnalyticService } from 'src/app/analytic-logging/services/battery-analytic.service';
import { Constant, SHOPPING_BASKET_PAGE } from 'src/app/core/conts/app.constant';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { LevelCategory } from '../../models/level-category.model';
import { MultiLevelCategory } from '../../models/multi-level-category.model';
import { SingleLevelCategory } from '../../models/single-level-category.model';
import { FreetextArticleService } from '../../service/freetext-article.service';
import { NonMerkmaleBusinessService } from '../../service/non-merkmale-business.service';
import { BroadcastService, ADS_TARGET_NAME } from 'sag-common';
import { MerkmaleGaids } from 'sag-article-list/models/merkmale-gaid.model';
import { MultiLevelSelectedFilter, MultiLevelSelectedFilterModel, ARTICLE_LIST_TYPE, SEARCH_MODE } from 'sag-article-list';
import { ArticleModel, ArticleBasketModel, ArticleBroadcastKey, CZ_AVAIL_STATE, FavoriteItem } from 'sag-article-detail';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { ArticleNotContextConst } from '../../constants/article-not-context-result-list.constant';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { AnalyticEventType, SUB_LIST_NAME_TYPE } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { SEARCH_TYPE } from '../../constants/search-type.const';
import { ARTICLE_SEARCH_MODE, LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH, LIB_VEHICLE_ARTICLE_NUMBER_SEARCH } from 'sag-article-search';
import { HeaderSearchTypeEnum } from 'src/app/layout/components/header/components/header-search/header-search-type.enum';
import { FavoriteCommonService } from 'src/app/shared/connect-common/services/favorite-common.service';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';

const ARTICLE_NR_SEARCH_TYPE = 'article_nr';

@Component({
    selector: 'connect-article-result-container',
    templateUrl: './article-result-container.component.html',
    styleUrls: ['./article-result-container.component.scss'],
})
export class ArticleResultContainerComponent implements OnInit, OnDestroy {

    @Input() articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
    @Input() data: any;

    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isSb = AffiliateUtil.isSb(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
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
    adsTargetName;
    numberOfSearchedArticles = 0;
    cartItemQuantity = 0;
    searchMode = SEARCH_MODE;
    typeMode: string;
    keywords: string;
    groupByRelevance = false;
    merkmaleBrandFilterData = [];

    private categoryLevel: LevelCategory;
    private additionalRequest: SingleLevelCategory;
    private originalAdditionalRequest: SingleLevelCategory;
    private originalContextKey = '';
    private incomingContextKey = '';
    private queryParams: any;
    private paramsSubscription: Subscription;
    private subs = new SubSink();
    private filterSpinner;
    private containerSpinner;
    private eventSent = false;
    private isArticleResultPage = false;
    private multipleLevelGaids = null;

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    constructor(
        public appStorage: AppStorageService,
        public userService: UserService,
        private storage: LocalStorageService,
        private freetextArticleService: FreetextArticleService,
        private router: Router,
        private route: ActivatedRoute,
        private batteryAnalyticService: BatteryAnalyticService,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService,
        private nonMerkmaleBS: NonMerkmaleBusinessService,
        private articleListBroadcastService: BroadcastService,
        private articleBasketService: ArticleShoppingBasketService,
        private shoppingBasketService: ShoppingBasketService,
        public creditLimitService: CreditLimitService,
        private fbRecordingService: FeedbackRecordingService,
        private analyticService: AnalyticLoggingService,
        private favoriteCommonService: FavoriteCommonService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        this.subs.sink = this.shoppingBasketService.basketQuantity$
            .subscribe(quantity => {
                this.cartItemQuantity = quantity;
            });

        this.articleBasketService.loadMiniBasket().then();
        this.articleBasketService.observeArticleRemove();
        this.articleBasketService.observeArticleUpdate();
        this.isArticleResultPage = this.router.url.indexOf('/article/result') > -1;

        this.subs.sink = this.router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                if (event.url.indexOf('/article/result') === -1) {
                    this.appStorage.clearBasketItemSource();
                }
            }
        });

        this.getArticles();

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'LOADED':
                        if (this.queryParams.articlesQuantity && this.data.length === 1) {
                            const finalQuantity = this.getDeeplinkQuantity(this.queryParams.articlesQuantity, data.article.salesQuantity);
                            if ((this.isCz || this.isAffiliateApplyFgasAndDeposit) && this.data[0].allowedAddToShoppingCart === false) {
                                return;
                            }
                            data.addToCart(finalQuantity);
                        } else {
                            this.articleBasketService.isAddedInCart(data);
                        }
                        break;
                    case 'ADD':
                        if (data.uuid) {
                            SpinnerService.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0 }
                            );
                        }
                        this.articleBasketService.addItemToCart(data);              
                        break;
                    case 'CUSTOM_PRICE_CHANGE':
                        this.shoppingBasketService.updateOtherProcess(data.basket);
                        break;
                }
            });
        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL)
            .subscribe((data: any) => {
                this.articlesAnalyticService.sendArticleListEventData(data);
                this.gaService.viewProductDetails(data.article, SUB_LIST_NAME_TYPE.PARTS_LIST_ITEMS);
            });

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe((code: string) => {
                this.router.navigate(['article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
            });

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .subscribe((vehicleId: string) => {
                if (vehicleId) {
                    this.router.navigate(['vehicle', vehicleId]);
                }
            });

    }

    ngOnDestroy() {
        this.paramsSubscription.unsubscribe();
        this.subs.unsubscribe();
    }

    getArticles(deliveryChanged = false) {
        this.paramsSubscription = this.route.queryParams.pipe(
            tap((queryParams) => {
                const type = queryParams.type;
                this.groupByRelevance = type === SEARCH_MODE.FREE_TEXT || type === SEARCH_MODE.ARTICLE_NUMBER;
                this.keywords = queryParams.keywords;
                this.filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
                this.containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
                this.articleListType = SEARCH_TYPE[queryParams.type] || ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;

                if (this.useMerkmaleFilter) {
                    if(this.isArticleResultPage) {
                        return;
                    }
                }

                this.filterDataOnBadge = new MultiLevelSelectedFilterModel();
            }),
            switchMap(queryParams => this.searchArticle(queryParams, deliveryChanged)),
            map(filterAndData => this.extractData(filterAndData, deliveryChanged)),
            tap((res) => {
                this.sendArticleSearchEvent(res);
                SpinnerService.stop(this.filterSpinner);
                SpinnerService.stop(this.containerSpinner);
            })
        ).subscribe(() => {
            this.updateAdsTarget();
        }, error => {
            this.data = [];
            SpinnerService.stop(this.filterSpinner);
            SpinnerService.stop(this.containerSpinner);
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

    get hasChanged() {
        if (this.useMerkmaleFilter) {
            const hasMultipleLevelGaids = get(this.categoryLevel, 'multipleLevelGaids.length', 0) > 0;
            const hasBrandFilter = get(this.categoryLevel, 'suppliers.length', 0) > 0;
            return hasMultipleLevelGaids || hasBrandFilter;
        } else {
            return !isEqual(this.additionalRequest, this.originalAdditionalRequest);
        }
    }

    toggleNetPriceSetting() {
        this.userService.toggleNetPriceView();
    }

    sendAdsEvent() {
        this.articlesAnalyticService.sendAdsEventData();
    }

    selectRootCategory({ multipleLevelGaids, gaId, nodeId }) {
        const filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
        const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        this.freetextArticleService.getSubCategory(gaId, this.categoryLevel, true).pipe(
            finalize(() => {
                SpinnerService.stop(filterSpinner);
                SpinnerService.stop(containerSpinner);
            }),
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
        const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        const filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
        this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(filterSpinner);
                    SpinnerService.stop(containerSpinner);
                })
            ).subscribe(result => {
                const articles = get(result, 'articles.content');
                this.numberOfSearchedArticles = get(result, 'articles.totalElements');
                this.hasMoreData = get(result, 'articles.numberOfElements') > 0 && get(result, 'articles.totalPages') > 1;
                this.data = (articles || []).map((article: any) => this.getArticle(article));
                this.moreData = [];
                this.filterDataOnBadge = new MultiLevelSelectedFilterModel(data.filterBadge);
                this.recordFeedbackArticle([...this.data || [], ...this.moreData || []]);
                this.incomingContextKey = get(result, 'context_key');
            });
    }

    resetFilter() {
        this.incomingContextKey = '';
        this.categoryLevel.contextKey = this.originalContextKey;

        if (this.useMerkmaleFilter) {
            this.categoryLevel.suppliers = [];
            this.selectCriteriaValue({ multipleLevelGaids: [], filterBadge: new MultiLevelSelectedFilterModel() });
            this.resetAllFilters = true;
            this.merkmaleBrandFilterData = cloneDeep(this.merkmaleBrandFilterData);
        } else {
            const filterSpinner = SpinnerService.start('connect-article-result-container .sag-article-filter');
            const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');

            this.categoryLevel = new SingleLevelCategory(this.typeMode);
            this.categoryLevel.contextKey = this.originalContextKey;

            if (this.typeMode === SEARCH_MODE.BAR_CODE) {
                if (this.queryParams.code) {
                    this.categoryLevel.setKeyword(this.queryParams.code);
                }
            } else {
                this.categoryLevel.setFilterType(this.queryParams);
                this.categoryLevel.suppliers = this.queryParams.brand && [this.queryParams.brand] || [];
            }

            this.freetextArticleService.getSingleLevelCategory(this.categoryLevel).pipe(
                finalize(() => {
                    SpinnerService.stop(containerSpinner);
                    SpinnerService.stop(filterSpinner);
                })
            ).subscribe((filter: any) => {
                const articles = get(filter, 'articles.content');
                this.data = (articles || []).map((article: any) => this.getArticle(article));
                this.moreData = [];
                this.hasMoreData = get(filter, 'articles.numberOfElements') > 0 && !get(filter, 'articles.last');
                this.numberOfSearchedArticles = get(filter, 'articles.totalElements') || 0;
                this.nonMerkmaleFilterData = get(filter, 'filters');
                this.sortAndSetNonMerkmaleFilterData();
                this.recordFeedbackArticle([...this.data || [], ...this.moreData || []]);
            });
        }
    }

    deselectMerkmale(val) {
        this.uncheckMerkmale = val;
    }

    goToPreviousRoute() {

    }

    showMoreArticles(callback) {
        if(this.additionalRequest) {
            const currentOffSet = this.categoryLevel.offset;
            if(this.typeMode === this.additionalRequest.filterMode) {
                this.categoryLevel = cloneDeep(this.additionalRequest);
            }
            
            this.categoryLevel.offset = currentOffSet;
        }

        this.categoryLevel.offset += 1;

        const contextKey = this.hasChanged ? this.incomingContextKey : this.originalContextKey;
        this.handleSearch(this.categoryLevel.offset, contextKey, false, this.categoryLevel)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(this.filterSpinner);
                    SpinnerService.stop(this.containerSpinner);
                    if (callback) {
                        callback();
                    }
                })
            ).subscribe(result => {
                const articles = get(result, 'articles.content');
                this.hasMoreData = get(result, 'articles.numberOfElements') > 0 && !get(result, 'articles.last');
                this.moreData = articles.map((article: any) => this.getArticle(article));
                this.recordFeedbackArticle([...this.moreData || []], false);
            });
    }

    handleAdditionalFilter(additionalFields: any) {
        const filterSpinner = SpinnerService.start('connect-article-result-container .sag-article-filter');
        const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        this.setAdditionalFields(additionalFields);
        this.setSuppliersAndGaids(additionalFields);
        this.additionalRequest.offset = 0;
        this.additionalRequest.contextKey = this.originalContextKey;
        this.freetextArticleService.getSingleLevelCategory(this.additionalRequest, this.hasChanged).pipe(
            finalize(() => {
                SpinnerService.stop(filterSpinner);
                SpinnerService.stop(containerSpinner);
            })
        ).subscribe((filter: any) => {
            const articles = get(filter, 'articles.content');
            this.data = (articles || []).map((article: any) => this.getArticle(article));
            this.moreData = [];
            this.hasMoreData = get(filter, 'articles.numberOfElements') > 0 && !get(filter, 'articles.last');
            this.categoryLevel.offset = 0;
            this.numberOfSearchedArticles = filter.articles.totalElements;
            this.recordFeedbackArticle([...this.data || [], ...this.moreData || []]);
            this.incomingContextKey = get(filter, 'context_key');
        });
    }

    goToBasket() {
        this.router.navigate([SHOPPING_BASKET_PAGE]);
    }

    private setSuppliersAndGaids(additionalFields: any) {
        if (additionalFields.type === 'suppliers') {
            this.additionalRequest.suppliers = additionalFields.isChecked ?
                [...this.additionalRequest.suppliers, additionalFields.filterId] :
                this.additionalRequest.suppliers.filter(supplier => supplier !== additionalFields.filterId);
        }

        if (additionalFields.type === 'gen_arts') {
            this.additionalRequest.gaids = additionalFields.isChecked ?
                [...this.additionalRequest.suppliers, additionalFields.filterId] :
                this.additionalRequest.suppliers.filter(supplier => supplier !== additionalFields.filterId);
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

    private searchArticle(queryParams: any, deliveryChanged: boolean): Observable<any> {
        if(queryParams.type !== this.typeMode || queryParams.keywords !== this.keywords) {
            this.multipleLevelGaids = null;
            this.filterDataOnBadge = new MultiLevelSelectedFilterModel();
        }

        let request = null;

        if(deliveryChanged && this.additionalRequest && this.additionalRequest.filterMode === this.typeMode) {
            request = cloneDeep(this.additionalRequest);
        }

        this.typeMode = queryParams.type;
        this.queryParams = Object.assign({}, queryParams);
        let articleNr = queryParams.articleNr;
        if (articleNr) {
            articleNr = articleNr.trim().replace(/\s+/g, '');;
            this.queryParams.articleNr = articleNr;
        }
        this.originalContextKey = this.queryParams.contextKey || '';

        return this.handleSearch(0, deliveryChanged ? '' : this.originalContextKey, deliveryChanged, request);
    }

    private updateAdsTarget() {
        switch (this.typeMode) {
            case SEARCH_MODE.BATTERY_SEARCH:
                this.adsTargetName = ADS_TARGET_NAME.BATTERIES;
                break;
            case SEARCH_MODE.OIL_SEARCH:
                this.adsTargetName = ADS_TARGET_NAME.OIL;
                break;
            case SEARCH_MODE.BULB_SEARCH:
                this.adsTargetName = ADS_TARGET_NAME.BULBS;
                break;
            case SEARCH_MODE.TYRES_SEARCH:
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
                this.adsTargetName = ADS_TARGET_NAME.TYRE;
                break;
            default:
                this.adsTargetName = ADS_TARGET_NAME.LIST_PARTS;
                break;
        }
    }

    private extractData(filterAndData: any, deliveryChanged = false) {
        this.originalContextKey = get(filterAndData, 'context_key', '');
        this.numberOfSearchedArticles = get(filterAndData, 'articles.totalElements');

        const articles = get(filterAndData, 'articles.content');
        if (this.typeMode === SEARCH_MODE.BATTERY_SEARCH) {
            this.batteryAnalyticService.sendEventData(this.queryParams, articles);
        }
        this.data = (articles || []).map((article: any) => this.getArticle(article));
        this.moreData = [];
        this.hasMoreData = get(filterAndData, 'articles.numberOfElements') > 0 && get(filterAndData, 'articles.totalPages') > 1;
        if (this.useMerkmaleFilter) {
            if(this.isArticleResultPage) {
                if(!this.multipleLevelGaids) {
                    this.filterData = get(filterAndData, 'filters.gen_arts');
                }
            } else {
                this.filterData = get(filterAndData, 'filters.gen_arts');
            }
            this.getMerkmaleSuppliers(filterAndData);
        } else {
            if(!deliveryChanged) {
                this.nonMerkmaleFilterData = get(filterAndData, 'filters');
            }

            const lib = this.nonMerkmaleBS.composeAttributes(this.queryParams);
            this.categoryLevel.contextKey = this.originalContextKey;
            this.additionalRequest = cloneDeep(this.categoryLevel) as SingleLevelCategory;
            this.originalAdditionalRequest = cloneDeep(this.categoryLevel) as SingleLevelCategory;
            this.attributes = lib;
            this.sortAndSetNonMerkmaleFilterData(deliveryChanged);
        }

        this.recordFeedbackArticle([...this.data || [], ...this.moreData || []]);
        return filterAndData;
    }

    public backToDestinationPage() {
        this.router.navigateByUrl(this.queryParams.returnUrl);
    }

    public checkout() {
        this.router.navigateByUrl('shopping-basket');
    }

    sortAndSetNonMerkmaleFilterData(deliveryChanged = false) {
        if (!this.nonMerkmaleFilterData) {
            return;
        }

        if(!deliveryChanged) {
            const filterData = cloneDeep(this.nonMerkmaleFilterData);
            let nonMerkmaleFilterData = [];
            // tslint:disable-next-line: forin
            for (const key in filterData) {
                if (filterData[key].length > 0) {
                    nonMerkmaleFilterData.push({
                        key,
                        value: filterData[key]
                    });
                }
            }

            this.nonMerkmaleFilterData = nonMerkmaleFilterData;
            this.removeUnusedNonMerkmaleFilterItem();
        }

        this.additionalRequest = cloneDeep(this.categoryLevel) as SingleLevelCategory;
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

    private getDeeplinkQuantity(amountNumber, salesQuantity): number {
        if (!amountNumber || !salesQuantity) {
            return 0;
        }
        return Math.round(amountNumber / salesQuantity) * salesQuantity;
    }

    private handleSearch(offset: number = 0, contextKey: string = '', deliveryChanged = false, request?) {
        const queryParams = this.queryParams;
        setTimeout(() => {
            this.filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
            this.containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        });
        this.useMerkmaleFilter = false;

        if (this.typeMode === SEARCH_MODE.FREE_TEXT.toString() || this.typeMode === SEARCH_MODE.ID_SAGSYS.toString()) {
            this.useMerkmaleFilter = true;

            if(this.categoryLevel && this.categoryLevel.keyword !== queryParams.articleId) {
                this.multipleLevelGaids = null;
                this.filterDataOnBadge = new MultiLevelSelectedFilterModel();
            }

            if(request) {
                this.categoryLevel = request;
            } else {
                this.categoryLevel = new MultiLevelCategory(this.typeMode);
            }

            this.categoryLevel.setKeyword(queryParams.articleId);
            this.categoryLevel.offset = offset;
            this.categoryLevel.contextKey = this.typeMode === SEARCH_MODE.FREE_TEXT ? contextKey : '';
            return this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, false, this.multipleLevelGaids);
        } else if (this.typeMode === SEARCH_MODE.BAR_CODE) {
            if (queryParams.code) {
                if(request) {
                    this.categoryLevel = request;
                } else {
                    this.categoryLevel = new SingleLevelCategory(this.typeMode);
                }

                this.categoryLevel.setKeyword(queryParams.code);
                this.categoryLevel.offset = offset;
                this.categoryLevel.contextKey = contextKey;
                return this.freetextArticleService.getSingleLevelCategory(this.categoryLevel);
            }
        } else {
            if (queryParams.articleNr) {
                this.useMerkmaleFilter = true;

                if(this.categoryLevel && this.categoryLevel.keyword !== queryParams.articleNr) {
                    this.multipleLevelGaids = null;
                    this.filterDataOnBadge = new MultiLevelSelectedFilterModel();
                }

                if(request) {
                    this.categoryLevel = request;
                } else {
                    this.categoryLevel = new MultiLevelCategory(SEARCH_MODE.ARTICLE_NUMBER);
                }

                this.categoryLevel.setKeyword(queryParams.articleNr);
                this.categoryLevel.offset = offset;
                this.categoryLevel.contextKey = contextKey;
                return this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, false, this.multipleLevelGaids)
                    .pipe(tap((res: any) => {
                        if (this.isCz && this.queryParams.from_source && res && res.articles) {
                            this.sendEvent(res);
                        }
                    }));
            } else {
                if(request) {
                    this.categoryLevel = request;
                } else {
                    this.categoryLevel = new SingleLevelCategory(this.typeMode);
                    this.categoryLevel.setFilterType(queryParams);
                }

                this.categoryLevel.offset = offset;
                this.categoryLevel.contextKey = contextKey;
                this.categoryLevel.suppliers = this.queryParams.brand && [this.queryParams.brand] || [];

                return this.freetextArticleService.getSingleLevelCategory(deliveryChanged ? this.additionalRequest: this.categoryLevel, deliveryChanged);
            }
        }
    }

    private recordFeedbackArticle(articles: any[], reset = true) {
        if (reset) {
            this.fbRecordingService.clearArticleResults();
        }
        (articles || []).forEach(article => {
            this.fbRecordingService.recordArticleResult(article);
        });
    }

    private sendEvent(data) {
        setTimeout(() => {
            if (this.eventSent) {
                return;
            }
            this.eventSent = true;
            const eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
            const request = {
                artSearchType: ARTICLE_NR_SEARCH_TYPE,
                artSearchTermEntered: data.search,
                artNumberOfResult: get(data, 'articles.totalElements', 0).toPrecision(),
                fromSource: this.queryParams.from_source
            };

            const eventRequest = this.analyticService.createFullTextSearchArticleEventData(request);
            this.analyticService
                .postEventFulltextSearch(eventRequest, eventType)
                .pipe(first())
                .toPromise();
        });
    }

    private sendArticleSearchEvent(res: any) {
        const params = this.queryParams || {};
        switch (params.ref) {
            case ARTICLE_SEARCH_MODE.ARTICLE_NUMBER:
            case ARTICLE_SEARCH_MODE.ARTICLE_DESC:
                this.articlesAnalyticService.sendArticleSearchEventData({
                    searchType: this.queryParams.ref === ARTICLE_SEARCH_MODE.ARTICLE_NUMBER ? LIB_VEHICLE_ARTICLE_NUMBER_SEARCH : LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH,
                    search: params.articleId || params.articleNr,
                    response: res && res.articles
                });
                break;
            case ARTICLE_SEARCH_MODE.SHOPPING_LIST:
                this.articlesAnalyticService.sendArticleSearchEventData({
                    searchType: LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH,
                    search: params.articleId || params.articleNr,
                    response: res && res.articles
                }, ARTICLE_SEARCH_MODE.SHOPPING_LIST);
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_ID:
            case ARTICLE_SEARCH_MODE.FREE_TEXT:
                const item = params.ref === ARTICLE_SEARCH_MODE.ARTICLE_ID ? get(res, 'articles.content[0]') : null;

                const eventData = {
                    ftsFilterSelected: HeaderSearchTypeEnum.ARTICLES.toUpperCase(),
                    ftsSearchTermEntered: params.articleId,
                    ftsNumberOfHits: get(res, 'articles.totalElements'),
                    ftsNameClicked: item && item.freetextDisplayDesc,
                    ftsArticleIdClicked: item && item.id_pim
                };
                const extras = {
                    isSelectedItemEvent: true,
                    isArticle: true,
                    isShowMore: params.ref === ARTICLE_SEARCH_MODE.FREE_TEXT
                };
                this.articlesAnalyticService.sendFulltextSearchEventData(eventData, extras);
                break;
        }
        if (!params.ref && params.articleNr && params.articlesQuantity) {
            this.articlesAnalyticService.sendArticleSearchEventData({
                searchType: LIB_VEHICLE_ARTICLE_NUMBER_SEARCH,
                search: params.articleNr,
                response: res && res.articles
            }, ARTICLE_SEARCH_MODE.DEEP_LINK);
        }
    }

    sendArticleResultData(articles) {
        this.articlesAnalyticService.sendArticleResults(articles);
    }

    onSelectFavoriteItem(item: FavoriteItem) {
        if (item.vehicleId) {
            this.favoriteCommonService.navigateToFavoriteVehicle(item);
        } else if (item.treeId && item.leafId) {
            this.appStorage.selectedFavoriteLeaf = item;
            this.router.navigate(['wsp'], {
                queryParams: {
                    treeId: item.treeId,
                    nodeId: item.leafId,
                    gaId: item.gaId
                }
            });
        } else {
            this.favoriteCommonService.navigateToFavoriteArticle(item);
        }
    }

    private getArticle(art) {
        const article = new ArticleModel(art);
        if (this.typeMode === SEARCH_MODE.TYRES_SEARCH) {
            article.priceRequested = true;
            article.availRequested = true;
        }
        article.stockRequested = true;
        return article;
    }

    onMerkmaleBrandFilterChange(brandFilters: string[]) {
        this.categoryLevel.suppliers = brandFilters;
        this.categoryLevel.offset = 0;
        this.setKeepDirectAndOriginalMatchParam();
        const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        const filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
        this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(filterSpinner);
                    SpinnerService.stop(containerSpinner);
                })
            ).subscribe(result => {
                const articles = get(result, 'articles.content');
                this.numberOfSearchedArticles = get(result, 'articles.totalElements');
                this.hasMoreData = get(result, 'articles.numberOfElements') > 0 && get(result, 'articles.totalPages') > 1;
                this.data = (articles || []).map((article: any) => this.getArticle(article));
                this.moreData = [];
                this.recordFeedbackArticle([...this.data || [], ...this.moreData || []]);
                this.incomingContextKey = get(result, 'context_key');
            });
    }

    private getMerkmaleSuppliers(filterAndData) {
        this.merkmaleBrandFilterData = get(filterAndData, 'filters.suppliers');
    }

    private setKeepDirectAndOriginalMatchParam() {
        this.categoryLevel.setKeepDirectAndOriginalMatch(true);
    }

    onSendArticlesGaData(event) {
        const articles = event;
        this.articlesAnalyticService.sendArticleListGaData(articles);
    }
}
