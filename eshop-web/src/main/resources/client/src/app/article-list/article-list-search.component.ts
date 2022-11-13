import { Component, OnDestroy, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';

import { BsModalService } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';
import { SagConfirmationBoxComponent, BroadcastService } from 'sag-common';
import { ArticleBroadcastKey, FAVORITE_BROADCAST_EVENT } from 'sag-article-detail';
import { SEARCH_MODE, ARTICLE_LIST_TYPE, ArticleSort } from 'sag-article-list';
import { CreditLimitService } from '../core/services/credit-limit.service';
import { UserService } from '../core/services/user.service';
import { ImportArticlesModalComponent } from './components/import-articles-modal/import-articles-modal.component';
import { ArticleImportType } from './enums/article-import-type.enum';
import { SearchHistory } from './models/search-history.model';
import { ArticleListSearchStorageService } from './services/article-list-storage.service';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { ArticleModel } from 'sag-article-detail';
import { ArticleShoppingBasketService } from '../core/services/article-shopping-basket.service';
import { AppStorageService } from '../core/services/app-storage.service';
import * as XLSX from 'xlsx';
import { Subject } from 'rxjs';
import { ArticleSearchService, ARTICLE_SEARCH_MODE } from 'sag-article-search';
import { DmsInfo } from '../dms/models/dms-info.model';
import { ActiveDmsProcessor } from '../dms/context/active-dms-processor';
import { ArticlesAnalyticService } from '../analytic-logging/services/articles-analytic.service';
import { GoogleAnalyticsService } from '../analytic-logging/services/google-analytics.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
@Component({
    selector: 'connect-article-list-search',
    templateUrl: 'article-list-search.component.html'
})
export class ArticleListSearchComponent implements OnInit, OnDestroy {
    @Input() isSubBasket = false;
    searchHistories: SearchHistory[] = [];
    searchQueue = [];
    isMultiSearch = false;
    creditInfo: any;
    form: FormGroup;
    @ViewChild('searchTerm', { static: false }) searchTerm: ElementRef;
    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;
    dmsInfo: DmsInfo;

    isSelectedAll = {
        status: false,
        isHeaderChecked: false
    };

    sortArticle = new Subject<ArticleSort>();

    isSb = AffiliateUtil.isSb(environment.affiliate);

    private isSearching = false;
    private subs = new SubSink();

    constructor(
        private fb: FormBuilder,
        private bsModalService: BsModalService,
        public userService: UserService,
        public creditLimitService: CreditLimitService,
        private articleListSearchStorageService: ArticleListSearchStorageService,
        private articleListBroadcastService: BroadcastService,
        private router: Router,
        private articleBasketService: ArticleShoppingBasketService,
        public appStorage: AppStorageService,
        private articleSearchService: ArticleSearchService,
        private dmsService: ActiveDmsProcessor,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService
    ) { }

    ngOnInit() {
        this.form = this.fb.group({
            searchTerm: ''
        });
        this.articleBasketService.observeArticleRemove(cartKey => {
            this.searchHistories = this.searchHistories.filter(item => {
                return !(item.cartKeys || []).some(k => k === cartKey);
            });
            this.saveHistoryToStorage(this.searchHistories);
        });
        this.subs.sink = this.creditLimitService.creditCardInfo$
            .subscribe(creditInfo => {
                this.creditInfo = creditInfo || {};
            });

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe((code: string) => {
                this.router.navigate(['article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
            });

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .subscribe((vehicleId: string) => {
                if (vehicleId) {
                    this.router.navigate(['vehicle', vehicleId]);
                }
            });

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL)
            .subscribe((data: any) => {
                this.articlesAnalyticService.sendArticleListEventData(data);
                this.gaService.viewProductDetails(data.article, data.rootModalName);
            });

        this.loadHistoryFromStorage();

        this.dmsInfo = this.dmsService.getDmsInfo();

        this.importFromDms();
        this.handleUpdateFavoriteItem();
        this.subs.sink = this.router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                if (event.url.indexOf('/article-list') === -1) {
                    this.appStorage.clearBasketItemSource();
                }
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    handleUpdateFavoriteItem() {
        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.FAVORITE_ITEM_EVENT).subscribe((data: any) => {
            switch (data.action) {
              case FAVORITE_BROADCAST_EVENT.UPDATE_SEARCH_ARTICLE_HISTORY: {
                const article = data && data.article || null;

                if(this.searchHistories && this.searchHistories.length > 0) {
                    this.searchHistories.forEach(item => {
                        (item.articles || []).forEach(art => {
                            if(article && art.artid === article.artid) {
                                art.favorite = article.favorite;
                                art.favoriteComment = article.favoriteComment;
                            }
                        });
                    });

                    this.saveHistoryToStorage(this.searchHistories);
                }

                break;
              }
            }
          });
    }

    get searchTermControl() {
        return this.form.get('searchTerm') as FormControl;
    }

    handleInputTab(event) {
        const searchTerm = (this.searchTermControl.value || '').trim();
        if (this.isSearching || !searchTerm) {
            event.preventDefault();
            return;
        }
        this.searchHistories.push({
            searchTerm,
            isHadResult: true
        });
        this.searchTermControl.setValue('');
        setTimeout(() => {
            this.searchTerm.nativeElement.focus();
        });
    }

    onSearch() {
        this.isSearching = true;
        const searchTerm = (this.searchTermControl.value || '').trim();
        this.isMultiSearch = false;
        this.searchHistories.push({ searchTerm, emitSearch: true, isShoppingList: true });
        this.saveHistoryToStorage(this.searchHistories);
        this.searchTerm.nativeElement.focus();
        const info = {
            searchTerm,
            rawSearchTerm: searchTerm,
            searchMode: ARTICLE_SEARCH_MODE.SHOPPING_LIST
        };
        this.articleSearchService.addHistory(info).subscribe();
    }

    clearSearchedList() {
        this.searchHistories = [];
        this.searchQueue = [];
        this.saveHistoryToStorage(this.searchHistories);
    }

    processImport() {
        if (this.searchHistories && this.searchHistories.length) {
            this.openClearSearchHistoryModal();
        } else {
            this.openImportArticlesModal();
        }
    }

    onDataChange(data) {
        const histories = [];
        this.searchHistories.forEach((item, index) => {
            let history: SearchHistory;
            if (data.index === index) {
                this.searchHistories[index] = item;
            } else {
                // If this is not search by import or not have at least 1 item added to basket through this list
                // then it wont save to searchHistory
                if (!this.isMultiSearch) {
                    if (!item.isAddedToCart) {
                        this.searchHistories[index].articles = [];
                        this.searchHistories[index].filter = null;
                        this.searchHistories[index].isHadResult = true;
                    }
                } else {
                    // If this is search by import then we not need to resubmit search
                    this.searchHistories[index].emitSearch = false;
                }
            }
            history = { ...item, filter: null };
            if (!history.isAddedToCart) {
                history.articles = [];
            }
            if (history.articles && history.articles.length === 0) {
                history.message = null;
            }
            histories.push(history);
        });
        if (!data.searchData) {
            this.searchHistories.splice(data.index, 1);
            histories.splice(data.index, 1);
        }
        this.saveHistoryToStorage(histories);
        this.searchTermControl.setValue('');
        this.isSearching = false;
        this.searchInQueue();
    }

    searchInQueue() {
        if (!this.isMultiSearch || this.searchQueue.length === 0) {
            return;
        }

        const topItem = this.searchQueue.shift();
        this.searchHistories.push({
            searchTerm: topItem.description,
            amount: topItem.amount,
            emitSearch: true,
            isImported: true,
            isImportedFromFile: this.dmsInfo ? false : true
        });

    }

    toggleNetPriceSetting() {
        this.userService.toggleNetPriceView();
    }

    onSelectFavoriteItem(item: any) {
        this.searchHistories.push({ searchTerm: item.articleId, emitSearch: true, searchType: SEARCH_MODE.ID_SAGSYS });
        this.saveHistoryToStorage(this.searchHistories);
    }

    private openClearSearchHistoryModal() {
        this.bsModalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                title: 'ARTICLE_SEARCH_LIST.IMPORT_ARTICLE_LIST',
                message: 'ARTICLE_SEARCH_LIST.CLEAR_CART_COMFIRM_MESSAGE',
                okButton: 'ARTICLE_SEARCH_LIST.CONTINUE',
                showHeaderIcon: false,
                showCloseButton: true,
                close: () => {
                    this.clearSearchedList();
                    this.openImportArticlesModal();
                }
            }
        });
    }

    private openImportArticlesModal() {
        this.bsModalService.show(ImportArticlesModalComponent, {
            class: 'modal-md',
            ignoreBackdropClick: true,
            initialState: {
                onImported: (fileUpload: File, importFileType: ArticleImportType) => {
                    if (importFileType === ArticleImportType.CSV || importFileType === ArticleImportType.TXT) {
                        this.uploadFile(fileUpload);
                    } else if (importFileType === ArticleImportType.EXCEL) {
                        this.uploadFileExcel(fileUpload);
                    }
                }
            }
        });
    }

    private uploadFileExcel(fileUpload: File) {
        this.readExcelToList(fileUpload).then(respone => {
            const extractedList = respone;
            this.searchByImportedItems(extractedList);
        }).catch((error) => {
            console.log(error);
        });
    }

    private readExcelToList(fileUpload: File): Promise<any[]> {
        return new Promise((resolve, reject) => {
            if (!fileUpload) {
                resolve([]);
                return;
            }
            const extractedList = [];
            const reader = new FileReader();
            reader.onload = () => {
                const data = reader.result;
                const workbook = XLSX.read(data, {
                    type: 'buffer'
                });
                const sheetName = workbook.SheetNames[0];
                const sheet = workbook.Sheets[sheetName];
                const json: any[] = XLSX.utils.sheet_to_json(sheet, { header: 1 });
                json.forEach((rowValues: any[]) => {
                    if (rowValues && rowValues.length > 0) {
                        const articleInfo: any = {};
                        articleInfo.description = rowValues[0];
                        articleInfo.amount = rowValues[1];
                        extractedList.push(articleInfo);
                    }
                });

                resolve(extractedList);
            };
            reader.onerror = () => {
                reject(reader.error);
            };
            reader.readAsArrayBuffer(fileUpload);
        });
    }

    private uploadFile(fileUpload: File) {
        const extractedList = [];
        if (typeof (FileReader) !== 'undefined') {
            const reader = new FileReader();
            reader.onload = (e) => {
                const target: any = e.target;
                const rows = target.result.split(/(?:\r\n|\n)+/);
                for (const row of rows) {
                    if ((row || '').trim() === '' || /^sep=/.test(row)) {
                        continue;
                    }
                    let articleData = row.split(/,|;|\t/);
                    const articleInfo = {
                        description: (articleData[0] || '').trim().replace(/\"/g, ''),
                        amount: (articleData[1] || '').trim().replace(/\"/g, '')
                    };
                    extractedList.push(articleInfo);
                }
            };
            reader.onloadend = () => {
                this.searchByImportedItems(extractedList);
            };
            reader.readAsText(fileUpload);
        } else {
            console.error('This browser does not support HTML5.');
        }
    }

    private searchByImportedItems(extractedList) {
        if (extractedList.length === 0) {
            return;
        }
        this.isMultiSearch = true;
        this.searchQueue = extractedList;
        this.searchInQueue();
        this.extractAndSaveAllHistory(extractedList);
    }

    private extractAndSaveAllHistory(extractedList) {
        const history = [];
        for (const item of extractedList) {
            history.push({
                searchTerm: item.description,
                amount: item.amount,
                emitSearch: true,
                isImported: true
            });
        }
        this.saveHistoryToStorage(history);
    }

    private async loadHistoryFromStorage() {
        let searchHistories;
        if (this.isSubBasket) {
            searchHistories = this.articleListSearchStorageService.getSubBasketHistory(this.userService.userDetail.id);
        } else {
            searchHistories = this.articleListSearchStorageService.getHistory(this.userService.userDetail.id);
        }
        if (!searchHistories || !searchHistories.length) {
            return;
        }
        for (const history of searchHistories) {
            history.articles = (history.articles || []).map(item => new ArticleModel(item));
            let emitSearch = false;
            if (history.isFromSearchHistory) {
                emitSearch = true;
                history.isFromSearchHistory = null;
            }
            this.searchHistories.push({
                ...history,
                emitSearch
            });
        }
    }

    private saveHistoryToStorage(histories: SearchHistory[]) {
        if (this.isSubBasket) {
            this.articleListSearchStorageService.setSubBasketHistory(this.userService.userDetail.id, histories);
        } else {
            this.articleListSearchStorageService.setHistory(this.userService.userDetail.id, histories);
        }
    }

    private importFromDms() {
        if (!this.dmsInfo) {
            return;
        }
        const { articleNumbers, articleQuantities } = this.dmsInfo;
        const extractedList = [];
        (articleNumbers || []).forEach((art, index) => {
            extractedList.push({
                description: art,
                amount: (articleQuantities || [])[index] || 0
            });
        });
        this.searchByImportedItems(extractedList);
    }
}
