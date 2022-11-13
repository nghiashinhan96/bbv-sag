import { Component, OnDestroy, OnInit, EventEmitter, Output, Input, ViewChild, TemplateRef } from '@angular/core';
import { BarFilter } from 'sag-article-detail'
import {
    ARTICLE_LIST_TYPE,
    SEARCH_MODE
} from 'sag-article-list';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';
import { SubSink } from 'subsink';
import { UniversalPartService } from '../../services/wsp.service';
import { AffiliateUtil, ARTICLE_EVENT_SOURCE, BroadcastService } from 'sag-common';
import { ArticleBroadcastKey, ArticleBasketModel, ArticleModel, CZ_AVAIL_STATE, FAVORITE_BROADCAST_EVENT, FavoriteItem, FAVORITE_PROCESS_TYPE, FavoriteService, FavoriteBusinessService } from 'sag-article-detail';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import {
    ArticleInContextService
} from 'sag-in-context';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { environment } from 'src/environments/environment';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { VIEW_MODE } from '../../services/constants';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ArticleReplaceModalComponent } from 'src/app/shared/connect-common/components/article-replace-modal/article-replace-modal.component';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';

@Component({
    selector: 'connect-wsp-article-list',
    templateUrl: './wsp-article-list.component.html',
    styleUrls: ['./wsp-article-list.component.scss']
})
export class WspArticleListComponent implements OnInit, OnDestroy {
    @Output() onBrandFilterStateChange = new EventEmitter<any[]>();
    @Output() onBarFilterStateChange = new EventEmitter<any[]>();

    @Input() viewMode;
    @Input() favoriteArticle: ArticleModel[];
    @Input() genArts = [];
    @Input() breadcrumbs = [];
    @Input() categoryLabel: string;
    @Input() currentTreeActive: string;
    @Output() rootHeaderClick = new EventEmitter();
    @Output() onLoadMore = new EventEmitter();

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    VIEW_MODE = VIEW_MODE;

    favoriteInfoOfList: FavoriteItem = null;
    loadingFavoriteInfoOfList = true;
    public articleListType = ARTICLE_LIST_TYPE.WSP;
    private subs = new SubSink();

    constructor(
        public universalPartService: UniversalPartService,
        public userService: UserService,
        public storageService: AppStorageService,
        public broadcastService: BroadcastService,
        public shoppingBasketService: ShoppingBasketService,
        public articleShoppingBasketService: ArticleShoppingBasketService,
        private articleInContextService: ArticleInContextService,
        private router: Router,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService,
        private favoriteService: FavoriteService,
        private favoriteBusinessService: FavoriteBusinessService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        this.articleListType = this.viewMode === VIEW_MODE.article ? ARTICLE_LIST_TYPE.WSP : ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
        this.getFavoriteInfoOfList();
        this.articleShoppingBasketService.observeArticleRemove();
        this.articleShoppingBasketService.observeArticleUpdate();
        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'LOADED':
                        this.articleShoppingBasketService.isAddedInCart(data);
                        const article = this.articleInContextService.articles.find(art => art.pimId === data.article.pimId);
                        if (article) {
                            article.availabilities = data.article.availabilities;
                        }
                        break;
                    case 'ADD':
                        if (data.uuid) {
                            SpinnerService.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0 }
                            );
                        }
                        if (data && data.article) {
                            data.article.source = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_WSP;
                        }
                        this.articleShoppingBasketService.addItemToCart(data);
                        break;
                    case 'CUSTOM_PRICE_CHANGE':
                        this.shoppingBasketService.updateOtherProcess(data.basket);
                        break;
                }
            });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe((code: string) => {
                this.router.navigate(['article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
            });
        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL)
            .subscribe((data: any) => {
                this.articlesAnalyticService.sendArticleListEventData(data);
                this.gaService.viewProductDetails(data.article, data.rootModalName);
            });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.FAVORITE_ITEM_EVENT)
            .subscribe((data: any) => {
                switch (data.action) {
                    case FAVORITE_BROADCAST_EVENT.ADD_LEAF:
                    case FAVORITE_BROADCAST_EVENT.EDIT_LEAF: {
                        let favoriteItem = this.universalPartService.getFavoriteListItemById(this.universalPartService.selectedGenArtIds[0], FAVORITE_PROCESS_TYPE.LEAF_NODE);
                        if (!favoriteItem) {
                            const labels = [this.categoryLabel, ...((this.breadcrumbs || []).map(br => br.nodeName))];
                            if (this.genArts.length > 1) {
                                labels.push(this.universalPartService.selectedGenArtName);
                            }
                            const title = labels.join(' > ');
                            favoriteItem = new FavoriteItem({
                                comment: '',
                                type: FAVORITE_PROCESS_TYPE.LEAF_NODE,
                                treeId: this.currentTreeActive,
                                title,
                                gaId: this.universalPartService.selectedGenArtIds[0],
                                leafId: this.breadcrumbs[this.breadcrumbs.length - 1] && this.breadcrumbs[this.breadcrumbs.length - 1].nodeId
                            });
                        }
                        this.favoriteBusinessService.showFavoriteModal(data.action, favoriteItem);
                        break;
                    }
                }
            })
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

    getFavoriteInfoOfList() {
        this.loadingFavoriteInfoOfList = true;
        const treeId = this.currentTreeActive;
        const gaId = this.universalPartService.selectedGenArtIds[0];
        const leafId = this.breadcrumbs[this.breadcrumbs.length - 1] && this.breadcrumbs[this.breadcrumbs.length - 1].nodeId;
        this.favoriteService.getGenArtStatus(treeId, leafId, gaId)
            .pipe(
                finalize(() => {
                    this.loadingFavoriteInfoOfList = false;
                })
            )
            .subscribe(res => {
                this.favoriteInfoOfList = this.favoriteService.currentGenArtStatus;
            });
    }

    onCurrentNetPriceChange() {
        this.userService.toggleNetPriceView();
    }

    emitBrandFilterData(data) {
        const checkedBrands = [];
        data.forEach(item => {
            item.brands.forEach(brand => {
                if (brand.checked) {
                    checkedBrands.push(brand.name)
                }
            });
        });
        this.onBrandFilterStateChange.emit(checkedBrands);
        this.universalPartService.emitBrandFilterData(data);
    }

    emitBarFilterStateChange(data: BarFilter[]) {
        if (!data) {
            return;
        }
        const checkedFilter = [];
        (data || []).map(barFilter => {
            (barFilter.options || []).map(op => {
                if (op.checked) {
                    checkedFilter.push({ cid: op.cid, cvp: op.cvp })
                }
            });
        })

        this.onBarFilterStateChange.emit(checkedFilter);
    }

    showMoreArticles(callback) {
        SpinnerService.start();
        this.universalPartService.loadMoreArticle()
            .pipe(
                finalize(() => {
                    SpinnerService.stop();
                    if (callback) {
                        callback();
                    }
                })
            ).subscribe(result => {
                this.onLoadMore.emit(result);
            });
    }
}
