import { Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from "rxjs";
import { get } from 'lodash';
import { ArticleBasketModel, ArticleBroadcastKey, ArticleModel, CZ_AVAIL_STATE } from "sag-article-detail";
import { ARTICLE_LIST_TYPE, SEARCH_MODE } from "sag-article-list";
import { AffiliateUtil, BroadcastService } from "sag-common";
import { MultiLevelCategory } from "src/app/article-not-context-result-list/models/multi-level-category.model";
import { FreetextArticleService } from "src/app/article-not-context-result-list/service/freetext-article.service";
import { AppStorageService } from "src/app/core/services/app-storage.service";
import { UserService } from "src/app/core/services/user.service";
import { CZ_PRICE_TYPE } from "src/app/shared/cz-custom/enums/article.enums";
import { environment } from "src/environments/environment";
import { SubSink } from "subsink";
import { finalize, map } from "rxjs/operators";
import { SpinnerService } from "src/app/core/utils/spinner";
import { ArticleShoppingBasketService } from "src/app/core/services/article-shopping-basket.service";
import { ShoppingBasketService } from "src/app/core/services/shopping-basket.service";
import { SHOPPING_BASKET_ENUM } from "src/app/core/enums/shopping-basket.enum";
import { ActivatedRoute, Router } from "@angular/router";
import { SHOPPING_BASKET_PAGE } from "src/app/core/conts/app.constant";
import { Validator } from "src/app/core/utils/validator";
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { CommonModalService } from "../../services/common-modal.service";
import { ArticlesAnalyticService } from "src/app/analytic-logging/services/articles-analytic.service";
import { SUB_LIST_NAME_TYPE } from "src/app/analytic-logging/enums/event-type.enums";

@Component({
    selector: 'connect-article-replace-modal',
    templateUrl: './article-replace-modal.component.html'
})
export class ArticleReplaceModalComponent implements OnInit, OnDestroy {
    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    @Input() article: ArticleModel;

    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
    rootModalName = SUB_LIST_NAME_TYPE.REPLACED_BY_ITEMS;
    
    data: any;
    isSubBasket = false;

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
        private commonModalService: CommonModalService,
        private articlesAnalyticService: ArticlesAnalyticService
    ) {

    }

    ngOnInit() {
        const spinner = SpinnerService.start('modal-container:last-child connect-article-replace-modal .modal-body');
        this.subs.sink = this.searchArticle()
            .pipe(
                map(res => this.extractData(res)),
                finalize(() => SpinnerService.stop(spinner))
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

    onSendArticlesGaData(event) {
        const articles = event;
        this.articlesAnalyticService.sendArticleListGaData(articles, SUB_LIST_NAME_TYPE.REPLACED_BY_ITEMS);
    }

    private searchArticle(): Observable<any> {
        const categoryLevel = new MultiLevelCategory(SEARCH_MODE.ONLY_ARTICLE_NUMBER_AND_SUPPLIER);
        categoryLevel.setKeyword(this.article.hasReplacement);
        categoryLevel.offset = 0;
        return this.freetextArticleService.getMultipleLevelCategory(categoryLevel);
    }

    private extractData(response: any) {
        const articles = get(response, 'articles.content');

        this.data = (articles || [])
            .map((article: any) => new ArticleModel({ ...article, replacementForArtId: this.article.artid, replacementInContextData: this.article.replacementInContextData, stockRequested: true }))
            .filter((art: ArticleModel) => this.normalizeArtnr(art.artnr) === this.normalizeArtnr(this.article.hasReplacement))
            .filter((art: ArticleModel) => {
                const replaceForArtIds = (art.isReplacementFor || '').split(',');
                return replaceForArtIds.some(id => this.normalizeArtnr(id) === this.normalizeArtnr(this.article.artnr));
            });
    }

    private normalizeArtnr(artnr) {
        return Validator.removeWhiteSpace(Validator.removeNonAlphaCharacter(artnr || '')).toLowerCase();
    }
}