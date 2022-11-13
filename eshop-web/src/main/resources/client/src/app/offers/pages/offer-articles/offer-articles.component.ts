import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';

import { SagTableColumn } from 'sag-table';
import { SagConfirmationBoxComponent, GrossPriceKeyPipe } from 'sag-common';

import { OffersService } from '../../services/offers.services';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Constant } from 'src/app/core/conts/app.constant';
import { ArticleSearchCriteria } from '../../models/article-search-criteria.model';
import { ArticleType } from 'src/app/core/enums/article.enum';
import { OfferArticleFormModalComponent } from '../../components/offer-article-form-modal/offer-article-form-modal.component';
import { OfferArticleModel } from '../../models/offer-article.model';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-offer-articles',
    templateUrl: 'offer-articles.component.html',
    styleUrls: ['offer-articles.component.scss']
})
export class OfferArticlesComponent implements OnInit, OnDestroy {
    titleSub: Subscription;

    readonly spinnerSelector = '.offer-articles-table';

    private currentPage = 0;
    private callback: any;
    columns = [];
    criteria: ArticleSearchCriteria;
    notFoundText = '';
    type: ArticleType;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private activatedRoute: ActivatedRoute,
        private modalService: BsModalService,
        private offersService: OffersService,
        private grossPriceKey: GrossPriceKeyPipe
    ) {
        this.titleSub = this.activatedRoute.data.subscribe(({ title, type }) => {
            this.offersService.title.next(title);
            this.type = type;
        });
    }

    ngOnInit() {
        this.buildColumns();
    }

    ngOnDestroy() {
        this.titleSub.unsubscribe();
    }

    buildColumns() {
        this.columns = [
            {
                id: 'articleNumber',
                i18n: 'COMMON_LABEL.NUMBER',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle'
            },
            {
                id: 'name',
                i18n: 'COMMON_LABEL.NAME',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '200px'
            },
            {
                id: 'description',
                i18n: 'COMMON_LABEL.DESCRIPTION',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '200px'
            },
            {
                id: 'amount',
                i18n: 'COMMON_LABEL.AMOUNT',
                filterable: true,
                sortable: true,
                type: 'currency',
                width: '110px'
            },
            {
                id: 'price',
                i18n: this.grossPriceKey.transform(environment.affiliate),
                filterable: true,
                sortable: true,
                type: 'currency',
                width: '110px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
                cellClass: 'align-middle'
            }
        ] as SagTableColumn[];
    }

    isArticleType(): boolean {
        return this.type === ArticleType.ARTICLE;
    }

    searchTableData(data) {
        this.currentPage = data.request.page - 1;
        this.criteria = data.request.filter;
        this.criteria.type = ArticleType[this.type].toString();
        this.callback = data.callback;

        if (data.request.sort && data.request.sort.field) {
            this.criteria.sort = {
                sortBy: data.request.sort.field,
                direction: data.request.sort.direction
            };
        }

        this.getArticles();
    }

    getArticles() {
        return this.offersService.getOfferArticles(this.currentPage, Constant.DEFAULT_PAGE_SIZE, this.criteria)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe((res) => {
                if (!res) {
                    return;
                }
                if (this.callback) {
                    this.callback({
                        rows: res.articles,
                        totalItems: res.total,
                        itemsPerPage: res.size
                    });
                }
            }, (err) => {
                if (this.callback) {
                    this.callback({
                        rows: [],
                        totalItems: 0
                    });
                }
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
            });
    }

    createArticle() {
        this.modalService.show(OfferArticleFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type: this.type,
                callback: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.getArticles();
                        sub.unsubscribe();
                    });
                }
            }
        });
    }

    editArticle(article: OfferArticleModel) {
        this.modalService.show(OfferArticleFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type: this.type,
                model: article,
                callback: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.getArticles();
                        sub.unsubscribe();
                    });
                }
            }
        });
    }

    deleteArticle(article: OfferArticleModel) {
        this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: '',
                message: this.isArticleType() ? 'OFFERS.OWN_ARTICLE.MESSAGE.DELETE_CONFIRM' : 'OFFERS.OWN_WORK.MESSAGE.DELETE_CONFIRM',
                messageParams: { param: `${article.articleNumber} - ${article.name}` },
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                bodyIcon: 'fa-exclamation-triangle',
                close: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.offersService.deleteOfferArticle(article.id).subscribe(() => {
                            this.getArticles();
                        });
                        sub.unsubscribe();
                    });
                }
            }
        });
    }
}
