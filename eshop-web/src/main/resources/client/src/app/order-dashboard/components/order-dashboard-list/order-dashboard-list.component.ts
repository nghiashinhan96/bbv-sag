import { Component, OnInit, Input, ViewChild, TemplateRef, SimpleChanges, OnChanges } from '@angular/core';
import {
    SagTableColumn,
    SagTableControl,
    SagTableRequestModel,
    SagTableResponseModel,
    TablePage
} from 'sag-table';
import { OrderDasboardService } from '../../service/order-dasboard.service';
import { OrderDashboardListRequestModel } from '../../models/order-dashboard-list-request.model';
import { ORDER_STATUS } from '../../enums/order-status';
import { IAngularMyDpOptions } from 'angular-mydatepicker';
import { TranslateService } from '@ngx-translate/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { OrderDashboardListItemModel } from '../../models/order-dashboard-list-item.model';
import { SagConfirmationBoxComponent, SagMessageData, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { ErrorCodeEnum } from 'src/app/core/enums/error-code.enum';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { Router } from '@angular/router';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SubShoppingBasketService } from 'src/app/core/services/sub-shopping-basket.service';
import * as moment from 'moment';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { BasketItemSourceDesc } from 'src/app/analytic-logging/enums/basket-item-source-desc.enum';
import { FC_ORDER_EXPORT_TYPE } from '../../enums/order-export-type';
import { UserService } from 'src/app/core/services/user.service';
const DATE_FORMAT = 'YYYY-MM-DD';
@Component({
    selector: 'connect-order-dashboard-list',
    templateUrl: './order-dashboard-list.component.html',
    styleUrls: ['./order-dashboard-list.component.scss']
})
export class OrderDashboardListComponent implements OnInit, OnChanges, SagTableControl {
    @Input() type;
    @Input() mode = 'online';
    @Input() rows = [];
    MY_CUSTOMER_ORDERS = ORDER_STATUS.MY_CUSTOMER_ORDERS;
    ORDERED = ORDER_STATUS.ORDERED;
    FC_ORDER_EXPORT_TYPE = FC_ORDER_EXPORT_TYPE;

    totalOrders = 0;
    columns: SagTableColumn[] = [];
    sort = {
        field: 'orderDate',
        direction: 'desc',
        force: true
    };
    searchModel = {};
    dateOption = {
        dateRange: true,
        dateFormat: 'dd.mm.yyyy',
        sunHighlight: true,
        markCurrentDay: true,
        showSelectorArrow: true,
        focusInputOnDateSelect: false,
        inputFieldValidation: true,
        showFooterToday: true
    } as IAngularMyDpOptions;
    rangeDate;
    page = new TablePage();
    locale = '';
    isShowVat: boolean = false;
    errorMessage: SagMessageData;

    @ViewChild('customerInfoRef', { static: true }) customerInfoRef: TemplateRef<any>;
    @ViewChild('articleDesc', { static: true }) articleDesc: TemplateRef<any>;
    @ViewChild('dateFilter', { static: true }) dateFilter: TemplateRef<any>;
    @ViewChild('actionRef', { static: true }) actionRef: TemplateRef<any>;
    @ViewChild('infoRef', { static: true }) infoRef: TemplateRef<any>;
    @ViewChild('totalGrossPriceRef', { static: true }) totalGrossPriceRef: TemplateRef<any>;
    @ViewChild('totalFCNetPriceRef', { static: true }) totalFCNetPriceRef: TemplateRef<any>;

    constructor(
        private dasboardService: OrderDasboardService,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private shoppingBasket: ShoppingBasketService,
        private router: Router,
        private appStorage: AppStorageService,
        private subShoppingBasket: SubShoppingBasketService,
        private analyticService: AnalyticLoggingService,
        public userService: UserService,
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.columns = [
            {
                id: 'orderDate',
                i18n: 'ORDER_DASHBOARD.ORDER_DATE_TIME',
                filterable: !(this.type === this.ORDERED),
                sortable: true,
                type: 'date',
                dateFormat: SAG_COMMON_DATETIME_FORMAT,
                filterTemplate: this.dateFilter,
                width: '230px'
            }, {
                id: 'id',
                i18n: 'ORDER_DASHBOARD.CART_NR',
                filterable: true,
                sortable: true
            }, {
                id: 'articleDesc',
                i18n: 'ORDER_DASHBOARD.VEHICLE_ARTICLE',
                filterable: false,
                sortable: false,
                width: '200px',
                cellTemplate: this.articleDesc
            }, {
                id: 'totalGrossPrice',
                i18n: 'ORDER_DASHBOARD.TOTAL',
                filterable: false,
                sortable: false,
                cellTemplate: this.totalGrossPriceRef,
                cellClass: 'align-middle text-right',
                class: 'text-right',
                width: '80px'
            }, {
                id: 'totalFinalCustomerNetPrice',
                i18n: 'ORDER_DASHBOARD.TOTAL_NETTO',
                filterable: false,
                sortable: false,
                cellTemplate: this.totalFCNetPriceRef,
                cellClass: 'align-middle text-right',
                class: 'text-right',
                width: '120px'
            }, {
                id: 'customerInfo',
                i18n: 'ORDER_DASHBOARD.CUSTOMER',
                filterable: true,
                sortable: true,
                cellTemplate: this.customerInfoRef,
                width: '200px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.actionRef,
                cellClass: 'ordered'
            }
        ];

        if (this.type === ORDER_STATUS.NEWS_ORDERS) {
            this.subShoppingBasket.subBasketOverview$.subscribe(res => {
                if (!!res && this.totalOrders !== res.newOrderQuantity) {
                    this.searchModel = { ...this.searchModel };
                }
            });
        }
        this.initialShowVat();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.userSetting && !changes.userSetting.firstChange) {
            this.initialShowVat();
        }
    }

    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void; }): void {
        const basketHistoryRequest = new OrderDashboardListRequestModel(Object.assign({
            statuses: [this.type],
            page: request.page - 1
        }, (request.filter || {}))).sort(request.sort);
        if (basketHistoryRequest.dateFrom) {
            basketHistoryRequest.dateFrom = moment(basketHistoryRequest.dateFrom).format(DATE_FORMAT);
        }
        if (basketHistoryRequest.dateTo) {
            basketHistoryRequest.dateTo = moment(basketHistoryRequest.dateTo).format(DATE_FORMAT);
        }
        this.dasboardService.getOrders(basketHistoryRequest).subscribe(res => {
            this.totalOrders = res.totalElements || 0;
            callback({
                rows: res.orderList,
                totalItems: res.totalElements,
            });
        }, (err) => {
            if (callback) {
                callback({
                    rows: [],
                    totalItems: 0
                });
            }
        });
    }


    onFilterDate(event) {
        const dateRange = event && event.dateRange || null;
        this.searchModel = Object.assign({}, this.searchModel, {
            dateFrom: dateRange && dateRange.beginJsDate && dateRange.beginJsDate.toISOString() || null,
            dateTo: dateRange && dateRange.endJsDate && dateRange.endJsDate.toISOString() || null
        });
    }


    deleteSubOrder(event, order: OrderDashboardListItemModel) {
        event.preventDefault();
        event.stopPropagation();
        const deleteModelRef = this.modalService.show(SagConfirmationBoxComponent, {
            initialState: {
                title: 'ORDER_DASHBOARD.CONFIRMATION.CONFIRM_DELETE_ORDER',
                message: 'ORDER_DASHBOARD.CONFIRMATION.CONFIRM_DELETE_ORDER'
            },
            ignoreBackdropClick: true
        });
        deleteModelRef.content.close = () => new Promise((resolve) => {
            SpinnerService.start('connect-confirmation-box');
            this.dasboardService.deleteOrder(order.id)
                .pipe(catchError(error => {
                    resolve({
                        message: 'BASKET_HISTORY.NOTICE_MESSAGE.DELETE_FAILURE',
                        type: 'ERROR',
                    } as SagMessageData);
                    return of(ErrorCodeEnum.UNKNOWN_ERROR);
                })).subscribe(res => {
                    if (res !== ErrorCodeEnum.UNKNOWN_ERROR) {
                        // resolve({
                        //     message: 'BASKET_HISTORY.NOTICE_MESSAGE.DELETE_SUCCESS',
                        //     type: 'SUCCESS',
                        // } as SagMessageData);
                        resolve(null);
                        this.subShoppingBasket.getOverview();
                        this.searchModel = Object.assign({}, { ...this.searchModel }, { action: 'delete' });
                    }
                    SpinnerService.stop('connect-confirmation-box');
                });
        });
    }

    addToBasket(event, row) {
        event.preventDefault();
        event.stopPropagation();
        if (this.type === ORDER_STATUS.ORDERED) {
            this.shoppingBasket.setBasketType(SHOPPING_BASKET_ENUM.DEFAULT);
        } else {
            this.shoppingBasket.setBasketType(SHOPPING_BASKET_ENUM.FINAL);
        }

        const spinner = SpinnerService.startApp();
        let basketItemSource = this.analyticService.createBasketItemSource(BasketItemSourceDesc.WSS_FC_ORDER);
        this.appStorage.clearBasketItemSource();
        this.shoppingBasket.addSubBasket(row, basketItemSource).pipe(
            catchError(err => {
                return of(null);
            })
        ).subscribe(res => {
            this.subShoppingBasket.getOverview();
            if (!!res) {
                this.router.navigate(['/shopping-basket', 'cart'], {
                    queryParams: {
                        basket: this.type === ORDER_STATUS.ORDERED ? SHOPPING_BASKET_ENUM.DEFAULT : SHOPPING_BASKET_ENUM.FINAL,
                        id: row.id
                    }
                }).then(status => {
                    SpinnerService.stop(spinner);
                });
            } else {
                SpinnerService.stop(spinner);
            }
        });

    }

    exportFinalCustomerOrder(event, finalCustomerOrder: OrderDashboardListItemModel, exportType: FC_ORDER_EXPORT_TYPE) {
        event.preventDefault();
        event.stopPropagation();
        if (!finalCustomerOrder || !exportType) {
            return;
        }
        const fileType: string = exportType === FC_ORDER_EXPORT_TYPE.CSV ? 'csv' : 'xlsx'
        const finalCustomerOrderId = finalCustomerOrder.id;
        const fileName = `order_${finalCustomerOrderId}_${moment(finalCustomerOrder.orderDate).format('YYYYMMDD')}_${finalCustomerOrder.customerNumber}.${fileType}`;
        this.dasboardService.exportFinalCustomerOrder(finalCustomerOrderId, exportType, fileName).subscribe((res: SagMessageData) => {
            this.errorMessage = res;
        });
    }
    
    private initialShowVat() {
        if (!this.userService.userPrice) {
            return;
        }
        const inclVATSettings = this.userService.userPrice && this.userService.userPrice.vatTypeDisplayConvert;
        this.isShowVat = inclVATSettings && inclVATSettings.list;
    }

}
