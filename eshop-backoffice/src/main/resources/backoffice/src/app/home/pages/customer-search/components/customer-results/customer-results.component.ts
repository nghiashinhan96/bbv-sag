import {
    Component,
    OnInit,
    Input,
    ViewChild,
    TemplateRef,
} from '@angular/core';
import { TableCustomerDto } from '../../model/table-customer-dto';
import { ActivatedRoute, Router } from '@angular/router';

import { SagTableControl, SagTableColumn } from 'sag-table';

import {
    CustomerRequestModel,
    CustomerRequestModelSorting,
} from '../../model/customer-request.model';
import { StringHelper } from 'src/app/core/utils/string.util';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import {
    EMPTY_STRING
} from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-customer-results',
    templateUrl: './customer-results.component.html',
    styleUrls: ['./customer-results.component.scss'],
    providers: [CustomerService],
})
export class CustomerResultsComponent implements OnInit, SagTableControl {
    // public customerDetails: any;
    // private customers: any;

    columns = [];
    tableRequest: any;
    tableCallback: any;

    public sortColumns = {
        customerNr: 'orderByCustomerNumberDesc',
        affiliate: 'orderByAffiliateNameDesc',
        companyName: 'orderByOrganisationNameDesc',
    };

    // customerController: any;

    // public itemsPerPage = 10;
    public pagi: number;
    // public totalItems: number;

    private customerRequestModel = new CustomerRequestModel();
    private isFirstSearchByParams = true;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private customerService: CustomerService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            const inputParam: CustomerRequestModel = JSON.parse(params['queryParam']);
            Object.assign(this.customerRequestModel, inputParam);
            this.buildColumns();
            this.customerService.searchCustomer(this.customerRequestModel).subscribe(
                (res) => {
                    this.updatePagination(res);
                },
                (err) => {
                }
            );
        });
    }

    buildColumns() {
        this.columns = [
            {
                id: 'customerNr',
                i18n: 'CUSTOMER.TABLE_RESULT.CUSTOMER_NUMBER',
                filterable: true,
                sortable: true,
                width: '30%',
                defaultValue: this.customerRequestModel.customerNr || EMPTY_STRING
            },
            {
                id: 'affiliate',
                i18n: 'CUSTOMER.TABLE_RESULT.AFFILIATE',
                filterable: true,
                sortable: true,
                width: '30%',
                defaultValue: this.customerRequestModel.affiliate
            },
            {
                id: 'companyName',
                i18n: 'CUSTOMER.TABLE_RESULT.COMPANY_NAME',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '30%',
                defaultValue: this.customerRequestModel.companyName || EMPTY_STRING
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    searchTableData({ request, callback }): void {
        this.tableRequest = request;
        this.tableCallback = callback;
        this.searchCustomers(this.tableRequest);
        return;
    }

    // public sortOrFiltering(config: any) {
    //     let userRequestModelSorting = new CustomerRequestModelSorting();
    //     for (const item of config.sorting.columns) {
    //         // if user do sort
    //         if (item.sort && item.sort !== '') {
    //             userRequestModelSorting = CustomerRequestModelSorting.matchColumnAndSort(
    //                 userRequestModelSorting,
    //                 item.name,
    //                 item.sort === 'desc' ? true : false
    //             );
    //             this.customerRequestModel.sorting = userRequestModelSorting;
    //         } else {
    //             // remove sort
    //             userRequestModelSorting = CustomerRequestModelSorting.matchColumnAndSort(
    //                 userRequestModelSorting,
    //                 item.name,
    //                 null
    //             );
    //             this.customerRequestModel.sorting = userRequestModelSorting;
    //         }
    //         // if user do filter
    //         if (item.filtering) {
    //             this.customerRequestModel = CustomerRequestModel.bindColumnFilterToModel(
    //                 item.name,
    //                 item.filtering.filterString,
    //                 this.customerRequestModel
    //             );
    //         }
    //     }
    //     this.router.navigate([
    //         '/home/search/customers',
    //         { queryParam: JSON.stringify(this.customerRequestModel) },
    //     ]);
    // }

    private transferDataToTableToDto(response) {
        const customerList = new Array<TableCustomerDto>();
        for (const item of response) {
            const customerNr = StringHelper.getEmptyStringIfNull(item.customerNr);
            const affiliate = StringHelper.getEmptyStringIfNull(item.affiliate);
            const companyName = StringHelper.getEmptyStringIfNull(item.companyName);
            const customerSearchDto = new TableCustomerDto({
                customerNr,
                affiliate,
                companyName
            });
            customerList.push(customerSearchDto);
        }
        return customerList;
    }

    sort(columnName, direction) {
        const isDesc = direction === 'desc' ? true : false;

        const customerSorting = new CustomerRequestModelSorting();
        customerSorting.orderByAffiliateNameDesc = null;
        customerSorting.orderByCustomerNumberDesc = null;
        customerSorting.orderByOrganisationNameDesc = null;

        switch (columnName) {
            case this.sortColumns.affiliate:
                customerSorting.orderByAffiliateNameDesc = isDesc;
                this.customerRequestModel.sorting = customerSorting;
                break;
            case this.sortColumns.customerNr:
                customerSorting.orderByCustomerNumberDesc = isDesc;
                this.customerRequestModel.sorting = customerSorting;
                break;
            case this.sortColumns.companyName:
                customerSorting.orderByOrganisationNameDesc = isDesc;
                this.customerRequestModel.sorting = customerSorting;
                break;
        }
    }

    public updatePage(event) {
        this.pagi = event;
        // server starting with 0
        this.customerRequestModel.page = this.pagi - 1;
        this.searchCustomers(this.customerRequestModel);
    }

    public getCustomerDetails(selectedCustomer) {
        this.router.navigate([
            '/home/search/customers/detail',
            {
                affiliate: selectedCustomer.affiliate,
                customerNr: selectedCustomer.customerNr,
                searchMode: this.customerRequestModel.searchMode,
            },
        ]);
    }

    private updatePagination(res = null) {
        if (!!res) {
            const { totalElements } = res;
            const numberOfElements = res.pageable.pageSize;
            const currentPage = res.pageable.pageNumber + 1;
            const customerList = this.transferDataToTableToDto(res.content);
            if (this.tableCallback) {
                this.tableCallback({
                    rows: customerList,
                    page: currentPage,
                    totalItems: totalElements,
                    itemsPerPage: numberOfElements,
                });
            }
        } else {
            if (this.tableCallback) {
                this.tableCallback({
                    rows: [],
                    totalItems: 0,
                });
            }
        }
    }

    private searchCustomers(request) {
        const { page, sort } = request;
        let { companyName, affiliate, customerNr } = request.filter;

        if (this.isFirstSearchByParams) {
            companyName = this.customerRequestModel.companyName || companyName;
            affiliate = this.customerRequestModel.affiliate || affiliate;
            customerNr = this.customerRequestModel.customerNr || customerNr;
            this.isFirstSearchByParams = false;
        }

        this.customerRequestModel.affiliate = affiliate;
        this.customerRequestModel.companyName = companyName;
        this.customerRequestModel.customerNr = customerNr;
        this.customerRequestModel.page = page - 1;
        if (sort.field) {
            this.sort(this.sortColumns[sort.field], sort.direction);
        }

        this.router.navigate([
            '/home/search/customers',
            { queryParam: JSON.stringify(this.customerRequestModel) },
        ]);
    }
}
