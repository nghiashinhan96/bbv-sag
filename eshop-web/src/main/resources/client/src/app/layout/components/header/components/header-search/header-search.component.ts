import { Router } from '@angular/router';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { of, throwError, Observable } from 'rxjs';
import { catchError, finalize, first } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';

import { SubSink } from 'subsink';

import { HeaderSearchService } from '../../services/header-search.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';
import { VehicleSearchRequest } from 'src/app/home/models/vehicle-search-request.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { HeaderSearchTypeEnum } from './header-search-type.enum';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { GA_SEARCH_CATEGORIES } from 'src/app/analytic-logging/enums/ga-search-category.enums';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { ReturnArticle } from 'src/app/article-return/models/return-article.model';
import { APP_RETURN_ARTICLE, APP_HEADER_SEARCH_CHANGE_EVENT } from 'src/app/core/conts/app.constant';
import { SessionStorageService } from 'ngx-webstorage';
import { BroadcastService } from 'sag-common';
import { RelevanceGroupUtil } from 'sag-article-detail';
import { SearchEventTarget, SEARCH_MODE, ArticleGroupUtil } from 'sag-article-list';
import { Constant } from 'src/app/core/conts/app.constant';

import { Validator } from 'src/app/core/utils/validator';
import { ArticleSearchService, ARTICLE_SEARCH_MODE } from 'sag-article-search';
import { ArticleReturnUtils } from 'src/app/article-return/article-return.utils';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { HeaderReturnArticlesModalComponent } from '../header-return-articles-modal/header-return-articles-modal.component';

const EXCLUDED_OPTIONS = [HeaderSearchTypeEnum.ALL, HeaderSearchTypeEnum.BATTERIES];
const SAG_ARTICLE_NR_LENGTH = 10;
const EAN_CODE_13_DIGITS = 13;
const EAN_CODE_12_DIGITS = 12;
const SEARCH_OPTIONS = [{
    label: `SEARCH_TYPE.${HeaderSearchTypeEnum.ALL.toUpperCase()}`,
    value: HeaderSearchTypeEnum.ALL
}, {
    label: `SEARCH_TYPE.${HeaderSearchTypeEnum.ARTICLES.toUpperCase()}`,
    value: HeaderSearchTypeEnum.ARTICLES
}, {
    label: `SEARCH_TYPE.${HeaderSearchTypeEnum.VEHICLES.toUpperCase()}`,
    value: HeaderSearchTypeEnum.VEHICLES
}];
const SEARCH_SALE_OPTIONS = [{
    label: `SEARCH_TYPE.${HeaderSearchTypeEnum.BARCODE.toUpperCase()}`,
    value: HeaderSearchTypeEnum.BARCODE
}, {
    label: `SEARCH_TYPE.${HeaderSearchTypeEnum.RETURN.toUpperCase()}`,
    value: HeaderSearchTypeEnum.RETURN
}];

@Component({
    selector: 'connect-header-search',
    templateUrl: './header-search.component.html',
    styleUrls: ['./header-search.component.scss']
})
export class HeaderSearchComponent implements OnInit, OnDestroy {

    search: FormGroup;
    items = [];
    searchEnum = HeaderSearchTypeEnum;
    result: {
        type: any;
        content: any;
        label: string;
        total: number;
        search?: any;
    }[] = [];
    eventData: any;

    @ViewChild('dashboard', { static: true }) dashboardPopup: PopoverDirective;
    @ViewChild('pop', { static: true }) resultPopup: PopoverDirective;
    @ViewChild('searchControl', { static: true }) searchControl: ElementRef;
    outsideClick = false;
    isShownDashboard = false;
    isShownResult = false;
    isSearching = false;
    hasWspPermisson = false;
    wspItems = [];

    error: {
        type: string,
        subType?: string,
        text: string
    } = null;
    returnArticles;
    showMoreProductCategory = false;
    contextKey = '';
    enhancedUsedPartsReturnProcEnabled = false;

    private subs = new SubSink();
    constructor(
        private fb: FormBuilder,
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService,
        private searchService: HeaderSearchService,
        private router: Router,
        private appStorage: AppStorageService,
        private userService: UserService,
        private sessionStorage: SessionStorageService,
        private broadcaster: BroadcastService,
        private articleSearchService: ArticleSearchService,
        private bsModalService: BsModalService,
        private appModals: AppModalService
    ) { }

    get term() { return this.search.get('term') as FormControl; }

    ngOnInit() {
        this.subs.sink = this.broadcaster.on(APP_HEADER_SEARCH_CHANGE_EVENT).subscribe(res => {
            this.search.get('type').setValue(res);
            this.focusSearchInput();
        });

        this.search = this.fb.group({
            term: ['', Validators.required],
            type: HeaderSearchTypeEnum.ALL
        });
        this.subs.sink = this.search.controls.term.valueChanges.subscribe(term => {
            if (term && this.isShownDashboard) {
                this.dashboardPopup.hide();
            }
        })
        this.subs.sink = this.resultPopup.onHidden.subscribe(res => {
            if (this.isShownDashboard) {
                this.isShownDashboard = false;
                return;
            }
            this.outsideClick = true;
            this.isShownResult = false;
            this.error = null;
            this.returnArticles = null;
            this.result = null;
        });
        this.subs.sink = this.resultPopup.onShown.subscribe(res => {
            if (this.isShownDashboard) {
                return;
            }
            this.isShownResult = true;
            if (this.returnArticles && this.returnArticles.length > 0) {
                document.querySelector('popover-container.header-search-result').classList.add('return-article-mode');
            }
        });
        this.subs.sink = this.appStorage.fastScanArt$.subscribe(res => {
            if (res === null) {
                // fastscan done added
                this.isSearching = false;
                this.search.enable();
                this.search.get('term').reset();
                this.focusSearchInput();
            }
        });
        this.subs.sink = this.userService.userDetail$.subscribe((userDetail: UserDetail) => {
            if (userDetail) {
                this.enhancedUsedPartsReturnProcEnabled = userDetail.settings && userDetail.settings.enhancedUsedPartsReturnProcEnabled;
                this.items = [...SEARCH_OPTIONS];

                if (userDetail.isSalesOnBeHalf) {
                    this.items = [...this.items, ...SEARCH_SALE_OPTIONS];
                    const isShopCustomer = this.userService.userDetail.isShopCustomer;
                    let validators = [];

                    if (isShopCustomer) {
                        this.search.reset({
                            term: '',
                            type: HeaderSearchTypeEnum.BARCODE
                        });

                        if(this.term) {
                            validators = [Validator.barcodeValidator];
                        }

                        setTimeout(() => {
                            this.focusSearchInput();
                        }, 0);

                    } else {
                        this.search.reset({
                            term: '',
                            type: HeaderSearchTypeEnum.ALL
                        });

                        validators = [];
                    }

                    this.term.setValidators(validators);
                }
            }
        });

        this.subs.sink = this.userService.hasPermission(Constant.PERMISSIONS.UNIPARTS)
            .subscribe(per => this.hasWspPermisson = per);
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    showMore() {
        this.showMoreProductCategory = !this.showMoreProductCategory;
    }

    doSearch(event?) {
        if (event) {
            event.preventDefault();
        }
        const data = this.search.value;

        if (this.search.invalid) {
            if(data.type === HeaderSearchTypeEnum.BARCODE) {
                this.error = {
                    type: HeaderSearchTypeEnum.BARCODE,
                    text: data.term && data.term.trim() || ''
                };

                this.resultPopup.show();
            }
            return;
        }

        if (!data.term.trim()) {
            return;
        }
        this.showMoreProductCategory = false;
        this.search.disable();
        this.focusSearchInput();
        const type = data.type;
        switch (type) {
            case HeaderSearchTypeEnum.BARCODE:
                this.searchBarcode(data);
                break;
            case HeaderSearchTypeEnum.RETURN:
                this.searchReturnArticles(data);
                break;
            default: this.otherSearch(data);
        }
    }

    searchBarcode(data) {
        const term = data.term.trim();
        let searchArticleByBarCode$: Observable<any>;
        if (!this.isValidBarCode(term)) {
            searchArticleByBarCode$ = throwError(400);
        } else {
            searchArticleByBarCode$ = this.searchService.searchArticleByBarCode(term);
        }
        this.startSearch();
        searchArticleByBarCode$.pipe(
            catchError(err => {
                return of(null);
            })
        ).subscribe(response => {
            if (!response) {
                data.type = HeaderSearchTypeEnum.ALL;
                this.otherSearch(data, true);
            } else {
                this.handleSearchByBarcodeResponse(data, response, () => this.callbackAfterHandleSearchByBarcodeResponse());
            }
        });

    }

    onViewReturnArticleList(returnArticle: ReturnArticle, attachedArticle?: ReturnArticle, redirect = true) {
        if (attachedArticle && !attachedArticle.editable) {
            return;
        }

        const addedAricle = attachedArticle && attachedArticle.editable ? attachedArticle : returnArticle;
        let tempReturnList = this.sessionStorage.retrieve(APP_RETURN_ARTICLE) || [];
        if (tempReturnList.length === 0) {
            const attached = (addedAricle.attachedTransactionReferences || []).filter(item => item.editable);
            if (attached && attached.length > 0) {
                delete addedAricle.attachedTransactionReferences;
                tempReturnList = [...tempReturnList, ...attached];
            }
            tempReturnList.push(addedAricle);
        } else {
            const isExistedArticleInBasket = tempReturnList.find(
                article => article.articleId === addedAricle.articleId && article.orderNr === addedAricle.orderNr);
            if (!isExistedArticleInBasket) {
                let uneditable = [];
                let editable = [];
                const attachedList = addedAricle.attachedTransactionReferences || [];
                if (attachedList && attachedList.length > 0) {
                    uneditable = attachedList.filter(item => !item.editable);
                    editable = attachedList.filter(item => item.editable);
                }
                // Remove the attached article if it was added previously
                if (uneditable && uneditable.length > 0) {
                    const tempReturnListCloned = [...tempReturnList];
                    addedAricle.attachedTransactionReferences = uneditable.filter((dataInAttach) => {
                        const existed = tempReturnListCloned.find((dataInTemp) => {
                            return dataInTemp.articleId !== dataInAttach.articleId &&
                                dataInTemp.orderNr !== dataInAttach.orderNr;
                        });
                        return !!existed;
                    });
                }
                if (editable && editable.length > 0) {
                    delete addedAricle.attachedTransactionReferences;
                    tempReturnList = [...tempReturnList, ...editable];
                }
                tempReturnList.push(addedAricle);
            }
        }
        this.sessionStorage.store(APP_RETURN_ARTICLE, [...tempReturnList]);
        if (redirect) {
            this.router.navigateByUrl('/return');
        }
    }

    searchReturnArticles({ term }) {
        this.startSearch();
        this.searchService.searchReturnArticles(term).pipe(
            catchError(err => {
                this.error = {
                    type: HeaderSearchTypeEnum.RETURN,
                    subType: 'ERROR',
                    text: term
                };
                return of(null);
            })
        ).subscribe((res: ReturnArticle[]) => {
            this.handleReturnArticleResponse(res, () => {
                this.isSearching = false;
                this.search.enable();
                this.search.get('term').reset();
                this.focusSearchInput();
            });
        });
    }

    clearValue() {
        this.search.controls['term'].reset();
    }

    showDashboard() {
        const data = this.search.value;
        const { term, type } = data;
        if (term) {
            return;
        }
        switch (type) {
            case HeaderSearchTypeEnum.ALL:
            case HeaderSearchTypeEnum.ARTICLES:
                this.isShownDashboard = true;
                this.outsideClick = true;
                this.dashboardPopup.show();
                break;
        }
    }

    private handleReturnArticleResponse(articles: ReturnArticle[], done: () => void) {
        if (articles && articles.length === 0) {
            this.error = {
                type: HeaderSearchTypeEnum.RETURN,
                subType: 'WARNING',
                text: ''
            };
        }
        if (!articles || articles.length === 0) {
            done();
            this.resultPopup.show();
            return;
        }
        if (articles.length === 1) {
            done();
            this.onViewReturnArticleList(articles[0]);
        } else {
            if (articles.length > 10) {
                this.appModals.modals = this.bsModalService.show(HeaderReturnArticlesModalComponent, {
                    initialState: {
                        term: this.search.get('term').value || '',
                        articles,
                        onSelectItem: (returnArticle: ReturnArticle, attachedArticle?: ReturnArticle, redirect = false) => {
                            this.onViewReturnArticleList(returnArticle, attachedArticle, redirect);
                        }
                    },
                    class: 'modal-lg',
                    ignoreBackdropClick: true
                })
            } else {
                this.returnArticles = articles;
                this.resultPopup.show();
            }
            done();
        }
    }

    private otherSearch(data, isFallback = false) {
        const body = {
            options: this.getSearchOptions(data.type),
            keyword: data.term.trim(),
            fullrequest: false,
            page: 0,
            genericSearch: true
        };
        this.isSearching = true;
        this.searchService.searchFreetext(body)
            .pipe(
                finalize(() => this.isSearching = false),
                catchError(err => {
                    return of({});
                })
            ).subscribe((res: any) => {
                this.result = [];
                let numberOfHits;

                if (isFallback) {
                    if (res.articles.content.length > 0) {
                        const articleId = res.articles.content[0].id_pim;
                        this.subs.sink = this.searchService.getArticleDataFromId(articleId).subscribe(articleData => {
                            if (articleData) {
                                res.articles.content[0] = articleData;
                                this.handleSearchByBarcodeResponse(data, res.articles, () => this.callbackAfterHandleSearchByBarcodeResponse());
                            };
                        });
                    } else {
                        this.handleSearchByBarcodeResponse(data, res.articles, () => this.callbackAfterHandleSearchByBarcodeResponse());
                    }
                } else {
                    if (res.vehiclePresent) {
                        const content = res.vehData.vehicles.content || [];
                        numberOfHits = res.vehData.vehicles.totalElements;
                        content.forEach(article => {
                            article.displayedText = article.vehDisplay;
                        });
                        this.result.push({
                            label: `SEARCH_TYPE.${HeaderSearchTypeEnum.VEHICLES.toUpperCase()}`,
                            content: [{
                                values: content
                            }],
                            total: res.vehData.vehicles.totalElements,
                            type: HeaderSearchTypeEnum.VEHICLES,
                            search: data
                        });
                    }

                    if (res.articlePresent) {
                        const content = res.articles.content || [];
                        numberOfHits = res.articles.totalElements;
                        content.forEach(article => {
                            article.displayedText = article.freetextDisplayDesc;
                            article.productAddon = article.product_addon || '';
                            article.relevanceGroupOrder = RelevanceGroupUtil.getRelevanceGroupOrder(article.relevanceGroupType);
                        });
                        this.result.unshift({
                            label: `SEARCH_TYPE.${HeaderSearchTypeEnum.ARTICLES.toUpperCase()}`,
                            content: ArticleGroupUtil.groupByRelevance(content),
                            total: res.articles.totalElements,
                            type: HeaderSearchTypeEnum.ARTICLES,
                            search: data
                        });
                        this.contextKey = res.contextKey || '';
                    }

                    if (res.unitreePresent) {
                        const content = res.unitreeData.unitrees.content || [];
                        numberOfHits = numberOfHits + res.unitreeData.unitrees.totalElements;

                        content.forEach(node => {
                            node.displayedText = node.nodeName;
                        });

                        this.wspItems = content;

                        if (!res.articlePresent) {
                            this.result.push({
                                label: `SEARCH_TYPE.${HeaderSearchTypeEnum.ARTICLES.toUpperCase()}`,
                                content: [],
                                total: res.articles.totalElements,
                                type: HeaderSearchTypeEnum.ARTICLES
                            });
                        }
                    } else {
                        this.wspItems = [];
                    }


                    if (this.result.length === 0) {
                        this.gaService.sendNoResultFound(body.keyword);
                    }
                    this.eventData = {
                        ftsFilterSelected: (data.type || '').toUpperCase(),
                        ftsSearchTermEntered: body.keyword,
                        ftsNumberOfHits: numberOfHits
                    };
                    this.sendEvent(this.eventData);

                    this.resultPopup.show();
                    this.search.enable();
                }
            }, () => this.gaService.sendNoResultFound(body.keyword));
    }


    goToDetail(event, item, type: string, index: number, searchData = null) {
        event.preventDefault();
        event.stopPropagation();
        const searchTerm = (searchData && searchData.term || '').trim();
        let eventData;
        let isArticle = false;
        const isShowMore = !item;

        switch (type) {
            case HeaderSearchTypeEnum.PRODUCT_CATEGORY:
                eventData = {
                    ftsHitListPosition: index + 1,
                    ftsNameClicked: item && item.freetextDisplayDesc,
                    ftsArticleIdClicked: item && item.treeId
                };

                this.router.navigate(['wsp'], {
                    queryParams: {
                        treeId: item.treeId,
                        nodeId: item.nodeId,
                        target: SearchEventTarget.FREE_TEXT_SEARCH_RESULTS
                    }
                });

                break;
            case HeaderSearchTypeEnum.ARTICLES:
                isArticle = true;
                const artid = item && item.id_pim;
                const artDisplay = item && item.artnr_display;
                eventData = {
                    ftsHitListPosition: index + 1,
                    ftsNameClicked: item && item.freetextDisplayDesc,
                    ftsArticleIdClicked: artid
                };
                const searcArticlehValue = artid || searchTerm;
                const mode = artid ? SEARCH_MODE.ID_SAGSYS : SEARCH_MODE.FREE_TEXT;
                const info = {
                    articleId: artid,
                    searchMode: artid ? ARTICLE_SEARCH_MODE.ARTICLE_ID : ARTICLE_SEARCH_MODE.FREE_TEXT,
                    searchTerm: JSON.stringify([type, searchTerm, artDisplay].filter(item => item)),
                    rawSearchTerm: searchTerm,
                };
                this.articleSearchService.addHistory(info).subscribe();
                this.router.navigateByUrl(`article/result?type=${mode}&articleId=${encodeURIComponent(searcArticlehValue)}&keywords=${encodeURIComponent(searchTerm)}&contextKey=${this.contextKey}`);
                break;
            case HeaderSearchTypeEnum.VEHICLES:
                eventData = {
                    ftsHitListPosition: index + 1,
                    ftsVehicleNameClicked: item && item.vehDisplay,
                    ftsVehicleClicked: item && item.vehId
                };
                const searchVehicleValue = item && item.vehId;
                if (searchVehicleValue) {
                    this.router.navigate(['vehicle', searchVehicleValue, 'quick-click'],
                        { queryParams: { keywords: searchTerm, searchMode: SEARCH_MODE.FREE_TEXT } });
                } else {
                    const search = new VehicleSearchRequest().buildVehicleDescriptionRequest({ vehicleName: searchTerm });
                    this.appStorage.vehicleFilter = JSON.stringify(search);
                    this.router.navigateByUrl(`vehicle-filtering`);
                }
                break;
        }
        this.resultPopup.hide();
        this.sendEvent({ ...this.eventData, ...eventData }, { isSelectedItemEvent: true, isArticle, isShowMore });
    }
    changeSearchOption(evt) {
        const type = evt && evt.value || '';
        if(!!type) {
            if(type === HeaderSearchTypeEnum.BARCODE) {
                this.term.setValidators([Validator.barcodeValidator]);
            } else {
                this.term.setValidators([]);
            }
            if (this.isShownDashboard && type !== HeaderSearchTypeEnum.ALL && type !== HeaderSearchTypeEnum.ARTICLES) {
                this.dashboardPopup.hide();
            }
        }
    }

    private focusSearchInput() {
        this.searchControl.nativeElement.focus();
    }

    private getSearchOptions(type: string) {
        if (type === HeaderSearchTypeEnum.ALL) {
            const options = this.items.filter(item => EXCLUDED_OPTIONS.indexOf(item.value) === -1).map(item => item.value);

            if(this.hasWspPermisson) {
                options.push(HeaderSearchTypeEnum.PRODUCT_CATEGORY);
            }

            return options;
        } else {
            return [type];
        }
    }

    private isValidBarCode(keySearch: string) {
        // According to #2679, the value of barcode is variety in character and length, so it no
        // longer possible to pre-validate the length or character types/ formats
        return true;
    }

    private handleSearchByBarcodeResponse(searchData, response: any, done: () => void) {
        if (!response) {
            done();
            this.resultPopup.show();
            return;
        }
        if (response && response.content && response.totalElements === 1) {
            this.router.navigate(['shopping-basket']).then(sucess => {
                if (sucess) {
                    this.appStorage.fastscanArt = response.content[0];
                }
            });
        } else {
            done();
            const mode = SEARCH_MODE.BAR_CODE;
            const searcArticlehValue = searchData.term.trim();
            this.router.navigateByUrl(`article/result?type=${mode}&code=${searcArticlehValue}`);
        }
        this.eventData = {
            ftsFilterSelected: HeaderSearchTypeEnum.FASTSCAN_ARTICLES,
            ftsSearchTermEntered: searchData.term.trim(),
            ftsNumberOfHits: response.totalElements
        };
        this.sendEvent(this.eventData);
    }

    private sendEvent(data, extras?: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.FULL_TEXT_SEARCH_EVENT;

            const request = this.analyticService.createFullTextSearchEventData(data, extras || {});
            if (request) {
                // GA4 Search event
                const sourceId = request.basket_item_source_id || '';
                const sourceDesc = request.basket_item_source_desc || '';
                const searchTerm = data.ftsSearchTermEntered || request.fts_search_term_entered || request.fts_vehicle_clicked || '';
                this.gaService.search('', searchTerm, sourceId, sourceDesc);

                // Json event
                this.analyticService.postEventFulltextSearch(request, eventType)
                    .pipe(first())
                    .toPromise();
            }
        });
    }

    private startSearch() {
        this.resultPopup.hide();
        this.isSearching = true;
        this.error = null;
        this.returnArticles = null;
        this.result = null;
    }

    private callbackAfterHandleSearchByBarcodeResponse() {
        this.isSearching = false;
        this.search.enable();
        this.search.get('term').reset();
        this.focusSearchInput();
    }
}
