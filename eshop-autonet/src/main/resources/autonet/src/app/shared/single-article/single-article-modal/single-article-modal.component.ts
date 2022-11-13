import { Component, OnInit, Input, OnDestroy, ChangeDetectionStrategy } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { map, tap, takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ArticleSearchService } from 'src/app/core/services/article-search.service';
import { Subject } from 'rxjs/internal/Subject';
import { Observable } from 'rxjs/internal/Observable';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AutonetCommonService } from '../../autonet-common/autonet-common.service';
import { ArticleBasketModel, ArticleBroadcastKey, ArticleModel } from 'sag-article-detail';
import { BroadcastService } from 'sag-common';
import { SEARCH_MODE } from 'sag-article-list';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { CommonModalService } from '../../autonet-common/services/common-modal.service';

@Component({
    selector: 'autonet-single-article-modal',
    templateUrl: './single-article-modal.component.html',
    styleUrls: ['./single-article-modal.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SingleArticleModalComponent implements OnInit, OnDestroy {

    @Input() articleNumber: string;
    @Input() fromArticleList = false;

    article$: Observable<ArticleModel>;
    errorMessage: string;
    title: string;
    private destroy$ = new Subject();
    constructor(
        private modalService: BsModalService,
        public bsModalRef: BsModalRef,
        private articleSearchService: ArticleSearchService,
        private articleListBroadcastService: BroadcastService,
        private router: Router,
        private autonetCommonService: AutonetCommonService,
        public storage: AppStorageService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        const spinner = SpinnerService.start('modal-container:last-child .modal-content');
        this.article$ = this.articleSearchService.searchArticleByNumber(this.articleNumber)
            .pipe(
                tap(() => SpinnerService.stop(spinner)),
                map(res => {
                    if (res instanceof ArticleModel) {
                        const genArtTxt = (res.genArtTxts || [{ gaid: '', gatxtdech: '' }])[0].gatxtdech || '';
                        this.title = `${res.supplier} - ${genArtTxt} - ${res.artnr}`;
                        return res;
                    }
                    this.errorMessage = res;
                    return null;
                }));

        this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((code: string) => {
                this.router.navigate(['/cars', 'article'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
                setTimeout(() => {
                    this.bsModalRef.hide();
                });
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((vehicleId: string) => {
                this.router.navigate(['/cars', 'vehicle', vehicleId]);
                setTimeout(() => {
                    this.bsModalRef.hide();
                });
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'ADD':
                        if (!this.autonetCommonService.isInArticleListPage) {
                            this.autonetCommonService.addToCart(data.article);
                        }
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
