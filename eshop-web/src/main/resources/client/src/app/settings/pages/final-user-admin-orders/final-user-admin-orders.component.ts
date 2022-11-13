import { Component, ElementRef, Inject, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { NgOption } from '@ng-select/ng-select';
import { get, isEmpty, keys } from 'lodash';
import * as moment from 'moment';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subscription, of } from 'rxjs';
import { filter, catchError, finalize } from 'rxjs/operators';
import { SagTableColumn, SagTableControl, TablePage } from 'sag-table';
import { Constant } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { FinalUserOrderHistoryDetail } from '../../models/final-user-admin/final-user-order-history-detail.model';
import { FinalUserOrdersCriteria } from '../../models/final-user-admin/final-user-orders-criteria.model';
import { ProfileModel } from '../../models/final-user-admin/user-profile.model';
import { FinalCustomerService } from '../../services/final-customer.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { AffiliateUtil, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { environment } from 'src/environments/environment';
import { PageScrollService } from 'ngx-page-scroll-core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'connect-final-user-admin-orders',
    templateUrl: './final-user-admin-orders.component.html',
    styleUrls: ['./final-user-admin-orders.component.scss']
})
export class FinalUserAdminOrdersComponent implements OnInit, OnDestroy, SagTableControl {
    bsModalRef: BsModalRef;
    userProfile: ProfileModel;

    searchModel: any = {
        orderDescByOrderDate: true
    };
    page: TablePage = new TablePage();
    sort = {
        field: 'orderDate',
        direction: 'desc',
        force: true
    };

    finalCustomerTypes: NgOption[] = [];
    salutationTypes: NgOption[] = [];

    columns = [];
    sortKeys = {
        id: 'orderDescById',
        orderDate: 'orderDescByOrderDate',
        lastName: 'orderDescCustomerInfo'
    };

    notFoundText = '';
    private readonly dateFormat = 'YYYY-MM-DD';

    routerEventsSub: Subscription;
    orderDetail: any;

    @ViewChild('dateTemplate', { static: true }) dateTemplate: TemplateRef<any>;
    @ViewChild('statusTemplate', { static: true }) statusTemplate: TemplateRef<any>;
    @ViewChild('sourceTemplate', { static: true }) sourceTemplate: TemplateRef<any>;
    @ViewChild('viewDetailTemplate', { static: true }) viewDetailTemplate: TemplateRef<any>;
    @ViewChild('orderHistoryDetail', {read: ElementRef, static: false }) orderHistoryDetailElement: ElementRef;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private finalCustomerService: FinalCustomerService,
        private shoppingBasketService: ShoppingBasketService,
        private pageScrollService: PageScrollService,
        @Inject(DOCUMENT) private document: any
    ) {
        this.routerEventsSub = this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                const navigation = router.getCurrentNavigation();
                if (get(navigation, 'previousNavigation.extras.state')) {
                    this.searchModel = navigation.previousNavigation.extras.state;
                }

                setTimeout(() => {
                    this.sort.field = keys(this.sortKeys).find(key => {
                        const sortKey = this.sortKeys[key];
                        return this.searchModel[sortKey] === false || this.searchModel[sortKey] === true;
                    });
                    this.sort.direction = this.searchModel[this.sortKeys[this.sort.field]] ? 'desc' : 'asc';
                    this.page.currentPage = (this.searchModel.page || 0) + 1;
                    this.searchModel = { ...this.searchModel };
                }, 0);

            });
    }

    ngOnInit() {
        this.buildOrderColumns();
    }

    ngOnDestroy() {
        this.routerEventsSub.unsubscribe();
    }

    searchTableData({ request, callback }) {
        const criteria = new FinalUserOrdersCriteria(isEmpty(request.filter) ? null : request.filter);
        criteria.page = request.page - 1;

        if (request.filter.status) {
            criteria.statuses = request.filter.status === 1 ?
                [Constant.ORDER_STATUS_NEW, Constant.ORDER_STATUS_OPEN] :
                [Constant.ORDER_STATUS_ORDERED];
        }

        if (get(request, 'filter.orderDate.dateRange.beginJsDate')) {
            const range = request.filter.orderDate.dateRange;
            criteria.dateFrom = moment(range.beginJsDate).format(this.dateFormat);
            criteria.dateTo = moment(range.endJsDate).format(this.dateFormat);
        } else {
            delete criteria.dateFrom;
            delete criteria.dateTo;
        }

        if (request.sort && request.sort.field) {
            const column = this.sortKeys[request.sort.field];
            const sortValue = request.sort.direction === 'desc';
            criteria.resetSort();
            criteria[column] = sortValue;
        }

        Object.assign(this.searchModel, criteria);

        this.finalCustomerService.getOrders(criteria)
            .subscribe((res) => {
                if (!res) {
                    return;
                }
                callback({
                    rows: res.data,
                    totalItems: res.totalElements,
                    page: this.searchModel.page + 1
                });
            }, (err) => {
                callback({
                    rows: [],
                    totalItems: 0
                });
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
            });
    }

    onViewOrderDetail(event, order: FinalUserOrderHistoryDetail) {
        event.preventDefault();
        event.stopPropagation();
        const spinner = SpinnerService.start();
        this.finalCustomerService.getOrderDetail(order.id)
            .pipe(
                finalize(() => SpinnerService.stop(spinner))
            )
            .subscribe(res => {
                this.orderDetail = res;
                setTimeout(() => {
                    this.pageScrollService.scroll({
                        document: this.document,
                        scrollTarget: this.orderHistoryDetailElement && this.orderHistoryDetailElement.nativeElement || null,
                        scrollInView: true,
                        duration: 400
                    });
                });
            });
    }

    addToShoppingBasket(callback?) {
        const spinner = SpinnerService.start();
        this.shoppingBasketService.addSubBasket(this.orderDetail).pipe(catchError(err => {
            if (callback) {
                callback(false);
            }
            return of(null);
        })).subscribe(basket => {
            if (!!basket) {
                if (callback) {
                    callback(true);
                }
                this.shoppingBasketService.updateOtherProcess(basket);
            }
            SpinnerService.stop(spinner);
        });
    }

    private buildOrderColumns() {
        const isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
        this.columns = [
            {
                id: 'orderDate',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_DATE',
                filterable: true,
                sortable: true,
                width: isEhCz ? '200px' : '250px',
                type: 'date',
                dateFormat: SAG_COMMON_DATETIME_FORMAT
            },
            {
                id: 'status',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_STATUS',
                filterable: true,
                sortable: false,
                cellTemplate: this.statusTemplate,
                width: '180px',
                type: 'select',
                selectSource: this.buildStatusOptions(),
                selectValue: 'value',
                selectLabel: 'label'
            },
            {
                id: 'id',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_NUMBER',
                filterable: true,
                sortable: false,
                width: '120px'
            },
            {
                id: 'vehicleDescs',
                i18n: 'SETTINGS.MY_ORDER.HEADER.VEHICLE',
                filterable: true,
                sortable: false
            },
            {
                id: 'username',
                i18n: 'SETTINGS.MY_ORDER.HEADER.USERNAME',
                filterable: true,
                sortable: false,
                width: '120px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: isEhCz ? '75px' : '50px',
                cellClass: 'align-middle',
                cellTemplate: this.viewDetailTemplate,
            }
        ] as SagTableColumn[];
    }

    private buildStatusOptions() {
        return [
            { value: 0, label: 'SETTINGS.FINAL_USER_ORDER.FILTER.ALL' },
            { value: 1, label: 'SETTINGS.FINAL_USER_ORDER.FILTER.NEW', name: Constant.ORDER_STATUS_NEW },
            { value: 2, label: 'SETTINGS.FINAL_USER_ORDER.FILTER.ORDERED', name: Constant.ORDER_STATUS_ORDERED }
        ];
    }
}
