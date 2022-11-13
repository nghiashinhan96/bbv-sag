import { Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { BsModalRef } from "ngx-bootstrap/modal";
import { PopoverDirective } from "ngx-bootstrap/popover";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";
import { get } from 'lodash';
import { ArticleBasketModel, ArticleBroadcastKey, ArticleModel, CZ_AVAIL_STATE } from "sag-article-detail";
import { ArticleListStorageService, ARTICLE_LIST_TYPE, SEARCH_MODE } from "sag-article-list";
import { AffiliateUtil, BroadcastService } from "sag-common";
import { MultiLevelCategory } from "src/app/article-not-context-result-list/models/multi-level-category.model";
import { FreetextArticleService } from "src/app/article-not-context-result-list/service/freetext-article.service";
import { SHOPPING_BASKET_PAGE } from "src/app/core/conts/app.constant";
import { SHOPPING_BASKET_ENUM } from "src/app/core/enums/shopping-basket.enum";
import { AppStorageService } from "src/app/core/services/app-storage.service";
import { ShoppingBasketService } from "src/app/core/services/shopping-basket.service";
import { UserService } from "src/app/core/services/user.service";
import { SpinnerService } from "src/app/core/utils/spinner";
import { CZ_PRICE_TYPE } from "src/app/shared/cz-custom/enums/article.enums";
import { environment } from "src/environments/environment";
import { SubSink } from "subsink";
import { CommonModalService } from "../../services/common-modal.service";
import { ArticlesAnalyticService } from "src/app/analytic-logging/services/articles-analytic.service";
import { SUB_LIST_NAME_TYPE } from "src/app/analytic-logging/enums/event-type.enums";

@Component({
    selector: 'connect-article-cross-reference-modal',
    templateUrl: './article-cross-reference-modal.component.html'
})
export class ArticleCrossReferenceModalComponent implements OnInit, OnDestroy {
    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;
    
    @Input() set article(art: ArticleModel) {
        this.root = this.getArticle(art);
    }
    @Input() vehicle: any;
    @Input() category: any;

    root: ArticleModel;
    data: any[];
    moreData: any;
    hasMoreData: boolean;

    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isCz9 = AffiliateUtil.isAffiliateCZ9(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
    rootModalName = SUB_LIST_NAME_TYPE.CROSS_REFERENCE_ITEMS;

    isSubBasket = false;

    private categoryLevel: MultiLevelCategory;

    private spinner;
    private pops: PopoverDirective[] = [];
    private subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        public bsModalRef: BsModalRef,
        public appStorage: AppStorageService,
        public userService: UserService,
        private articleListBroadcastService: BroadcastService,
        private shoppingBasketService: ShoppingBasketService,
        private freetextArticleService: FreetextArticleService,
        private articleListStorageService: ArticleListStorageService,
        private commonModalService: CommonModalService,
        private articlesAnalyticService: ArticlesAnalyticService,
    ) {

    }

    ngOnInit() {
        setTimeout(() => {
            this.spinner = SpinnerService.start('modal-container:last-child connect-article-cross-reference-modal .modal-body');
        }, 50);
        this.subs.sink = this.searchArticle()
            .pipe(
                map(res => this.extractData(res))
            ).subscribe();

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'REMOVE':
                        if (data.uuid) {
                            SpinnerService.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0 }
                            );
                        }
                        this.shoppingBasketService.removeBasketItem({ cartKeys: [data.cartKey] })
                            .pipe(
                                finalize(() => {
                                    SpinnerService.stop(`#part-detail-${data.uuid}`);
                                    this.bsModalRef.hide();
                                }),
                            )
                            .subscribe();
                        break;
                }
            });

            this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
                .subscribe((vehicleId: string) => {
                    if (vehicleId) {
                        this.bsModalRef.hide();
                        this.articleListStorageService.accessoryArticle = new ArticleModel({...this.root, criteria: []});
                        this.router.navigate(['vehicle', vehicleId]);
                    }
                });

        const url = this.router.url || '';
        const params = this.activatedRoute.snapshot.queryParams || {};
        this.isSubBasket = url.indexOf(SHOPPING_BASKET_PAGE) !== -1 && params.basket === SHOPPING_BASKET_ENUM.FINAL;
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    toggleNetPriceSetting() {
        this.userService.toggleNetPriceView();
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

    showMoreArticles(callback?) {
        const offset = this.categoryLevel.offset + 1;
        this.searchArticle(offset)
            .pipe(finalize(() => {
                if (callback) {
                    callback();
                }
            }))
            .subscribe((response: any) => {
                const articles = get(response, 'articles.content');
                this.hasMoreData = this.isHasMoreData(response);
                this.moreData = articles.map((article: any) => this.getArticle(article));
            });
    }

    onCurrentNetPriceChange() {
        this.userService.toggleNetPriceView();
    }

    onSendArticlesGaData(event) {
        const articles = event;
        this.articlesAnalyticService.sendArticleListGaData(articles, SUB_LIST_NAME_TYPE.CROSS_REFERENCE_ITEMS);
    }

    private searchArticle(offset = 0): Observable<any> {
        this.categoryLevel = new MultiLevelCategory(SEARCH_MODE.CROSS_REFERENCE);
        this.categoryLevel.offset = offset;
        this.categoryLevel.size = 10;
        this.categoryLevel.setCrossReference({ articleNumber: this.root.artnr, dlnrId: this.root.dlnrId });
        return this.freetextArticleService.getMultipleLevelCategory(this.categoryLevel);
    }

    private extractData(response: any) {
        const articles = get(response, 'articles.content', []).map((art) => {
            return this.getArticle(art);
        });
        this.data = [...articles];
        this.moreData = [];
        this.hasMoreData = this.isHasMoreData(response);
        SpinnerService.stop(this.spinner);
    }

    private getArticle(art) {
        const article = new ArticleModel(art);
        article.stockRequested = true;
        return article;
    }

    private isHasMoreData(response) {
        return get(response, 'articles.numberOfElements') > 0 && !get(response, 'articles.last');
    }
}