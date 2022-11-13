import {
    Component,
    OnInit,
    OnDestroy,
    ViewChild,
    TemplateRef,
} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { Subject } from 'rxjs';
import { SagTableControl, SagTableColumn } from 'sag-table';
import { takeUntil } from 'rxjs/operators';

import { CustomerPopupComponent } from '../customer-popup/customer-popup.component';
import { CustomerGroupService } from 'src/app/home/services/customer-group/customer-group.service';
import { CustomerGroupModel } from 'src/app/home/models/customer-group/customer-group.model';
import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { CustomerGroupSearchModel } from 'src/app/home/models/customer-group/customer-group-search.model';
import {
    CustomerGroupRequestSortingModel,
    CustomerGroupRequestModel,
} from 'src/app/home/models/customer-group/customer-group-request.model';
import {
    DEFAULT_SELECTOR_VALUE
} from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
    selector: 'backoffice-customer-group-list',
    templateUrl: './customer-group-list.component.html',
    styleUrls: ['./customer-group-list.component.scss'],
})
export class CustomerGroupListComponent implements OnInit, OnDestroy, SagTableControl {
    public searchRequest = new CustomerGroupRequestModel();
    public customerGroups: CustomerGroupModel[] = null;
    public pagination = {
        itemsPerPage: 0,
        currentPage: 0,
        totalItems: 0,
    };

    columns = [];
    tableRequest: any;
    tableCallback: any;

    private isDestroyed$ = new Subject<boolean>();
    public affiliates = [];
    public sortColumns = {
        collectionName: 'orderByCollectionNameDesc',
        affiliate: 'orderByAffiliateNameDesc',
    };
    readonly spinnerSelector = '.customer-group-table';

    private bsModalRef: BsModalRef = null;
    private searchTimeout;

    @ViewChild('colCustomerNr', { static: true }) colCustomerNr: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private customerGroupService: CustomerGroupService,
        private affiliateService: AffiliateService,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        this.activatedRoute.queryParams.pipe(
            takeUntil(this.isDestroyed$)
        )
            .subscribe((params) => {
                this.searchRequest.affiliate = params.affiliate;
                this.searchRequest.collectionName = params.collectionName;
                this.searchRequest.sorting = new CustomerGroupRequestSortingModel();
            });

        this.getAffiliates();
    }

    ngOnDestroy() {
        this.isDestroyed$.next(true);
        this.isDestroyed$.complete();
        if (this.bsModalRef) {
            this.bsModalRef.hide();
        }
    }

    buildColumns() {
        this.searchRequest.affiliate =
            this.searchRequest.affiliate || DEFAULT_SELECTOR_VALUE.value;
        this.searchRequest.collectionName = this.searchRequest.collectionName || '';

        const selectorValue = this.affiliates.find(
            (aff) => aff.value == this.searchRequest.affiliate
        );

        this.columns = [
            {
                id: 'collectionName',
                i18n: 'CUSTOMER_GROUP.TABLE_RESULT.CUSTOMER_GROUP_NAME',
                filterable: true,
                sortable: true,
                defaultValue: '',
                width: '30%',
            },
            {
                id: 'affiliate',
                i18n: 'CUSTOMER_GROUP.TABLE_RESULT.AFFILIATE',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.affiliates,
                defaultValue: selectorValue,
                width: '30%',
            },
            {
                id: 'customerNr',
                i18n: 'CUSTOMER_GROUP.TABLE_RESULT.CUSTOMER_NR',
                filterable: true,
                sortable: false,
                cellTemplate: this.colCustomerNr,
                defaultValue: '',
                cellClass: 'align-middle',
                width: '30%',
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

    goToDetailPage(collectionShortName: string) {
        this.router.navigateByUrl(
            `/home/search/customer-groups/${collectionShortName}`
        );
    }

    // updatePage(page) {
    //     this.searchRequest = {
    //         ...this.searchRequest,
    //         page,
    //     };
    //     this.getCustomerGroups();
    // }

    sort(columnName, direction) {
        const isDesc = direction === 'desc' ? true : false;
        switch (columnName) {
            case this.sortColumns.collectionName:
                this.searchRequest.sorting.orderByCollectionNameDesc = isDesc;
                this.searchRequest.sorting.orderByAffiliateNameDesc = null;
                break;
            case this.sortColumns.affiliate:
                this.searchRequest.sorting.orderByAffiliateNameDesc = isDesc;
                this.searchRequest.sorting.orderByCollectionNameDesc = null;
                break;
        }
    }

    search() {
        clearTimeout(this.searchTimeout);
        this.searchTimeout = setTimeout(() => {
            const { filter, page, sort } = this.tableRequest;

            let affiliate = null;
            if (filter.affiliate) {
                affiliate = filter.affiliate.value;
            } else {
                affiliate = this.searchRequest.affiliate;
            }

            let collectionName = filter.collectionName;
            if (!collectionName && collectionName !== '') {
                collectionName = this.searchRequest.collectionName;
            }

            this.searchRequest.page = page - 1;
            this.searchRequest.collectionName = collectionName;
            this.searchRequest.affiliate = affiliate;
            this.searchRequest.customerNr = filter.customerNr || '';

            if (sort.field) {
                this.sort(this.sortColumns[sort.field], sort.direction);
            }

            this.getCustomerGroups();
        }, 500);
    }

    searchTableData({ request, callback }): void {
        SpinnerService.start(this.spinnerSelector);
        this.tableRequest = request;
        this.tableCallback = callback;
        this.search();
    }

    showModalCustomerNr(collectionShortName: string) {
        this.bsModalRef = this.modalService.show(CustomerPopupComponent, {
            class: 'customer-nr-modal',
            ignoreBackdropClick: false,
            initialState: {
                collectionShortName,
            },
        });
    }

    createNewGroup() {
        this.router.navigate(['../', 'new'], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge',
        });
    }

    private getAffiliates() {
        this.affiliateService.getShortInfos().subscribe((data) => {
            const res: any = data;
            const affiliates = [];
            affiliates.push(DEFAULT_SELECTOR_VALUE);
            this.affiliates = affiliates.concat(
                res.map((item) => {
                    return { value: item.shortName, label: item.name };
                })
            );
            this.buildColumns();
        });
    }

    private getCustomerGroups() {
        this.customerGroupService
            .search(this.searchRequest)
            .pipe(
                takeUntil(this.isDestroyed$)
            )
            .subscribe(
                (customerSearchModel: CustomerGroupSearchModel) => {
                    this.customerGroups = customerSearchModel.content;
                    this.updatePagination(customerSearchModel);
                },
                () => {
                    this.customerGroups = [];
                    this.updatePagination();
                }
            );
    }

    private updatePagination(res: CustomerGroupSearchModel = null) {
        if (!!res) {
            this.pagination.itemsPerPage = res.size;
            this.pagination.currentPage = res.number + 1;
            this.pagination.totalItems = res.totalElements;

            if (this.tableCallback) {
                this.tableCallback({
                    rows: this.customerGroups,
                    page: this.pagination.currentPage,
                    totalItems: this.pagination.totalItems,
                    itemsPerPage: this.pagination.itemsPerPage,
                });
            }
        } else {
            this.pagination.itemsPerPage = 0;
            this.pagination.currentPage = 0;
            this.pagination.totalItems = 0;

            if (this.tableCallback) {
                this.tableCallback({
                    rows: [],
                    totalItems: 0,
                });
            }
        }
        SpinnerService.stop(this.spinnerSelector);
    }
}
