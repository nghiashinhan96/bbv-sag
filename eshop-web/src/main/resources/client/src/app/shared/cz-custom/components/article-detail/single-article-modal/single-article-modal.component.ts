import { Component, OnInit, Input, OnDestroy, ChangeDetectionStrategy, ViewChild, TemplateRef, ChangeDetectorRef } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { map, tap, takeUntil, finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/internal/Subject';
import { Observable } from 'rxjs/internal/Observable';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { ArticleBasketModel, ArticleBroadcastKey, ArticleModel, CZ_AVAIL_STATE } from 'sag-article-detail';
import { SEARCH_MODE } from 'sag-article-list';
import { AffiliateUtil, BroadcastService } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { MultiLevelCategory } from 'src/app/article-not-context-result-list/models/multi-level-category.model';
import { FreetextArticleService } from 'src/app/article-not-context-result-list/service/freetext-article.service';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';

@Component({
    selector: 'connect-cz-single-article-modal',
    templateUrl: './single-article-modal.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SingleArticleModalComponent implements OnInit, OnDestroy {

    @Input() acticleId: string;

    article$: Observable<ArticleModel>;
    errorMessage: string;
    title: string;
    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    private destroy$ = new Subject();

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    constructor(
        public bsModalRef: BsModalRef,
        private broadcastService: BroadcastService,
        private router: Router,
        public userService: UserService,
        private freetextArticleService: FreetextArticleService,
        private articleBasketService: ArticleShoppingBasketService,
        private cdr: ChangeDetectorRef,
        private appModal: AppModalService,
        private modalService: BsModalService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        const spinner = SpinnerService.start('connect-cz-single-article-modal .modal-body');
        const categoryLevel = new MultiLevelCategory(SEARCH_MODE.ID_SAGSYS);
        categoryLevel.setKeyword(this.acticleId);
        categoryLevel.offset = 0;
        this.article$ = this.freetextArticleService.getMultipleLevelCategory(categoryLevel)
            .pipe(
                finalize(() => SpinnerService.stop(spinner)),
                map((res: any) => {
                    if (res.articles && res.articles.content && res.articles.content.length > 0) {
                        const art = res.articles.content[0];
                        const genArtTxt = (art.genArtTxts || [{ gaid: '', gatxtdech: '' }])[0].gatxtdech || '';
                        this.title = `${art.supplier} - ${genArtTxt} - ${art.artnr}`;
                        return new ArticleModel(art);
                    }
                    this.errorMessage = 'MESSAGES.NO_RESULTS_FOUND';
                    return null;
                }));

        this.broadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((code: string) => {
                this.router.navigate(['/cars', 'article'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
                setTimeout(() => {
                    this.bsModalRef.hide();
                });
            });

        this.broadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((vehicleId: string) => {
                this.router.navigate(['/cars', 'vehicle', vehicleId]);
                setTimeout(() => {
                    this.bsModalRef.hide();
                });
            });

        this.broadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'LOADED':
                        this.articleBasketService.isAddedInCart(data);
                        setTimeout(() => {
                            this.cdr.detectChanges();
                        });
                        break;
                    case 'ADD':
                        if (data.uuid) {
                            SpinnerService.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0, withoutText: true }
                            );
                        }
                        this.articleBasketService.addItemToCart(data);
                        setTimeout(() => {
                            this.cdr.detectChanges();
                        },5000);
                        break;
                }
            });
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
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
}
