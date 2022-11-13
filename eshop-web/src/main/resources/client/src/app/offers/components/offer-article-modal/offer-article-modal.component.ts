import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';

import { finalize } from 'rxjs/operators';
import { BsModalRef } from 'ngx-bootstrap/modal';

import { SagTableColumn, SagTableControl } from 'sag-table';
import { GrossPriceKeyPipe } from 'sag-common';

import { ArticleType } from 'src/app/core/enums/article.enum';
import { OfferArticleModel } from '../../models/offer-article.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { ArticleSearchCriteria } from '../../models/article-search-criteria.model';
import { OffersService } from '../../services/offers.services';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-offer-article-modal',
    templateUrl: './offer-article-modal.component.html',
    styleUrls: ['./offer-article-modal.component.scss']
})

export class OfferArticleModalComponent implements OnInit, SagTableControl {

    type: ArticleType;
    createArticle: any;
    selectArticles: any;

    spinnerSelector = '.modal-content';

    filterModels: ArticleSearchCriteria;

    public articles: OfferArticleModel[] = [];
    public selectedArticles: OfferArticleModel[] = [];

    public isArticle: boolean;
    public warningMessage: string;

    columns = [];
    @ViewChild('colCheckboxAll', { static: true }) colCheckboxAll: TemplateRef<any>;
    @ViewChild('colCheckbox', { static: true }) colCheckbox: TemplateRef<any>;

    constructor(
        public modalRef: BsModalRef,
        private offersService: OffersService,
        private grossPriceKey: GrossPriceKeyPipe
    ) {
    }

    ngOnInit(): void {
        this.isArticle = this.isArticleType();
        this.buildColumns();
    }

    buildColumns() {
        this.columns = [
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                headerTemplate: this.colCheckboxAll,
                cellTemplate: this.colCheckbox,
                class: 'align-middle py-0',
                cellClass: 'align-middle py-0'
            },
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
            }
        ] as SagTableColumn[];
    }

    takeArticlesForOffer() {
        const selectedArticles = [...this.selectedArticles];
        this.selectedArticles = [];
        this.selectArticles(selectedArticles);
        this.modalRef.hide();
    }

    isArticleType(): boolean {
        return this.type === ArticleType.ARTICLE;
    }

    searchTableData(data) {
        SpinnerService.start(this.spinnerSelector);
        this.filterModels = data.request.filter;
        this.filterModels.type = ArticleType[this.type].toString();

        if (data.request.sort && data.request.sort.field) {
            this.filterModels.sort = {
                sortBy: data.request.sort.field,
                direction: data.request.sort.direction
            };
        }

        this.offersService.getOfferArticles(data.request.page - 1, Constant.DEFAULT_PAGE_SIZE, this.filterModels)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                if (!res) {
                    return;
                }

                this.articles = res.articles;

                if (data.callback) {
                    data.callback({
                        rows: res.articles,
                        totalItems: res.total,
                        itemsPerPage: res.size
                    });
                }
            }, err => {
                if (data.callback) {
                    data.callback({
                        rows: [],
                        totalItems: 0
                    });
                }
                if (err && err.error && err.error.code === Constant.NOT_FOUND_CODE) {
                    if (this.isArticleType()) {
                        this.warningMessage = 'OFFERS.OWN_ARTICLE.MESSAGE.NOT_FOUND_ARTICLES';
                    } else {
                        this.warningMessage = 'OFFERS.OWN_WORK.MESSAGE.NOT_FOUND_WORKS';
                    }
                } else {
                    this.warningMessage = 'MESSAGES.GENERAL_ERROR';
                }
            });
    }

    selectArticle(event, article: OfferArticleModel) {
        if (event.target.checked) {
            this.selectedArticles.push(article);
        } else {
            this.selectedArticles = this.selectedArticles.filter(item => item.id !== article.id);
        }
    }

    selectAllArticles(event) {
        if (event.target.checked) {
            this.selectedArticles = [...this.articles];
        } else {
            this.selectedArticles = [];
        }
    }

    isArticleSelected(article: OfferArticleModel) {
        return this.selectedArticles.some(item => item.id === article.id);
    }

    isAllArticlesSelected() {
        return (this.articles.length > 0 && this.articles.length === this.selectedArticles.length);
    }

    showCreatingPersonModal() {
        this.createArticle();
        this.modalRef.hide();
    }
}
