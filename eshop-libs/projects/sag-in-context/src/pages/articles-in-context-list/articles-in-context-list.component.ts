import { Component, OnInit, OnDestroy, Input, TemplateRef, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { uniqBy } from 'lodash';
import { ArticleInContextService } from '../../services/articles-in-context.service';
import { SagInContextStorageService } from '../../services/articles-in-context-storage.service';
import { ARTICLE_LIST_TYPE, ARTICLE_TYPE, CategoryTreeService, BrandFilterItem } from 'sag-article-list';
import { SagInContextIntegrationService } from '../../services/articles-in-context-integration.service';
import { CustomPriceUpdate, SagCustomPricingService } from 'sag-custom-pricing';
import { SagInContextConfigService } from '../../services/articles-in-context-config.service';
import { Observable, Observer } from 'rxjs';
import { ArticleModel, BarFilter } from 'sag-article-detail';
import { AffiliateUtil } from 'sag-common';
@Component({
    selector: 'sag-in-context-article-list',
    templateUrl: './articles-in-context-list.component.html',
    styleUrls: ['./articles-in-context-list.component.scss']
})
export class SagInContextArticleListComponent implements OnInit, OnDestroy {
    @Input() userPrice: any = {};
    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;
    @Input() articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;
    @Input() brandsDataAsync: Observable<any>;
    @Output() emitBrandFilterData = new EventEmitter<any[]>();

    @Output() emitBarFilterData = new EventEmitter<any[]>();
    @Output() barFilterStateChange = new EventEmitter<BarFilter>();

    @Input() moreData: any;
    @Input() hasMoreData: boolean;
    @Output() showMoreArticlesEmitter = new EventEmitter();
    @Input() barFilterOptionAsync: Observable<any>;
    @Input() genArts = [];
    @Output() rootHeaderClick = new EventEmitter();

    @Input() articleCount = 0;

    @Input() gaId: number;
    @Input() isFavoritedList = false;
    @Input() enabledFavorite = false;
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    brandsData;
    barFilterData;
    cartItemQuantity = 0;
    returnUrl: string;
    isSb = AffiliateUtil.isSb(this.config.affiliate)

    private subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private config: SagInContextConfigService,
        public articleInContextService: ArticleInContextService,
        public storage: SagInContextStorageService,
        private categoryTreeService: CategoryTreeService,
        private customPricingService: SagCustomPricingService,
        public integrationService: SagInContextIntegrationService
    ) { }

    ngOnInit() {
        this.subs.sink = this.integrationService.basketQuantity$
            .subscribe(quantity => {
                this.cartItemQuantity = quantity;
            });
        this.subs.sink = this.activatedRoute.queryParams.subscribe(params => {
            if (params.returnUrl) {
                this.returnUrl = params.returnUrl;
            }
        })
        this.subs.sink = this.brandsDataAsync.subscribe(data => {
            this.brandsData = data;
        })

        this.subs.sink = this.barFilterOptionAsync.subscribe(data => {
            this.barFilterData = data;
        })
    }

    ngOnDestroy(): void {
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

    onCurrentNetPriceChange() {
        this.integrationService.toggleNetPriceView();
    }

    async onCustomPriceChange({ price }) {
        if (!this.articleInContextService.articles || this.articleInContextService.articles.length === 0) {
            return;
        }
        const articles = [...this.articleInContextService.articles];
        const spinner = this.config.spinner.start('sag-in-context-articles-result-list .result', {
            containerMinHeight: 200
        });
        const prices = await this.customPricingService.updateCustomPriceByBrand(price, articles, {
            brand: this.storage.selectedVehicle.vehicle_brand,
            brandId: this.storage.selectedVehicle.id_make
        });

        articles.forEach(art => {
            const customPrice = prices.find(p => p.articleId === art.pimId);
            art.displayedPrice = customPrice && customPrice.displayedPrice || null;
        });
        const basket = await this.integrationService.loadMiniBasket();
        const updatingItems = basket.items.filter(item => {
            return this.storage.selectedVehicle.vehid === item.vehicleId &&
                item.itemType !== ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE];
        }).map(item => {
            const customPrice = prices.find(p => p.articleId === item.articleItem.pimId);
            if (!!customPrice) {
                return {
                    cartKey: item.cartKey,
                    displayedPrice: customPrice && customPrice.displayedPrice
                } as CustomPriceUpdate;
            }
            return null;
        }).filter(item => !!item);

        if (updatingItems && updatingItems.length > 0) {
            const res = await this.customPricingService.updateCustomPriceInBasket(updatingItems).toPromise();
            // update shopping basket
            this.integrationService.updateOtherProcess(res);
        }
        const selectedCategories = this.categoryTreeService.getCheckedCategories();

        const groupedArticles = this.articleInContextService.groupArticle(
            articles,
            selectedCategories,
            [],
            this.storage.selectedVehicle.vehid
        );

        groupedArticles.forEach(group => {
            group.values.forEach(g => {
                g.isForceChanged = true;
            });
        });

        this.articleInContextService.emitData(groupedArticles);

        this.config.spinner.stop(spinner);

        this.integrationService.sendArticleListEventData({
            article: {
                artlistPriceType: price.type,
                artlistPriceVehBrand: this.storage.selectedVehicle.vehicle_brand,
                artlistVehicleId: this.storage.selectedVehicle.vehid
            }
        });
    }

    goToBasket() {
        this.router.navigate(['shopping-basket/cart']);
    }

    onBrandsItemsChange(data: BrandFilterItem) {
        const userSetting: any = this.integrationService.userPrice;
        let isBrandFilterEnable = userSetting.isSalesOnBeHalf ? userSetting.salesBrandFilterEnabled : userSetting.customerBrandFilterEnabled;

        if (!isBrandFilterEnable) {
            return;
        }

        const brandsData = [...this.articleInContextService.brandsData];
        const results = brandsData.filter(item => item.key !== data.key || item.gaID !== data.gaID);
        this.emitBrandFilterData.emit([...results, data]);
    }

    onBrandsStateChange(data: BrandFilterItem) {
        const brandsData = [...this.brandsData] || [];

        brandsData.forEach(item => {
            if (item.key === data.key) {
                item.brands.forEach(brand => {
                    brand.checked = data.brands.some(b => b.name === brand.name && b.checked);
                });
            }
        });

        this.emitBrandFilterData.emit(brandsData);
    }

    onBarFilterItemsChange(data) {
        const barFilterData = [...this.articleInContextService.barFilterData];
        const results = barFilterData.filter(item => item.key !== data.key);

        let currentFilter = barFilterData.filter(item => item.key === data.key)[0];
        if (currentFilter) {
            currentFilter.options = uniqBy([...currentFilter.options, ...data.options], 'cvp');
        } else {
            currentFilter = data;
        }

        let options = [...currentFilter.options];
        const isAllNumber = !((options || []).some(op => !parseFloat(op.cvp)));
        if (isAllNumber) {
            options = (options || []).sort((opA, opB) => parseFloat(opA.cvp) - parseFloat(opB.cvp));
        } else {
            options = (options || []).sort((opA, opB) => opA.cvp > opB.cvp ? 1 : -1);
        }

        currentFilter.options = options;
        this.emitBarFilterData.emit([...results, currentFilter]);
    }

    onBarFilterStateChange(data: BarFilter) {
        if (this.articleListType === ARTICLE_LIST_TYPE.IN_CONTEXT) {
            const barFilterData = [...this.barFilterData] || [];
            const filterData = data && data[0];
            if (!filterData) {
                return;
            }
            barFilterData.forEach(item => {
                if (item.key === filterData.key) {
                    item.options.forEach(filter => {
                        filter.checked = (filterData.options || []).some(op => op.cvp === filter.cvp && op.checked);
                    })
                }
            });

            this.emitBarFilterData.emit(barFilterData);
            return;
        }
        this.barFilterStateChange.emit(data);
    }

    onArticleResultDisplay(articles) {
        if (this.articleListType == ARTICLE_LIST_TYPE.WSP) {
            this.integrationService.sendArticleResultData(articles);
            return;
        }

        if (this.articleListType == ARTICLE_LIST_TYPE.IN_CONTEXT) {
            const vehicle = this.storage.selectedVehicle;
            const vehicleId = vehicle && vehicle.id || "";
            this.integrationService.sendArticleResultData(articles, vehicleId);
        }
    }

    onSendArticlesGaData(articles) {
        this.integrationService.sendArticlesGaData(articles);
    }

    onRemovePseudoGroup(key) {
        this.articleInContextService.removePseudoGroup(key);
    }
}
