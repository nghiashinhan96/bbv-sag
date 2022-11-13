import { OnInit, OnDestroy, Component, Input, Output, EventEmitter, TemplateRef, ViewChild} from '@angular/core';
import { map, finalize, tap, catchError, first } from 'rxjs/operators';
import { get, uniq, cloneDeep } from 'lodash';
import { SubSink } from 'subsink';
import { TranslateService } from '@ngx-translate/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';
import { FreetextArticleService } from 'src/app/article-not-context-result-list/service/freetext-article.service';

import { LevelCategory } from 'src/app/article-not-context-result-list/models/level-category.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Observable, of, Subject } from 'rxjs';
import { MultiLevelCategory } from 'src/app/article-not-context-result-list/models/multi-level-category.model';
import { SearchHistory } from '../../models/search-history.model';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { BroadcastService, SagMessageData, AffiliateUtil } from 'sag-common';
import { MultiLevelSelectedFilter, MultiLevelSelectedFilterModel, SEARCH_MODE, ArticleSort } from 'sag-article-list';
import { ArticleBroadcastKey, ArticleBasketModel, ArticleModel, CZ_AVAIL_STATE } from 'sag-article-detail';
import { MerkmaleGaids } from 'sag-article-list/models/merkmale-gaid.model';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { environment } from 'src/environments/environment';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { ARTICLE_SEARCH_TYPE } from 'src/app/analytic-logging/enums/article-search-type.enums';
import { ARTICLE_SEARCH_MODE } from 'sag-article-search';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { GA_SEARCH_CATEGORIES } from 'src/app/analytic-logging/enums/ga-search-category.enums';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { CurrencyUtil } from 'sag-currency';
@Component({
    selector: 'connect-article-list-item',
    templateUrl: 'article-list-item.component.html'
})
export class ArticleListItemComponent implements OnInit, OnDestroy {
    @Input() isSubBasket = false;
    @Input() searchData: SearchHistory;
    @Input() filter: any;
    @Input() index: number;
    @Input() sortArticle: Subject<ArticleSort>;

    @Output() dataChange = new EventEmitter();

    filterDataOnBadge: MultiLevelSelectedFilter;
    subSelectedCategory: any;
    uncheckMerkmale: any;
    moreData: any;
    message: SagMessageData;

    hasMoreData: boolean;
    resetAllFilters: boolean;
    loading = false;
    allMoreArticles = [];

    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);

    private categoryLevel: LevelCategory;
    private originalContextKey = '';
    private incomingContextKey = '';

    private spinnerSelector = 'connect-article-list-search .article-list-search';
    private subs = new SubSink();
    merkmaleBrandFilterData = [];
    amountFromFileImported: number;

    private pops: PopoverDirective[] = [];

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    constructor(
        private translateService: TranslateService,
        public appStorage: AppStorageService,
        public userService: UserService,
        private freetextArticleService: FreetextArticleService,
        private articleListBroadcastService: BroadcastService,
        private articleBasketService: ArticleShoppingBasketService,
        private shoppingBasketService: ShoppingBasketService,
        private fbRecordingService: FeedbackRecordingService,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService,
        private analyticService: AnalyticLoggingService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .subscribe((data: ArticleBasketModel) => {
                if (!data.article) {
                    return;
                }
                this.handleShoppingBasketEvent(data);
            });

        if (this.searchData.emitSearch) {
            this.loading = true;
            if (this.searchData.isShoppingList) {
                this.onSearch(this.searchData.searchTerm, this.searchData.isShoppingList);
            } else {
                this.onSearch(this.searchData.searchTerm);
            }
        }

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.MINI_BASKET_CONDITION_EVENT)
            .subscribe((data: any) => {
                if(this.searchData.articles && this.searchData.articles.length > 0) {
                    this.searchData.articles = null;
                    this.onSearch(this.searchData.searchTerm);
                }
            });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
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
        const hasMultipleLevelGaids = get(this.categoryLevel, 'multipleLevelGaids.length', 0) > 0;
        const hasBrandFilter = get(this.categoryLevel, 'suppliers.length', 0) > 0;
        return hasMultipleLevelGaids || hasBrandFilter;
    }

    handleInputTab(data) {

    }

    onSearch(value, isShoppingList?) {
        const searchTerm = (value || '').trim();
        if (searchTerm) {
            this.searchData.searchTerm = searchTerm;
            this.searchData.message = null;
            const spinner = SpinnerService.start(this.spinnerSelector);
            this.searchArticle(searchTerm, isShoppingList).pipe(
                catchError(err => of({})),
                map(res => {
                    SpinnerService.stop(spinner);
                    this.extractData(res);
                })
            ).subscribe();
        }
    }

    selectRootCategory({ multipleLevelGaids, gaId, nodeId }) {
        this.freetextArticleService.getSubCategory(gaId, this.categoryLevel, true).pipe(
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
        const spinner = SpinnerService.start(this.spinnerSelector);
        this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => SpinnerService.stop(spinner))
            ).subscribe(result => {
                const articles = get(result, 'articles.content') || [];
                this.hasMoreData = get(result, 'articles.totalPages') > 1;
                this.searchData.articles = articles.map((article: any) => this.getArticle(article));
                this.filterDataOnBadge = new MultiLevelSelectedFilterModel(data.filterBadge);
                this.recordFeedbackArticle([...this.searchData.articles || [], ...this.moreData || []]);
                this.incomingContextKey = get(result, 'context_key');
            });
    }

    deselectMerkmale(val) {
        this.uncheckMerkmale = val;
    }

    showMoreArticles(callback) {
        const spinner = SpinnerService.start(this.spinnerSelector);
        this.categoryLevel.offset += 1;
        this.categoryLevel.contextKey = this.hasChanged ? this.incomingContextKey : this.originalContextKey;
        this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(spinner);
                    if (callback) {
                        callback();
                    }
                })
            ).subscribe(result => {
                const articles = get(result, 'articles.content') || [];
                this.hasMoreData = !get(result, 'articles.last');
                this.moreData = articles.map((article: any) => this.getArticle(article));
                this.allMoreArticles = [...this.allMoreArticles, ...this.moreData];
                this.recordFeedbackArticle([...this.moreData || []], false);
            });
    }

    resetFilter() {
        this.categoryLevel.suppliers = [];
        this.incomingContextKey = '';
        this.categoryLevel.contextKey = this.originalContextKey;
        this.selectCriteriaValue({ multipleLevelGaids: [], filterBadge: new MultiLevelSelectedFilterModel() });
        this.resetAllFilters = true;
        this.merkmaleBrandFilterData = cloneDeep(this.merkmaleBrandFilterData);
    }

    private async handleShoppingBasketEvent(data: ArticleBasketModel) {
        const allArticles = [...this.searchData.articles || [], ...this.allMoreArticles || []];
        const article = allArticles.find(art => art.artid === data.article.artid);
        const replacement = allArticles.find(art => art.artid === data.article.replacementForArtId);
        if (!article && !replacement) {

            if (data.isAccessoryItem || data.isPartsItem) {
                this.handleShoppingBasketEventForNonExistArticle(data);
            }
            return;
        }

        switch (data.action) {
            case 'LOADED':
                const loadCallback = data.callback;
                data.callback = (res: any) => {
                    if (!this.searchData.isAddedToCart) {
                        this.searchData.cartKeys = uniq([...this.searchData.cartKeys || [], res.cartKey]);
                        this.emitDataChange();
                    }
                    if (loadCallback) {
                        loadCallback({
                            ...res,
                            enableShoppingCart: false,
                            removable: true
                        });
                    }
                };
                const isAddedToCart = await this.articleBasketService.isItemAddedToCart(data);
                if (this.searchData.articles && this.searchData.articles.length === 1 && !replacement) {
                    const isAffiliateApplyFgasAndDeposit = this.isCz || this.isAffiliateApplyFgasAndDeposit;
                    if (!isAffiliateApplyFgasAndDeposit || (isAffiliateApplyFgasAndDeposit && this.searchData.articles[0].allowedAddToShoppingCart !== false)) {
                        this.searchData.isAddedToCart = true;
                        if (!isAddedToCart && data.addToCart) {
                            data.addToCart();
                        }
                    }
                }
                break;
            case 'ADD':
                if (data.uuid) {
                    SpinnerService.start(`#part-detail-${data.uuid}`,
                        { containerMinHeight: 0 }
                    );
                }
                const callback = data.callback;
                data.callback = (res: any) => {
                    if (article) {
                        this.hasMoreData = false;
                        this.moreData = [];
                        this.allMoreArticles = [];
                        res.article.replacementForArtId = '';
                        this.searchData.articles = [this.getArticle(res.article)];
                        this.searchData.isAddedToCart = true;
                        this.searchData.cartKeys = uniq([...this.searchData.cartKeys || [], res.cartKey]);
                        this.emitDataChange();
                    }
                    if (callback) {
                        callback({
                            ...res,
                            enableShoppingCart: false,
                            removable: true
                        });
                    }
                };
                this.articleBasketService.addItemToCart(data);
                if (article) {
                    this.message = null;
                    this.searchData.message = null;
                }
                break;
            case 'REMOVE':
                if (data.article.replacementForArtId) {
                    return;
                }
                if (data.uuid) {
                    SpinnerService.start(`#part-detail-${data.uuid}`,
                        { containerMinHeight: 0 }
                    );
                }
                this.shoppingBasketService.removeBasketItem({ cartKeys: [data.cartKey] })
                    .pipe(
                        finalize(() => SpinnerService.stop(`#part-detail-${data.uuid}`)),
                    )
                    .subscribe();
                break;
            case 'CUSTOM_PRICE_CHANGE':
                this.shoppingBasketService.updateOtherProcess(data.basket);
                break;
        }
    }

    private async handleShoppingBasketEventForNonExistArticle(data: ArticleBasketModel) {
        switch (data.action) {
            case 'LOADED':
                await this.articleBasketService.isItemAddedToCart(data);
                break;
            case 'ADD':
                this.articleBasketService.addItemToCart(data);
                break;
            case 'REMOVE':
                this.shoppingBasketService.removeBasketItem({ cartKeys: [data.cartKey] }).subscribe();
                break;
            case 'CUSTOM_PRICE_CHANGE':
                this.shoppingBasketService.updateOtherProcess(data.basket);
                break;
        }
    }

    private searchArticle(articleInfo: any, isShoppingList?: boolean): Observable<any> {
        this.categoryLevel = new MultiLevelCategory(this.searchData.searchType || SEARCH_MODE.FREE_TEXT);
        this.categoryLevel.setKeyword(articleInfo);
        if (isShoppingList) {
            this.categoryLevel.setIsShoppingList(isShoppingList);
        }
        return this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel);
    }

    private async extractData(filterAndData: any) {
        this.originalContextKey = get(filterAndData, 'context_key', '');
        const articles = get(filterAndData, 'articles.content') || [];
        this.searchData.articles = articles.map((article: any) => this.getArticle(article)).sort((a, b) => a.relevanceGroupOrder - b.relevanceGroupOrder);
        this.moreData = [];
        this.allMoreArticles = [];
        this.hasMoreData = get(filterAndData, 'articles.totalPages') > 1;

        this.searchData.filter = get(filterAndData, 'filters.gen_arts');
        this.getMerkmaleSuppliers(filterAndData);
        this.searchData.isHadResult = articles && articles.length > 0;

        this.sendEvent(filterAndData);

        if (this.searchData.isImported) {
            const params = {
                amount: this.searchData.amount || this.translateService.instant('ARTICLE_SEARCH_LIST.NO_INFO')
            };
            if (!articles || articles.length === 0) {
                this.message = { type: 'ERROR', message: 'ARTICLE_SEARCH_LIST.IMPORTED_NO_RESULTS', params };
                this.searchData.message = this.message;
            }
            if (articles && articles.length > 0) {
                this.message = { type: 'INFO', message: 'ARTICLE_SEARCH_LIST.IMPORTED_MULTIPLE_RESULT', params };
                this.searchData.message = this.message;
            }
            if (articles && articles.length === 1) {
                const isAddedToCart = await this.articleBasketService.isItemAddedToCart({
                    action: 'LOADED',
                    pimId: articles[0].id_pim,
                    vehicle: articles[0].vehicle
                });
                this.searchData.isAddedToCart = isAddedToCart;
                if (isAddedToCart) {
                    this.message = { type: 'INFO', message: 'ARTICLE_SEARCH_LIST.IMPORTED_ITEM_ALREADY_IN_CART', params };
                    this.searchData.message = this.message;
                } else {
                    this.message = null;
                    this.searchData.message = null;
                    const qtyMultiple = this.searchData.articles[0].qtyMultiple || 1;
                    let amount = this.searchData.amount;
                    if (amount > 0) {
                        if (amount % qtyMultiple !== 0) {
                            amount = Math.ceil(amount / qtyMultiple) * qtyMultiple;
                        }
                        this.searchData.articles[0].amountNumber = CurrencyUtil.getMaxQuantityValid(amount, qtyMultiple);
                    }
                }

            }
            if (articles && articles.length > 1 && this.searchData.isImportedFromFile) {
                this.amountFromFileImported = parseInt(this.searchData.amount.toString());
            }
        }

        this.emitDataChange();
        this.loading = false;
        this.recordFeedbackArticle([...this.searchData.articles || [], ...this.moreData || []]);
    }

    private emitDataChange() {
        this.dataChange.emit({ index: this.index, searchData: this.searchData });
    }

    private recordFeedbackArticle(articles: any[], reset = true) {
        if (reset) {
            this.fbRecordingService.clearArticleResults();
        }
        (articles || []).forEach(article => {
            this.fbRecordingService.recordArticleResult(article);
        });
    }

    sendArticleResultData(articles) {
        this.articlesAnalyticService.sendArticleResults(articles);
    }

    private sendEvent(data) {
        setTimeout(() => {
            const eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
            let request: any = {
                artSearchType: ARTICLE_SEARCH_TYPE.ARTICLE_DESC,
                artSearchTermEntered: this.searchData.searchTerm,
                artNumberOfResult: get(data, 'articles.totalElements', 0).toPrecision()
            };

            const eventRequest = this.analyticService.createFullTextSearchArticleEventData(request, {articleSearchMode: ARTICLE_SEARCH_MODE.SHOPPING_LIST, isSubBasket: this.isSubBasket});
            if (eventRequest) {
                // GA4 Search event
                const sourceId = eventRequest.basket_item_source_id;
                const sourceDesc = eventRequest.basket_item_source_desc;
                const searchTerm = eventRequest.art_search_term_entered || '';
                this.gaService.search('', searchTerm, sourceId, sourceDesc);
    
                // Json event
                this.analyticService.postEventFulltextSearch(eventRequest, eventType)
                    .pipe(first())
                    .toPromise();
            }
        });
    }

    private getArticle(art) {
        const article = new ArticleModel(art);
        article.stockRequested = true;
        return article;
    }

    onMerkmaleBrandFilterChange(brandFilters: string[]) {
        this.categoryLevel.suppliers = brandFilters;
        this.categoryLevel.offset = 0;
        const containerSpinner = SpinnerService.start('connect-article-result-container .article-list-section');
        const filterSpinner = SpinnerService.start('connect-article-result-container .filtering');
        this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel, this.hasChanged)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(filterSpinner);
                    SpinnerService.stop(containerSpinner);
                })
            ).subscribe(result => {
                const articles = get(result, 'articles.content') || [];
                this.hasMoreData = get(result, 'articles.totalPages') > 1;
                this.searchData.articles = articles.map((article: any) => this.getArticle(article));
                this.recordFeedbackArticle([...this.searchData.articles || [], ...this.moreData || []]);
                this.incomingContextKey = get(result, 'context_key');
            });
    }

    private getMerkmaleSuppliers(filterAndData) {
        this.merkmaleBrandFilterData = get(filterAndData, 'filters.suppliers');
    }

    popoversChanged(pops: PopoverDirective[]) {
        (pops || []).forEach(pop => {
            if (!this.pops.some(p => p === pop)) {
                this.subs.sink = pop.onShown.subscribe(() => {
                    this.pops
                        .filter(p => p !== pop)
                        .forEach(p => {
                            if (p && p.isOpen) {
                                p.hide();
                            }
                        });
                });
                this.pops.push(pop);
            }
        })
    }

    onSendArticlesGaData(event) {
        const articles = event;
        this.articlesAnalyticService.sendArticleListGaData(articles);
    }
}
