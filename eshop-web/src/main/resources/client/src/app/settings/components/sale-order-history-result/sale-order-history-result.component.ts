import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { Router } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { SagTableColumn, SagTableControl } from 'sag-table';

import { SaleOrderHistory } from '../../models/sale-order-history/sale-order-history.model';
import { OrderHistoryBusinessService } from '../../services/order-history-business.service';
import { Constant } from 'src/app/core/conts/app.constant';
import { SalesOrderHistoryFilter } from '../../models/sale-order-history/sale-order-history-filter.model';
import { OrderHistoryService } from '../../services/order-history.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SagMessageData, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { SaleOrderHistoryDetail } from '../../models/sale-order-history/sale-order-history-detail.model';
import { CustomerSearchService } from 'src/app/home/service/customer-search.service';
import { ORDER_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { OrderHistoryDetailRequest } from 'src/app/settings/models/order-history/order-history-detail.model';
import { IAngularMyDpOptions } from 'angular-mydatepicker';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';

@Component({
    selector: 'connect-sale-order-history-result',
    templateUrl: './sale-order-history-result.component.html',
    styleUrls: ['./sale-order-history-result.component.scss']
})
export class SaleOrderHistoryResultComponent implements OnInit, SagTableControl {
    @ViewChild('dateFilter', { static: true }) dateFilter: TemplateRef<any>;
    @ViewChild('typeTemplate', { static: true }) typeTemplate: TemplateRef<any>;

    errorMessage: SagMessageData;

    columns: SagTableColumn[] = [];
    orderTypeOptions: any[];
    saleOrderHistoryResult: Observable<SaleOrderHistory[]>;
    tableCallback: any;
    tableRequest: any;

    selectedOrder = null;
    selectedCustomerNr: number;
    orderHistoryRequest: OrderHistoryDetailRequest;
    
    locale = '';
    defaultFormat = SAG_COMMON_DATETIME_FORMAT;
    dateOption = {
        dateRange: false,
        dateFormat: 'dd.mm.yyyy',
        sunHighlight: true,
        markCurrentDay: true,
        showSelectorArrow: true,
        focusInputOnDateSelect: false,
        inputFieldValidation: true,
        showFooterToday: true
    } as IAngularMyDpOptions;

    constructor(
        private orderHistoryBusiness: OrderHistoryBusinessService,
        private orderHistoryService: OrderHistoryService,
        private customerSearchService: CustomerSearchService,
        private router: Router,
        private translateService: TranslateService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.buildOrderTypeOptions();
        this.columns = this.orderHistoryBusiness.buildSaleOrderHistoryColumn([this.dateFilter, this.typeTemplate], this.orderTypeOptions);
    }

    buildOrderTypeOptions() {
        this.orderTypeOptions = [];
        this.orderTypeOptions.push({
            value: Constant.SPACE,
            label: 'ORDER.TYPE.ALL'
        });
        this.orderTypeOptions.push({
            value: ORDER_TYPE.ORDER.toString(),
            label: 'ORDER.TYPE.ORDER'
        });
        this.orderTypeOptions.push({
            value: ORDER_TYPE.KSO_AUT.toString(),
            label: 'ORDER.TYPE.KSO_AUT'
        });
        this.orderTypeOptions.push({
            value: ORDER_TYPE.COUNTER.toString(),
            label: 'ORDER.TYPE.COUNTER'
        });
        this.orderTypeOptions.push({
            value: ORDER_TYPE.ABS.toString(),
            label: 'ORDER.TYPE.ABS'
        });
        this.orderTypeOptions.push({
            value: ORDER_TYPE.STD.toString(),
            label: 'ORDER.TYPE.STD'
        });
    }

    searchTableData({ request, callback }) {
        this.tableCallback = callback;
        this.tableRequest = request;
        const filter = this.handleSearchRequest();
        this.getSaleOrderHistory(filter);
    }

    handleSearchRequest(): SalesOrderHistoryFilter {
        if (!this.tableRequest) {
            return null;
        }
        const filter = new SalesOrderHistoryFilter({
            ... this.tableRequest.filter,
            page: this.tableRequest.page - 1,
            size: Constant.DEFAULT_PAGE_SIZE
        });

        const { field, direction } = this.tableRequest.sort;
        if (field) {
            filter.resetSort({ column: field, value: direction === 'asc' });
        }
        return filter;
    }


    getSaleOrderHistory(filter?: SalesOrderHistoryFilter) {
        if (!filter) {
            filter = new SalesOrderHistoryFilter({ page: Constant.FIRST_PAGE_INDEX, size: Constant.DEFAULT_PAGE_SIZE });
        }
        this.orderHistoryService.getSaleOrders(filter).pipe(
            finalize(() => SpinnerService.stop())
        ).subscribe((res: any) => {
            this.handleSaleOrderHistoryData(res);
        }, (err) => {
            this.handleSaleOrderHistoryData({ rows: [] });
        });
    }

    handleSaleOrderHistoryData(res: any) {
        if (this.tableCallback) {
            this.tableCallback({
                rows: res.rows as SaleOrderHistory[],
                totalItems: res.totalItems,
                itemsPerPage: res.itemsPerPage,
                page: res.currentPage + 1
            });
        }
    }

    onSelectOrderHistory(order: any): any {
        this.selectedOrder = null;
        this.selectedCustomerNr = null;

        SpinnerService.start();

        this.orderHistoryService.getOrderDetail(order).pipe(
            finalize(() => SpinnerService.stop())
        ).subscribe(
            (response: any) => {
                const data = response.orderItems;
                this.selectedOrder = [];
                this.selectedCustomerNr = response.customerNr;
                this.orderHistoryRequest = <OrderHistoryDetailRequest>{
                    orderId: response.id,
                    orderNumber: response.nr || null
                };

                data.map(item => {
                    this.selectedOrder.push(
                        new SaleOrderHistoryDetail({
                            quantity: item.article.amountNumber + '',
                            articleNr: item.article.artnr || '',
                            info: this.getInforText(item.article) || '',
                            vehicle: item.vehicle.vehicleInfo || ''
                        })
                    );
                });
            }, err => {
                this.errorMessage = { message: 'MESSAGE_SEARCH_NOT_FOUND.ORDER_HISTORY', type: 'ERROR' };
            }
        );
    }

    getInforText(article) {
        article.genArtTxts = article.genArtTxts || [];
        const genArtStr = article.genArtTxts.reduce((val, res) => (res.gatxtdech) + val, '');
        return `${article.product_brand || ''} ${genArtStr || ''}`;
    }

    switchUser() {
        SpinnerService.start();
        this.customerSearchService.switchUser(this.selectedCustomerNr, this.orderHistoryRequest).pipe(
            finalize(() => {
                SpinnerService.stop();
            })
        ).subscribe((res) => {
            this.orderHistoryRequest = null;
            this.router.navigateByUrl(Constant.HOME_PAGE);
        });
    }

    onFilterDate(event) {
        let date = event && event.singleDate && event.singleDate.jsDate || null;
        if (date) {
            date = moment(date).format('YYYY-MM-DD');
        }
        this.tableRequest.filter.orderDate = date;
        const filter = this.handleSearchRequest();
        this.getSaleOrderHistory(filter);
    }
}
