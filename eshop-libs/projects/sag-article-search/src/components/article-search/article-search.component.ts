import { Component, OnInit, Output, EventEmitter, Input, ChangeDetectionStrategy, ChangeDetectorRef, TemplateRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs/internal/Observable';
import { LIB_VEHICLE_ARTICLE_NUMBER_SEARCH, LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH } from '../../constant';
import { ArticleSearchService } from '../../services/article-search.service';
import { ArticleNumberSearchRequest } from '../../models/article-number-search-request.model';
import { map } from 'rxjs/internal/operators/map';
import { catchError } from 'rxjs/internal/operators/catchError';
import { ArticleDescriptionSearchRequest } from '../../models/article-description-search-request.model';
import { of } from 'rxjs/internal/observable/of';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { AffiliateUtil, ProjectId } from 'sag-common';

@Component({
    selector: 'sag-search-article',
    templateUrl: './article-search.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SagSearchArticleComponent implements OnInit {
    @Input() isArticleNumberSearch = true;
    @Input() enableValidation = true;
    @Input() customTemplate: TemplateRef<any>;
    @Input() keywords: string;
    @Output() articleSearchEmitter = new EventEmitter();
    @Output() articleSearchEventEmitter = new EventEmitter();

    articleNumberSearchType = LIB_VEHICLE_ARTICLE_NUMBER_SEARCH;
    articleDescSearchType = LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH;

    articleSearchForm: FormGroup;

    PROJECT_ID = ProjectId;
    projectId = ProjectId.CONNECT;

    isSearching = false;
    errorMsg: string;
    exportFocusFn;
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
    isSb = AffiliateUtil.isSb(this.config.affiliate);
    buttonTabIndex = this.getButtonTabIndex();
    private searchType: string;

    constructor(
        private fb: FormBuilder,
        private articleSearchService: ArticleSearchService,
        private cdr: ChangeDetectorRef,
        private config: ArticleSearchConfigService
    ) { }

    ngOnInit() {
        this.projectId = this.config.projectId;
        this.initForm();
        this.exportFocusFn = (unfocusInput: string, searchType: string, tabIndex = 1) => {
            this.onResetUnfocusInput(unfocusInput, searchType, tabIndex);
        };
        this.cdr.detectChanges();
    }

    onSubmit() {
        this.errorMsg = '';
        const searchForm = this.articleSearchForm.value;
        let search;
        const isEmpty = !Object.keys(searchForm).find(k => !!(searchForm[k] || '').trim());
        if (isEmpty) {
            this.errorMsg = 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
            return;
        }
        this.isSearching = true;
        let responseObserver: Observable<any>;
        this.cdr.detectChanges();
        if (this.projectId === ProjectId.AUTONET) {
            this.searchType = LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH;
        }
        switch (this.searchType) {
            case LIB_VEHICLE_ARTICLE_NUMBER_SEARCH:
                search = (searchForm.articleNumber || '').trim();
                responseObserver = this.articleNumberSearch(searchForm);
                break;
            case LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH:
                search = (searchForm.articleDescription || '').trim();
                responseObserver = this.articleDesSearch(searchForm);
                break;
        }
        this.articleSearchEventEmitter.emit({
            type: 'emit',
            searchType: this.searchType,
            search
        });
        responseObserver.subscribe(res => {
            if (!!res) {
                this.articleSearchEmitter.emit({
                    searchType: this.searchType,
                    keywords: search,
                    ...res
                });
            }
            this.isSearching = false;
            this.cdr.detectChanges();
        });
    }

    onResetUnfocusInput(unfocusInput: string, searchType: string, tabIndex) {
        this.searchType = searchType;
        this.articleSearchForm.get(unfocusInput).reset();
        this.buttonTabIndex = this.getButtonTabIndex(tabIndex);
        this.cdr.detectChanges();
    }

    private articleNumberSearch(searchForm) {
        const articleNumber = searchForm.articleNumber;
        if (!this.enableValidation) {
            return of(this.articleNumberSearchReturn(articleNumber, null));
        }
        const request = new ArticleNumberSearchRequest();
        const validatedArtNr = this.validateArticleNumber(articleNumber);
        return this.articleSearchService.searchArticleByNumber(validatedArtNr, request).pipe(
            map((res: any) => {
                if (!res || !res.content || res.content.length === 0) {
                    return this.handleErrorMessage({ error: 404 }, validatedArtNr);
                }
                return this.articleNumberSearchReturn(validatedArtNr, res);
            }),
            catchError(error => of(this.handleErrorMessage(error, validatedArtNr))));
    }

    private articleNumberSearchReturn(articleNumber, response) {
        return {
            analytic: {
                searchTerm: articleNumber,
            },
            search: articleNumber,
            info: articleNumber,
            response
        };
    }

    private articleDesSearch(searchForm) {
        const articleDescription = searchForm.articleDescription;
        if (!this.enableValidation) {
            return of(this.articleDesSearchReturn(articleDescription, null));
        }
        const request = new ArticleDescriptionSearchRequest(searchForm);
        const keyword = this.validateArticleDesc(articleDescription);
        return this.articleSearchService.searchAricleByDescription(request).pipe(
            map((res: any) => {
                if (!res || !res.articles || !res.articles.content || res.articles.content.length === 0) {
                    return this.handleErrorMessage({ error: 404 }, keyword);
                }
                return this.articleDesSearchReturn(keyword, res);
            }),
            catchError(error => of(this.handleErrorMessage(error, keyword))));
    }

    private articleDesSearchReturn(articleDescription: string, response) {
        return {
            analytic: {
                searchTerm: articleDescription,
            },
            search: articleDescription,
            info: articleDescription,
            response: response && response.articles,
            contextKey: response && response.contextKey
        };
    }

    private initForm() {
        this.articleSearchForm = this.fb.group({
            articleNumber: [''],
            articleDescription: [this.keywords || '']
        });
    }

    private validateArticleNumber(input: string) {
        // copy from old app
        return input.trim().replace(/[^A-Za-z0-9 \* säüößéèàùâêîôûëïç]/g, '').replace(/\s+/g, '');
    }

    private validateArticleDesc(input: string) {
        // copy from old app
        return input.trim().replace(/[^[.]-A-Za-z0-9 \* säüößéèàùâêîôûëïç]/g, '').toLowerCase();
    }

    private handleErrorMessage(error, search) {
        // turn off the spinner
        this.isSearching = false;
        let code = error.error_code || error.code || error.status;
        if (!code) {
            code = error.error;
        }
        switch (code) {
            case 404:
            case 400:
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.NOT_FOUND';
                break;
            case 'access_denied':
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.PERMISSION';
                break;
            default:
                this.errorMsg = 'ERROR_500';
        }
        this.articleSearchEventEmitter.emit({
            type: 'error',
            searchType: this.searchType,
            search,
            error: {
                ...error,
                code
            }
        });
        return null;
    }

    private getButtonTabIndex(initValue = null) {
        if (this.isCz || this.isSb) {
            return 13;
        }
        return initValue || 9;
    }
}
