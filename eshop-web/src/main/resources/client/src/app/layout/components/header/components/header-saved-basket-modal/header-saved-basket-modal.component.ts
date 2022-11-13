import { Component, OnInit, ViewChild, TemplateRef, Inject } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import {
    SagTableColumn,
    SagTableControl,
    SagTableRequestModel,
    SagTableResponseModel,
    TablePage
} from 'sag-table';
import { AffiliateUtil, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { ShoppingBasketHistoryService } from 'src/app/core/services/shopping-basket-history.service';
import { BasketHistoryRequestModel } from '../../models/basket-history-request.model';
import { UserService } from 'src/app/core/services/user.service';
import { BASKET_HISTORY_FILTER_ENUM } from '../../enums/basket-history-filter.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { BasketHistoryItemModel } from '../../models/basket-history-item.model';
import { DATE_FILTER } from 'src/app/core/enums/date-filter.enum';
import { catchError, map, tap, first } from 'rxjs/operators';
import { of } from 'rxjs';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { BasketHistoryArticleModel } from '../../models/basket-history-article.model';
import { CustomerSearchService } from 'src/app/home/service/customer-search.service';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { SagConfirmationBoxComponent, SagMessageData, BroadcastService } from 'sag-common';
import { ErrorCodeEnum } from 'src/app/core/enums/error-code.enum';
import { Constant, APP_SHOPPING_CART_UPDATED_EVENT } from 'src/app/core/conts/app.constant';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { AnalyticEventType, ShoppingBasketEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { ArticleModel } from 'sag-article-detail';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-header-saved-basket-modal',
    templateUrl: './header-saved-basket-modal.component.html',
    styleUrls: ['./header-saved-basket-modal.component.scss']
})
export class HeaderSavedBasketModalComponent implements OnInit, SagTableControl {
    rows = [];
    columns: SagTableColumn[];
    searchModel = {};

    dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;
    basketFilterMode;
    filterOptions;
    basketHistoryArticles$;
    selectedBasketHistoryItem: BasketHistoryItemModel = null;
    sort = {
        field: 'savingDate',
        direction: 'desc',
        force: true
    };
    messenger: SagMessageData = null;
    page = new TablePage();
    private articlesInSavedBasket: any[];

    private customerNrCol: SagTableColumn = {
        i18n: 'COMMON_LABEL.COLUMNS.CUSTOMER_NR',
        filterable: true,
        id: 'customerNr',
        sortable: true,
        width: '100px',
    };
    private deleteModelRef: BsModalRef;
    @ViewChild('dateRef', { static: true }) dateRef: TemplateRef<any>;
    @ViewChild('deleteRef', { static: true }) deleteRef: TemplateRef<any>;

    private isSb = AffiliateUtil.isSb(environment.affiliate);

    constructor(
        public bsModalRef: BsModalRef,
        public shoppingBasketService: ShoppingBasketService,
        private userService: UserService,
        private shoppingBasketHistoryService: ShoppingBasketHistoryService,
        private appStorage: AppStorageService,
        private customerSearchService: CustomerSearchService,
        private modalService: BsModalService,
        private analyticService: AnalyticLoggingService,
        private broadcaster: BroadcastService
    ) { }

    ngOnInit() {
        this.filterOptions = this.buildFilterMode();
        this.onFilter();
        this.buildColumns();
    }

    buildColumns() {
        this.columns = [
            this.customerNrCol,
            {
                id: 'customerName',
                i18n: 'COMMON_LABEL.COLUMNS.CUSTOMER_NAME',
                sortable: true,
                filterable: true
            },
            {
                id: 'saleName',
                i18n: 'COMMON_LABEL.COLUMNS.SALE_NAME',
                sortable: true,
                filterable: true
            },
            {
                id: 'basketName',
                i18n: 'BASKET_HISTORY.COLUMNS.BASKET_NAME',
                sortable: true,
                filterable: true
            },
            {
                id: 'referenceText',
                i18n: 'COMMON_LABEL.COLUMNS.REFERENCE',
                sortable: true,
                filterable: true
            },
            {
                id: 'savingDate',
                i18n: 'COMMON_LABEL.COLUMNS.SAVING_DATE',
                sortable: true,
                filterable: true,
                type: 'select',
                selectSource: [{
                    value: DATE_FILTER.ALL,
                    label: `COMMON_LABEL.${DATE_FILTER.ALL}`
                },
                {
                    value: DATE_FILTER.TODAY,
                    label: `COMMON_LABEL.${DATE_FILTER.TODAY}`
                },
                {
                    value: DATE_FILTER.TWO_DAYS,
                    label: `COMMON_LABEL.${DATE_FILTER.TWO_DAYS}`
                },
                {
                    value: DATE_FILTER.LAST_WEEK,
                    label: `COMMON_LABEL.${DATE_FILTER.LAST_WEEK}`
                }],
                defaultValue: DATE_FILTER.ALL,
                selectValue: 'value',
                selectLabel: 'label',
                width: '130px',
                cellTemplate: this.dateRef
            },
            {
                id: '',
                i18n: '',
                sortable: false,
                filterable: false,
                width: '50px',
                cellTemplate: this.deleteRef,
                class: 'text-center'
            }
        ];
    }

    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void; }): void {
        const filter = request.filter || {};
        if (this.basketFilterMode === BASKET_HISTORY_FILTER_ENUM.USER_BASKET) {
            filter.customerNr = '';
        }
        const basketHistoryRequest = new BasketHistoryRequestModel(Object.assign({
            basketFilterMode: this.basketFilterMode,
            salesOnBehalfToken: this.appStorage.saleToken,
            offset: request.page - 1
        }, (filter))).sort(request.sort);

        this.shoppingBasketHistoryService.loadBasketHistory(basketHistoryRequest.requestBody).subscribe((res: any) => {
            this.clearSavedBasketDetail();
            this.rows = (res && res.content || []).map(item => new BasketHistoryItemModel(item, this.userService.userDetail));
            callback({
                rows: this.rows,
                totalItems: res.totalElements,
                itemsPerPage: res.size,
                page: res.number + 1
            });
        });
    }

    onFilter() {
        this.page = new TablePage();
        this.messenger = null;
        switch (this.basketFilterMode) {
            case BASKET_HISTORY_FILTER_ENUM.USER_BASKET:
            case BASKET_HISTORY_FILTER_ENUM.CUSTOMER_BASKET:
                this.searchModel = Object.assign({}, { customerNr: this.userService.userDetail.custNr });
                this.customerNrCol.sortable = false;
                this.customerNrCol.filterable = false;
                break;
            case BASKET_HISTORY_FILTER_ENUM.SALES_BASKET:
                this.customerNrCol.sortable = true;
                this.customerNrCol.filterable = true;
                this.searchModel = Object.assign({}, { customerNr: '' })
                if (!this.userService.userDetail.isSalesOnBeHalf) {
                    this.searchModel = Object.assign({}, { saleName: this.getSaleName() });
                }
                break;
            default:
                this.searchModel = Object.assign({}, { customerNr: '', });
                this.customerNrCol.sortable = true;
                this.customerNrCol.filterable = true;
                break;
        }
    }

    onSelectBasketItem(basketItem: BasketHistoryItemModel) {
        this.messenger = null;
        SpinnerService.start('connect-header-saved-basket-modal .modal-body');
        this.basketHistoryArticles$ = this.shoppingBasketHistoryService.getBasketHistory(basketItem.id).pipe(
            catchError(err => {
                return of({});
            }),
            map(({ items }: { items: { articles: ArticleModel[], vehicle: any }[] }) => {
                let result = [];
                this.articlesInSavedBasket = [];
                if (items) {
                    this.selectedBasketHistoryItem = basketItem;
                    items.forEach(({ articles, vehicle }) => {
                        const vehicleInfo = vehicle && vehicle.vehicleInfo;
                        result = [...result, ...articles.map(art => new BasketHistoryArticleModel(new ArticleModel(art), vehicleInfo))];
                        this.articlesInSavedBasket.push({
                            vehicle,
                            articles: articles.map(art => new ArticleModel(art))
                        });
                    });
                }
                return result;
            }),
            tap(() => {
                SpinnerService.stop('connect-header-saved-basket-modal .modal-body');
            })
        );
    }

    checkUserAndAddToCart() {
        if (!this.selectedBasketHistoryItem) {
            return;
        }
        this.messenger = null;
        SpinnerService.start('connect-header-saved-basket-modal .modal-body');
        if (this.userService.userDetail.salesUser && !this.userService.userDetail.isSalesOnBeHalf) {
            this.customerSearchService.switchUser(this.selectedBasketHistoryItem.customerNr).pipe(
                catchError((err) => {
                    // this.errorMessage = this.errorHandler(err.error);
                    this.messenger = {
                        message: 'BASKET_HISTORY.ADDING_FAILED',
                        type: 'ERROR'
                    };
                    SpinnerService.stop('connect-header-saved-basket-modal .modal-body');
                    return of(false);
                })).subscribe(successful => {
                    if (successful) {
                        this.addBasketHistoryIntoCart(this.selectedBasketHistoryItem.id);
                    }
                });
        } else {
            this.addBasketHistoryIntoCart(this.selectedBasketHistoryItem.id);
        }
    }

    private addBasketHistoryIntoCart(basketHistoryId: number) {
        this.messenger = null;
        this.shoppingBasketHistoryService.addBasketHistoryToCart(basketHistoryId).pipe(catchError(error => {
            this.messenger = {
                message: 'BASKET_HISTORY.ADDING_FAILED',
                type: 'ERROR'
            };
            return of(null);
        })).subscribe(res => {
            if (!res) {
                return;
            }
            const shoppingBasket = new ShoppingBasketModel(res);
            this.broadcaster.broadcast(APP_SHOPPING_CART_UPDATED_EVENT, shoppingBasket);
            this.sendSavedBasketEventData();
            this.shoppingBasketService.updateMiniBasket(shoppingBasket);

            // emit reload in article List
            if (this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
                const items = (shoppingBasket && shoppingBasket.items || []);
                this.shoppingBasketService.updateItemInBasketSubject.next(items);
            }

            this.shoppingBasketService.loadMiniBasket();

            // restored text reference if not empty
            const customerRefText = this.selectedBasketHistoryItem.referenceText;
            if (customerRefText) {
                const customerNr = this.selectedBasketHistoryItem.customerNr;
                this.appStorage.refText = { [customerNr]: customerRefText };
            }
            SpinnerService.stop('connect-header-saved-basket-modal .modal-body');
            this.messenger = {
                message: 'BASKET_HISTORY.ADDING_SUCCESSFULLY',
                type: 'SUCCESS'
            };
        });
    }

    deleteBasketItem(event, basketItem: BasketHistoryItemModel) {
        event.preventDefault();
        event.stopPropagation();
        this.deleteModelRef = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-delete-basket-item',
            initialState: {
                title: '',
                message: 'BASKET_HISTORY.DELETE',
                messageParams: '',
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                bodyIcon: 'fa-exclamation-triangle',
            },
            ignoreBackdropClick: true
        });
        this.deleteModelRef.content.close = () => new Promise((resolve) => {
            SpinnerService.start('connect-confirmation-box');
            this.shoppingBasketHistoryService.deleteBasketHistory(basketItem.id)
                .pipe(catchError(error => {
                    resolve({
                        message: 'BASKET_HISTORY.NOTICE_MESSAGE.DELETE_FAILURE',
                        type: 'ERROR',
                    } as SagMessageData);
                    return of(ErrorCodeEnum.UNKNOWN_ERROR);
                })).subscribe(res => {
                    this.shoppingBasketHistoryService.loadBasketHistoryQuantity();
                    if (res !== ErrorCodeEnum.UNKNOWN_ERROR) {
                        resolve({
                            message: 'BASKET_HISTORY.NOTICE_MESSAGE.DELETE_SUCCESS',
                            type: 'SUCCESS',
                        } as SagMessageData);
                        setTimeout(() => {
                            this.deleteModelRef.content.hideModel();
                            this.searchModel = { ...this.searchModel };
                        }, 2000);
                    }
                    SpinnerService.stop('connect-confirmation-box');
                });
        });
    }

    private buildFilterMode() {
        if (this.userService.userDetail.salesUser || this.userService.userDetail.isSalesOnBeHalf) {
            this.basketFilterMode = BASKET_HISTORY_FILTER_ENUM.SALES_BASKET;
            const options = [{
                value: BASKET_HISTORY_FILTER_ENUM.ALL_BASKET,
                label: 'COMMON_LABEL.ALL'
            },
            {
                value: BASKET_HISTORY_FILTER_ENUM.SALES_BASKET,
                label: 'BASKET_HISTORY.MY_SAVED_BASKETS'
            }];
            // For sales login on behalf
            if (this.userService.userDetail.isSalesOnBeHalf) {
                this.basketFilterMode = BASKET_HISTORY_FILTER_ENUM.CUSTOMER_BASKET;
                options.push({
                    value: BASKET_HISTORY_FILTER_ENUM.CUSTOMER_BASKET,
                    label: 'BASKET_HISTORY.CURRENT_CUSTOMER'
                });
            }
            return options;
        } else {
            this.basketFilterMode = BASKET_HISTORY_FILTER_ENUM.USER_BASKET;
            return [{
                value: BASKET_HISTORY_FILTER_ENUM.CUSTOMER_BASKET,
                label: 'COMMON_LABEL.ALL'
            }, {
                value: BASKET_HISTORY_FILTER_ENUM.USER_BASKET,
                label: 'BASKET_HISTORY.MY_SAVED_BASKETS'
            }];
        }
    }

    private getSaleName() {
        return `${this.userService.userDetail.firstName}${Constant.SPACE}${this.userService.userDetail.lastName}`;
    }

    private sendSavedBasketEventData() {
        setTimeout(() => {
            let savedArticles = [];
            this.articlesInSavedBasket.forEach(({ vehicle, articles }) => {
                savedArticles = [
                    ...savedArticles,
                    ...articles.map(art => ({ ...art, vehicle }))
                ];
            });
            const event = this.analyticService.createShoppingBasketEventData(
                savedArticles,
                {
                    source: ShoppingBasketEventType.SAVED_BASKETS
                }
            );
            this.analyticService
                .postEventFulltextSearch(event, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    private clearSavedBasketDetail() {
        this.basketHistoryArticles$ = of(null);
    }
}
