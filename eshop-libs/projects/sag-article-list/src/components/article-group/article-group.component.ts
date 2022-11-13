import { Component, OnInit, Input, OnDestroy, ViewChild, ElementRef, EventEmitter, TemplateRef, Output, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { xor, includes, uniq, uniqBy, cloneDeep } from 'lodash';

import { ArticleGroupSortService } from '../../services/article-group-sort.service';
import { BRAND, NUMBER_ITEMS_REQUEST_AVAILABILITY } from '../../consts/article-list.const';
import { ArticleGroupModel } from '../../models/article-group.model';
import { SortedArticleGroup } from '../../models/sorted-article-group.model';
import { ArticlesService, ArticleModel, LibUserSetting, ArticleDetailConfigService, WspCategoryModel, CategoryModel, ArticleBroadcastKey, ArticleBasketModel } from 'sag-article-detail';
import { Observable, of, BehaviorSubject, Subscription } from 'rxjs';
import { AffiliateUtil, BrandPriorityAvailabilityUtil, BroadcastService, ProjectId } from 'sag-common';
import { BrandPriorityAvailService } from '../../services/brand-priority-avail.service';
import { SagArticleListBrandFilterPipe } from '../../pipes/brand-filter.pipe';
import { BrandFilterItem } from '../../models/brand-filter-item.model';
import { BrandFilterUtil } from '../../utils/brand-filter.util';
import { ArticleSort } from '../../models/article-sort.model';
import { ArticleSortUtil } from '../../utils/article-sort.util';
import { ARTICLE_LIST_TYPE } from '../../enums/article-list-type.enum';
import { CategoryTreeService } from '../../services/category-tree.service';
import { ArticleListStorageService } from '../../services/article-list-storage.service';
import { BarFilter } from "sag-article-detail";
import { catchError, map } from 'rxjs/operators';
import { SagShowHideNonAvailPipe } from '../../pipes/show-hide-non-available.pipe';
import { SagArticleListIntegrationService } from '../../services/article-list-integration.service';

@Component({
    selector: 'sag-article-list-group',
    templateUrl: './article-group.component.html',
    styleUrls: ['./article-group.component.scss'],
    providers: [SagArticleListBrandFilterPipe, SagShowHideNonAvailPipe]
})
export class SagArticleListGroupComponent implements OnInit, OnChanges, OnDestroy {
    @Input() articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;
    @Input() set group(val: ArticleGroupModel) {
        if (val) {
            this.groupName = val.key;
            this.initialData(val);
        }
    }

    @Input() vehicle: any;
    @Input() articleMode: boolean;
    @Input() userSetting: LibUserSetting;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;
    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() showLiquidationObserve: Observable<any>;
    @Input() indexGroup: number;
    @Input() totalGroups: number;
    @Input() totalGroupGaIDShowNoAvailMsg: number = 0;
    @Input() isGroupByGaId = false;
    @Input() sortArticle: BehaviorSubject<ArticleSort>;
    @Input() rootModalName: string = '';

    @Output() hasLiquidationEmiter = new EventEmitter();
    @Output() totalGroupGaIDShowNoAvailMsgChange = new EventEmitter();

    @Input() brandsData: BrandFilterItem[] = [];
    @Output() brandsItemsChange = new EventEmitter<BrandFilterItem>();

    @Output() barFilterItemsChange = new EventEmitter<BarFilter>();

    @Output() sendArticleResultData = new EventEmitter();
    @Output() sendArticlesGaData = new EventEmitter();
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Input() barFilterOption: BarFilter;

    @Input() set moreArticles(arts: ArticleModel[]) {
        if (!!arts && arts.length > 0) {
            this.config.spinner.start();
            this.requestErpInfoWsp(arts, (res) => {
                const articles = [];
                (res || []).forEach(art => {
                    articles.push(art);
                });
                this.topList = [...this.topList, ...articles];
                this.sendArticleResultData.emit(articles);
                this.sendVisibleArticlesToGa();
                this.config.spinner.stop();
            });
        }
    }

    @Output() popoversChanged = new EventEmitter();

    storedGroup: ArticleGroupModel;
    groupName: string;
    showNoAvailMsgForDefaultGroup = false;

    availabilities = [];
    private data: SortedArticleGroup;
    private showLiquidationObserveSubscription: Subscription;
    isFirstLoading = false;
    isBrand: boolean;
    topList: ArticleModel[] = []; // List default
    secondList: ArticleModel[] = []; // List more
    topListTemp: ArticleModel[] = []; // Temp list default for sorting cz
    secondListTemp: ArticleModel[] = []; // temp list more for sorting cz
    liquidationArticles: ArticleModel[] = []; // List liquidation
    hasMoreArticles: boolean;
    hasLiquidation: boolean;
    isLoadingMoreArticles: boolean;
    isShownMoreArticles: boolean;
    isLoadingLiquidations: boolean;
    isShownliquidation: boolean;
    autonetRemainSecondList: ArticleModel[] = [];
    wspRemainTopList: ArticleModel[] = [];
    readonly autonetMaxItem = 10;
    readonly wspMaxItem = 10;
    isLoadingMoreArticlesUsedForWsp: boolean;

    brandPriorityAvail = {
        p1: false,
        p2: false,
        p3: false
    };

    filterByCaId = null;
    defaultCaIdFilterVal = null;
    showFilterBar = false;
    filterSort = false;
    filterCriteria = [];

    projectId = ProjectId.CONNECT;
    totalGroupDefaultShowNoAvailMsg = 0;

    filterBrands = [];
    sort: ArticleSort;

    isCz: boolean;
    isSb: boolean;
    isCz9: boolean;

    subs = new Subscription();
    hideNonAvail = false;

    TYPE = ARTICLE_LIST_TYPE;

    defaultTotal = 0;
    showMoreTotal = 0;

    isAutonet = false;
    isArticlesLoaded = false;

    @ViewChild('liqSection', { static: false }) liqSection: ElementRef;
    constructor(
        private sorting: ArticleGroupSortService,
        private articlesService: ArticlesService,
        private translateService: TranslateService,
        public config: ArticleDetailConfigService,
        private brandPriorityAvailService: BrandPriorityAvailService,
        private brandFilterPipe: SagArticleListBrandFilterPipe,
        private sagShowHideNonAvailPipe: SagShowHideNonAvailPipe,
        private categoryTreeService: CategoryTreeService,
        private libStorage: ArticleListStorageService,
        private broadcastService: BroadcastService,
        private sagArticleListIntegrationService: SagArticleListIntegrationService
    ) {
        this.hideNonAvail = this.libStorage.hideNonAvailArticle;
        this.isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate) || AffiliateUtil.isAxCz(this.config.affiliate);
        this.isCz9 = AffiliateUtil.isAffiliateCZ9(this.config.affiliate);
        this.isSb = AffiliateUtil.isSb(this.config.affiliate);
        this.isAutonet = this.config.projectId === ProjectId.AUTONET;
    }

    ngOnInit() {
        this.sorting.disabledBrandPriorityAvailability = this.userSetting.disabledBrandPriorityAvailability;

        this.showLiquidationObserveSubscription = this.showLiquidationObserve.subscribe(val => {
            this.showLiquidation();
        });

        if (this.sortArticle) {
            this.subs.add(
                this.sortArticle.subscribe((sort: ArticleSort) => {
                    this.sort = sort;
                    this.sortArticles(this.sort);
                })
            );
        }

        this.projectId = this.config.projectId || ProjectId.CONNECT;
        if (this.projectId !== ProjectId.AUTONET) {
            this.brandPriorityAvail = BrandPriorityAvailabilityUtil.convertResponseData(this.userSetting.brandPriorityAvailFilter);
        }
        this.subs.add(
            this.libStorage.isHideNonAvailArticleChange().subscribe(data => this.hideNonAvail = data)
        );
        if (this.articleListType == ARTICLE_LIST_TYPE.WSP) {
            this.subs.add(
                this.broadcastService.on(ArticleBroadcastKey.MINI_BASKET_CONDITION_EVENT)
                    .subscribe(() => {
                        this.availabilities = [];
                        (this.topList || []).forEach(art => {
                            art.availRequested = false;
                        });
                        this.requestErpInfoWsp(this.topList, undefined);
                    })
            );
        }
    }

    ngOnChanges(change: SimpleChanges) {
        if (change.totalGroupGaIDShowNoAvailMsg && !change.totalGroupGaIDShowNoAvailMsg.firstChange) {
            this.totalGroupDefaultShowNoAvailMsg = change.totalGroupGaIDShowNoAvailMsg.currentValue;
            this.checkShowNoAvailMsgForDefaultGroup();
        }
        if (change.brandsData && !change.brandsData.firstChange) {
            this.handleFilter();
        }

        if (change.barFilterOption && !change.barFilterOption.firstChange) {
            this.handleFilter();
        }
    }

    ngOnDestroy(): void {
        if (this.showLiquidationObserveSubscription) {
            this.showLiquidationObserveSubscription.unsubscribe();
        }
        this.subs.unsubscribe();
        this.emitBrandsItems([]);
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

    async initialData(group: ArticleGroupModel) {
        if (group.pseudo) {
            this.storedGroup = group;
            this.data = {
                displayedArticles: group.values,
                nonDisplayedArticles: []
            };
            this.secondList = group.values;
            this.requestErpInfo();
            return;
        }

        if (this.articleListType == ARTICLE_LIST_TYPE.WSP) {
            let allArticles = group.values;
            this.storedGroup = group;
            this.updateFilterCaId(this.categoryTreeService.currentSelectedWspCategory);
            this.isBrand = true;
            this.hasLiquidationEmiter.emit(false);
            // sorting below is WspSortingService instead of ArticleGroupSortService
            const convertedMainList = await this.sorting.filterBrandAndSortByPriority(allArticles, group.cate);
            this.wspRemainTopList = convertedMainList.displayedArticles;
            const wspArticleList = this.wspRemainTopList.splice(0, this.wspMaxItem);

            this.topList = wspArticleList;
            this.topListTemp = [...this.topList];
            this.requestErpInfoWsp(wspArticleList, (articles) => {
                this.topList = this.topList.slice();
                this.sendArticleResultData.emit(articles);
                this.sendVisibleArticlesToGa();
                this.isFirstLoading = false;
            });
            return;
        }

        if (this.isChanged(group)) {
            this.storedGroup = group;
            // check isValueChanged
            if (!this.isAutonet) {
                this.isFirstLoading = true;
            }
            // SpinnerService.start('sag-lib-article-group .card-body');
            const convertedMainList = await this.sorting.filterBrandAndSortByPriority(group.values, group.cate);

            const hintenInTopList = this.filterListWithHinten(convertedMainList.displayedArticles).hintenList;
            let hintenList: any;
            let convertedHintenList: SortedArticleGroup;
            if (hintenInTopList.length === 0) {
                hintenList = this.filterListWithHinten(convertedMainList.nonDisplayedArticles);
                convertedHintenList = await this.sorting.filterBrandAndSortByPriority(hintenList.hintenList, group.cate);
            }

            this.isBrand = convertedMainList.type === BRAND || (!!convertedHintenList && convertedHintenList.type === BRAND);

            this.data = hintenInTopList.length > 0 ? convertedMainList :
                {
                    displayedArticles: [...convertedMainList.displayedArticles, ...convertedHintenList.displayedArticles],
                    nonDisplayedArticles: [...convertedHintenList.nonDisplayedArticles, ...hintenList.otherList]
                } as SortedArticleGroup;


            this.data.displayedArticles.forEach(art => {
                if (!!art.availabilities) {
                    this.availabilities.push({
                        key: art.pimId,
                        value: art.availabilities
                    });
                }
            });

            this.data.nonDisplayedArticles.forEach(art => {
                if (!!art.availabilities) {
                    this.availabilities.push({
                        key: art.pimId,
                        value: art.availabilities
                    });
                }
            });

            let allArticles;

            if (this.isBrand) {
                this.topList = this.data.displayedArticles;
                if (this.isCz || this.isSb) {
                    this.hasMoreArticles = this.data.nonDisplayedArticles.length > 0;
                    this.hasLiquidation = false;
                } else {
                    this.hasMoreArticles = this.data.nonDisplayedArticles.some(item => !this.isLiquidationArticle(item));
                    this.hasLiquidation = this.data.nonDisplayedArticles.filter(item => this.isLiquidationArticle(item)).length > 0;
                }
                this.hasLiquidationEmiter.emit(this.hasLiquidation);
                allArticles = [...this.data.displayedArticles, ...this.data.nonDisplayedArticles];
            } else {
                allArticles = [...this.data.displayedArticles];
                if (this.projectId === ProjectId.AUTONET) {
                    this.autonetRemainSecondList = this.data.displayedArticles;
                    this.secondList = this.autonetRemainSecondList.splice(0, this.autonetMaxItem);
                    this.hasMoreArticles = this.autonetRemainSecondList.length > 0;
                } else {
                    this.secondList = this.data.displayedArticles;
                }
            }

            this.emitBrandsItems(allArticles);
            this.emitBarFilterItems(allArticles);

            this.requestErpInfo();
        }
    }

    private requestErpInfo(callback = null) {
        // For default section case
        if (this.isAutonet) {
            const filterByAvail = this.secondList.filter(item => !item.autonetRequested);
            this.requestErp(filterByAvail, this.secondList, () => {
                this.sortByAvail(this.secondList, (sortedArticles) => {
                    this.secondList = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, sortedArticles, this.brandPriorityAvail.p2);
                    this.isFirstLoading = false;
                    this.isArticlesLoaded = true;
                    this.handleFilter();
                });
            });
            return;
        }
        this.isFirstLoading = false;
        if (this.isBrand) {
            this.sorting.requestErpStock(this.topList, () => {
                this.sortByStock(this.topList, (sortedArticles) => {
                    this.topList = sortedArticles;
                    const filterByAvail = this.topList.filter(item => !this.hasAvail(item));
                    this.requestErp(filterByAvail, this.topList, () => {
                        this.topList = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, this.topList, this.brandPriorityAvail.p1);
                        if (this.isCz) {
                            this.topListTemp = [...this.topList];
                        }
                        this.sortArticles(this.sort);
                        if (this.isGroupByGaId && this.brandPriorityAvail.p1 && this.totalGroups >= 0 && this.topList.length === 0) {
                            this.totalGroupDefaultShowNoAvailMsg = this.totalGroupDefaultShowNoAvailMsg + 1;
                            this.totalGroupGaIDShowNoAvailMsgChange.emit(this.totalGroupDefaultShowNoAvailMsg);
                        }
                        this.isArticlesLoaded = true;
                        this.handleFilter();
                        this.sendArticleResultData.emit(this.topList);
                        this.sendVisibleArticlesToGa();
                    });
                });
            });
        } else {
            this.sorting.requestErpStock(this.secondList, () => {
                this.sortByStock(this.secondList, (sortedArticles) => {
                    this.secondList = sortedArticles;
                    const filterByAvail = this.secondList.filter(item => !this.hasAvail(item));
                    this.requestErp(filterByAvail, this.secondList, () => {
                        this.secondList = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, this.secondList, this.brandPriorityAvail.p2);
                        if (this.isCz) {
                            this.secondListTemp = [...this.secondList];
                        }
                        this.sortArticles(this.sort);
                        if (this.isGroupByGaId && this.brandPriorityAvail.p1 && this.totalGroups >= 0 && this.topList.length === 0) {
                            this.totalGroupDefaultShowNoAvailMsg = this.totalGroupDefaultShowNoAvailMsg + 1;
                            this.totalGroupGaIDShowNoAvailMsgChange.emit(this.totalGroupDefaultShowNoAvailMsg);
                        }
                        this.isArticlesLoaded = true;
                        this.handleFilter();
                        this.sendArticleResultData.emit(this.secondList);
                        this.sendVisibleArticlesToGa();
                        if (this.projectId === ProjectId.AUTONET) {
                            if (callback) {
                                callback();
                            }
                        }
                    });
                });
            });
        }
    }

    requestErpInfoAutonet(articles, callback) {
        if (!articles || articles.length == 0) {
            if (callback) {
                callback([]);
            }
            return;
        }
        const filterByAvail = articles.filter(item => !item.autonetRequested);
        this.requestErp(filterByAvail, articles, () => {
            this.sortByAvail(articles, (sortedArticles) => {
                const results = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, sortedArticles, this.brandPriorityAvail.p2);

                if (callback) {
                    callback(results);
                }
            });
        });
    }

    requestErpInfoWsp(articles, callback) {
        if (!articles || articles.length == 0) {
            if (callback) {
                callback([]);
            }
            return;
        }
        const filterByAvail = articles.filter(item => !this.hasAvail(item));
        this.requestErp(filterByAvail, articles, () => {

            if (callback) {
                callback(articles);
            }
        });

    }

    showLiquidation(forceShow = false) {
        if (!this.hasLiquidation) {
            return;
        }
        if (this.isLoadingLiquidations) {
            return;
        }
        this.isLoadingLiquidations = true;
        if (this.isShownliquidation && !forceShow) {
            this.isShownliquidation = false;
            this.isLoadingLiquidations = false;
            this.sendVisibleArticlesToGa();
        } else {
            this.isShownliquidation = true;
            this.requestInfoForLiquidationPart(() => {
                this.sendArticleResultData.emit(this.liquidationArticles);
                this.sendVisibleArticlesToGa(false);
                setTimeout(() => {
                    if (this.liqSection) {
                        this.liqSection.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
                    }
                });
            });
        }
    }

    private async requestInfoForLiquidationPart(callback) {
        if (this.liquidationArticles.length === 0) {
            const liquidationArticles = this.data.nonDisplayedArticles.filter(item => this.isLiquidationArticle(item));
            const requestItems = this.brandFilterPipe
                .transform(liquidationArticles, this.filterBrands, this.filterCriteria);

            this.sorting.requestErpStock(requestItems, () => {
                this.sortByStock(liquidationArticles, (sortedArticles: ArticleModel[]) => {
                    this.liquidationArticles = sortedArticles;
                    this.isLoadingLiquidations = false;
                    const requestAvailItems = requestItems.filter(item => !this.hasAvail(item));
                    this.requestErp(requestAvailItems, requestItems, () => {
                        this.liquidationArticles = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, this.liquidationArticles, this.brandPriorityAvail.p3);
                        callback();
                    });
                });
            });
        } else {
            this.isLoadingLiquidations = false;
            callback();
        }
    }

    showMoreArticles(forceShow = false, callback = null) {
        if (this.projectId === ProjectId.AUTONET) {
            const filterBrands = BrandFilterUtil
                .getUniqCombinedBrands(this.brandsData)
                .filter(item => item.checked)
                .map(item => item.name);
            let list;
            if (filterBrands.length > 0) {
                list = this.autonetRemainSecondList.splice(0, this.autonetRemainSecondList.length);
            } else {
                list = this.autonetRemainSecondList.splice(0, this.autonetMaxItem);
            }
            this.hasMoreArticles = this.brandFilterPipe.transform(this.autonetRemainSecondList, this.filterBrands, this.filterCriteria).length > 0;
            this.secondList = [...this.secondList, ...list];
            this.requestErpInfoAutonet(this.secondList, () => {
                if (callback) {
                    callback();
                }
                this.secondList = this.secondList.slice(); // trigger change detection after getting availability
            });
            return;
        }
        if (this.articleListType == ARTICLE_LIST_TYPE.WSP) {
            const wspArticleList = this.wspRemainTopList.splice(0, this.wspMaxItem);
            this.isLoadingMoreArticlesUsedForWsp = true;
            this.topList = [...this.topList, ...wspArticleList];
            this.topListTemp = [...this.topListTemp, ...wspArticleList];
            this.requestErpInfoWsp(wspArticleList, (articles) => {
                this.topList = this.topList.slice();
                this.sortArticles(this.sort);
                this.sendArticleResultData.emit(articles);
                this.sendVisibleArticlesToGa();
                this.isLoadingMoreArticlesUsedForWsp = false;
            });
            return;
        }
        if (this.isLoadingMoreArticles) {
            return;
        }
        this.isLoadingMoreArticles = true;
        if (this.isShownMoreArticles && !forceShow) {
            this.isShownMoreArticles = false;
            this.isLoadingMoreArticles = false;
            if (this.isCz) {
                this.topList = [...this.topListTemp];
                this.secondList = [...this.secondListTemp];
                this.sortArticles(this.sort);
            }
            this.sendVisibleArticlesToGa();
        } else {
            this.isShownMoreArticles = true;

            this.loadInfoForMoreArticles(() => {
                if (this.isCz) {
                    this.sortArticles(this.sort);
                }
                this.sendArticleResultData.emit(this.secondList);
                this.sendVisibleArticlesToGa(false);
            });
        }
    }

    private async loadInfoForMoreArticles(callback) {
        this.isShownMoreArticles = true;
        const secondList = this.isCz || this.isSb ? [...this.data.nonDisplayedArticles] : this.data.nonDisplayedArticles.filter(item => !this.isLiquidationArticle(item));
        const requestItems = this.brandFilterPipe.transform(secondList, this.filterBrands, this.filterCriteria);
        const toBeRequest = requestItems.filter(item => !item.stockRequested || !item.priceRequested || !item.availRequested);
        if (toBeRequest.length > 0) {
            if (this.isCz || this.isSb) {
                this.sorting.requestErpStock(requestItems, () => {
                    this.sortByStock(requestItems, (sortedArticles: ArticleModel[]) => {
                        this.topList = [...this.topListTemp, ...sortedArticles];
                        this.secondListTemp = [...sortedArticles];
                        this.secondList = [];
                        this.isLoadingMoreArticles = false;
                        const requestAvailItems = requestItems.filter(item => !this.hasAvail(item));
                        this.requestErp(requestAvailItems, requestItems, () => {
                            this.topList = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, this.topList, this.brandPriorityAvail.p2);
                            callback();
                        });
                    });
                });
            } else {
                this.secondList = [...secondList];
                this.sorting.requestErpStock(requestItems, () => {
                    this.sortByStock(this.secondList, (sortedArticles: ArticleModel[]) => {
                        this.secondList = sortedArticles;
                        this.isLoadingMoreArticles = false;
                        const requestAvailItems = requestItems.filter(item => !this.hasAvail(item));
                        this.requestErp(requestAvailItems, requestItems, () => {
                            this.secondList = this.brandPriorityAvailService.applyBrandPriorityAvail(this.projectId, this.secondList, this.brandPriorityAvail.p2);
                            callback();
                        });
                    });
                });
            }
        } else {
            this.isLoadingMoreArticles = false;
            if (this.isCz || this.isSb) {
                this.secondList = [];
                this.topList = [...this.topListTemp, ...this.secondListTemp];
            }
            callback();
        }
    }

    private assignErpInfo(articles: ArticleModel[], infos: any[], erpRequest: any, assignAvail = false) {
        articles.forEach(article => {
            const artInfo = (infos || []).find(p => p.key === article.pimId);
            this.sorting.assignArticleErpInfo(article, artInfo, erpRequest, assignAvail);
        });
    }

    private async requestErp(requestAvailArticles, requestErpArticles, callback?) {
        // Get articles are added to cart in order to update the amountNumber before request erp
        const articleIdsIsAddedToCart = await this.getArticlesIsAddedToCart(requestAvailArticles);
        articleIdsIsAddedToCart.forEach(item => {
            const { pimId, quantity } = item;
            const articleFoundIdx = requestAvailArticles.findIndex(item => item.pimId === pimId);
            if (articleFoundIdx !== -1) {
                requestAvailArticles[articleFoundIdx].amountNumber = quantity;
            }
        });
        let index = 0;
        const requests = [];
        const stock_requests = [];
        let stock_obs: Observable<any>;

        const requestAvailItems = requestAvailArticles.filter(art => !art.pseudo);
        const requestErpItems = requestErpArticles.filter(art => !art.pseudo);

        const erpRequest = {
            stockRequested: !this.isCz9
        };
        const filterByErpDefault = requestErpItems.filter(item => !this.isCz9 && !item.stockRequested);
        const filterByErp = this.isAutonet ? [] : filterByErpDefault;

        if (filterByErp.length > 0) {
            stock_requests.push(this.articlesService.getArticlesInfoWithBatch(filterByErp, filterByErp.length, erpRequest));
        }

        if (stock_requests.length > 0) {
            stock_obs = forkJoin(stock_requests).pipe(
                map(respones => {
                    let allInfos = []
                    respones.forEach(res => {
                        const infos = this.articlesService.getDisplayedInfo(res.items);
                        allInfos = [...allInfos, ...infos];
                    });
                    this.assignErpInfo(requestErpItems, allInfos, erpRequest);
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
            while (index < requestAvailItems.length) {
                const requestItemsBatch = requestAvailItems.slice(index, index + NUMBER_ITEMS_REQUEST_AVAILABILITY);
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
                    if (!this.isAutonet) {
                        this.availabilities = [...this.availabilities, ...avails];
                    }
                    this.assignErpInfo(requestErpItems, allInfos, availRequest, true);
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

    private sortByAvail(articles: ArticleModel[], callback?) {
        articles.forEach(article => {
            // assign avail to article for sorting purpose;
            const artAvail = this.availabilities.find(avail => avail.key === article.pimId);
            if (!!artAvail && !article.availabilities) {
                article.availabilities = artAvail.value;
            }
        });
        // sort by avail;
        const sortedArticles = this.sorting.sortBySubPrio(articles) as ArticleModel[];
        if (callback) {
            callback(sortedArticles);
        }
    }

    private sortByStock(articles: ArticleModel[], callback?) {
        const sortedArticles = this.sorting.sortBySubPrioAndStock(articles) as ArticleModel[];
        if (callback) {
            callback(sortedArticles);
        }
    }

    private isChanged(newGroup: ArticleGroupModel) {
        if (!this.storedGroup) {
            return true;
        }
        const olds = this.storedGroup.values.map(art => art.pimId);
        const news = newGroup.values.map(art => art.pimId);
        const difference = xor(olds, news);
        return difference.length > 0;
    }

    private isLiquidationArticle(article) {
        return !article.prio; // Article with no defined brand priority or brand prio = 0
    }

    private hasAvail(item: ArticleModel) {
        return !!this.availabilities.find(avail => avail.key === item.pimId);
    }

    private filterListWithHinten(list) {
        const hintenList = [];
        const otherList = [];

        const rearMultilingual = this.translateService.instant('ARTICLE.REAR');

        list.forEach((article: any) => {
            const criteria: any[] = article.criteria;
            const hintenArticle = criteria
                .find(criterion =>
                    criterion.cid === '100' && (includes(criterion.cvp.toLowerCase(), rearMultilingual.toLowerCase()) ||
                        includes(criterion.cvp.toLowerCase(), 'hinte')));
            if (!!hintenArticle) {
                hintenList.push(article);
            } else {
                otherList.push(article);
            }
        });

        return { hintenList, otherList };
    }

    private checkShowNoAvailMsgForDefaultGroup() {
        if (this.isGroupByGaId) {
            if (this.totalGroupDefaultShowNoAvailMsg === this.totalGroups) {
                if (this.indexGroup === 0) {
                    this.showNoAvailMsgForDefaultGroup = true;
                } else {
                    this.showNoAvailMsgForDefaultGroup = false;
                }
            } else {
                this.showNoAvailMsgForDefaultGroup = false;
            }
        } else {
            this.showNoAvailMsgForDefaultGroup = true;
        }
    }

    private handleFilter() {
        if (!this.isArticlesLoaded) {
            return;
        }
        if (this.articleListType == ARTICLE_LIST_TYPE.WSP) {
            return;
        }

        const filterBrands = BrandFilterUtil
            .getUniqCombinedBrands(this.brandsData)
            .filter(item => item.checked)
            .map(item => item.name);

        const difference = xor(filterBrands, this.filterBrands);

        const filterCriteria = (this.barFilterOption && this.barFilterOption.options || [])
            .filter(op => op.checked);
        const differenceCriteria = xor(filterCriteria, this.filterCriteria);

        if (differenceCriteria.length === 0 && difference.length === 0) {
            return;
        }

        this.filterBrands = difference.length !== 0 && filterBrands || this.filterBrands;
        this.filterCriteria = differenceCriteria.length !== 0 && filterCriteria || this.filterCriteria;
        if (this.isBrand || this.isAutonet) {
            let topList;
            if (this.isAutonet) {
                topList = this.brandFilterPipe.transform(this.secondList, this.filterBrands, this.filterCriteria);
            } else {
                topList = this.brandFilterPipe.transform(this.isCz ? this.topListTemp : this.topList, this.filterBrands, this.filterCriteria);
            }
            let nonDisplayedArticles = this.brandFilterPipe.transform(this.data.nonDisplayedArticles, this.filterBrands, this.filterCriteria);

            if (this.isAutonet) {
                this.hasMoreArticles = this.brandFilterPipe.transform(this.autonetRemainSecondList, this.filterBrands, this.filterCriteria).length > 0;
            } else if (this.isCz || this.isSb) {
                this.hasMoreArticles = nonDisplayedArticles.length > 0;
                this.hasLiquidation = false;
            } else {
                this.hasMoreArticles = nonDisplayedArticles.some(item => !this.isLiquidationArticle(item));
                this.hasLiquidation = nonDisplayedArticles.filter(item => this.isLiquidationArticle(item)).length > 0;
            }
            this.hasLiquidationEmiter.emit(this.hasLiquidation);
            if (topList.length === 0) {
                if (this.hasMoreArticles) {
                    this.showMoreArticles(true);
                } else if (this.hasLiquidation) {
                    this.showLiquidation(true);
                }
            } else {
                if (this.hasMoreArticles && this.isShownMoreArticles) {
                    this.loadInfoForMoreArticles(() => {
                        if (this.isCz) {
                            this.sortArticles(this.sort);
                        }
                        this.sendArticleResultData.emit(this.secondList);
                        this.sendVisibleArticlesToGa(false);
                    });
                }
                if (this.hasLiquidation && this.isShownliquidation) {
                    this.requestInfoForLiquidationPart(() => {
                        this.sendArticleResultData.emit(this.liquidationArticles);
                        this.sendVisibleArticlesToGa(false);
                    });
                }

            }
        }
    }

    private emitBrandsItems(articles: ArticleModel[]) {
        const brands = BrandFilterUtil.getUniqCombinedBrands(this.brandsData);
        const newBrands = [...articles].map(art => art.supplier);
        const uniqBrands = uniq(newBrands).map(brand => {
            return {
                name: brand,
                checked: brands.some(item => item.name === brand && item.checked)
            };
        });
        const newBrandItem = new BrandFilterItem({
            key: this.groupName,
            gaID: this.storedGroup.gaId,
            brands: uniqBrands
        });
        if (this.articleListType !== ARTICLE_LIST_TYPE.WSP) {
            this.brandsItemsChange.emit(newBrandItem);
        }
    }

    private emitBarFilterItems(articles: ArticleModel[]) {
        if (!this.storedGroup || !this.storedGroup.cate) {
            return;
        }

        const currentSelectedCategory = this.storedGroup.cate;

        const { filters } = currentSelectedCategory;
        if (!filters || filters.length === 0) {
            return;
        }
        const filterData = filters[0];
        const filterCaid = filterData.filterCaid;
        const filterDefault = filterData.filterDefault;
        const filterBar = filterData.filterBar;
        const filterSort = filterData.filterSort;
        const filterOpen = filterData.filterOpen;
        let filterCn = '';
        const newFilters = [];
        [...articles].map(art => {
            const filter = art.criteria.find(cr => cr.cid === filterCaid);
            if (filter) {
                filterCn = filter.cn;
                newFilters.push(filter);
                newFilters.push(filter);
            }
        });

        const uniqFilters = uniqBy(newFilters, 'cvp').map(filter => {
            return {
                cvp: filter.cvp,
                cid: filterCaid,
                cn: filter.cn,
                checked: filter.cvp == filterDefault
            };
        });
        const newFilterItem = new BarFilter({
            key: this.groupName,
            gaID: this.storedGroup.gaId,
            filterCaid,
            name: filterCn,
            filterBar,
            filterSort,
            filterOpen,
            options: uniqFilters
        });
        if (this.articleListType !== ARTICLE_LIST_TYPE.WSP) {
            this.barFilterItemsChange.emit(newFilterItem);
        }
    }

    sortArticles(sort: ArticleSort) {
        if (!this.sort) {
            return;
        }
        if (sort.ascessdingSupplier === undefined && sort.ascessdingGrossPrice === undefined && sort.ascessdingNetPrice === undefined) {
            if (this.isShownMoreArticles) {
                this.topList = [...this.topListTemp, ...this.secondListTemp];
                this.secondList = [];
            } else {
                this.topList = [...this.topListTemp];
                this.secondList = [...this.secondListTemp];
            }
            return;
        }
        if (sort.ascessdingGrossPrice !== undefined) {
            if (this.isBrand) {
                this.topList = ArticleSortUtil.sortByGrossPrice(this.topList, sort.ascessdingGrossPrice);
            } else {
                this.secondList = ArticleSortUtil.sortByGrossPrice(this.secondList, sort.ascessdingGrossPrice);
            }
            return;
        }
        if (sort.ascessdingNetPrice !== undefined) {
            if (this.isBrand) {
                this.topList = ArticleSortUtil.sortByNetPrice(this.topList, sort.ascessdingNetPrice);
            } else {
                this.secondList = ArticleSortUtil.sortByNetPrice(this.secondList, sort.ascessdingNetPrice);
            }
            return;
        }
    }

    updateFilterCaId(data: WspCategoryModel) {
        if (!data) {
            return;
        }
        if (data && data.filterBar == '1') {
            this.filterByCaId = data.filterCaid;
            this.defaultCaIdFilterVal = data.filterDefault;
            this.showFilterBar = data.filterBar == '1';
            this.filterSort = data.filterSort == '1';
        } else {
            this.filterByCaId = null;
            this.defaultCaIdFilterVal = null;
        }
    }

    private sendVisibleArticlesToGa(isInit = true) {
        const result = this.getVisibleArticles(isInit);
        if (result.isChanged) {
            this.sendArticlesGaData.emit(result.articles);
        }
    }

    private getVisibleArticles(isInit) {
        const articles = this.getFilteredArticles(this.topList);
        this.defaultTotal = articles.length;
        this.showMoreTotal = 0;
        let isChanged = isInit;
        if (this.isShownMoreArticles) {
            const filteredMore = this.getFilteredArticles(this.secondList);
            this.showMoreTotal = filteredMore.length;
            isChanged = isChanged || filteredMore.length > 0;
            articles.push(...filteredMore);
        }
        if (this.isShownliquidation) {
            const filteredLiquidation = this.getFilteredArticles(this.liquidationArticles);
            isChanged = isChanged || filteredLiquidation.length > 0;
            articles.push(...filteredLiquidation);
        }
        return { isChanged, articles };
    }

    private getFilteredArticles(articles) {
        let result = this.brandFilterPipe.transform(articles, this.filterBrands, this.filterCriteria);
        result = this.sagShowHideNonAvailPipe.transform(result, {
            affiliate: this.config.affiliate,
            projectId: this.config.projectId,
            hideNonAvail: this.hideNonAvail
        });
        return [...result] || [];
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
}
