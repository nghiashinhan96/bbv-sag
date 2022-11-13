import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';

import { DeliveryProfileImporterComponent } from '../delivery-profile-importer/delivery-profile-importer.component';
import { DeliveryProfileService } from '../../services/delivery-profile.service';
import { DeliveryProfileCriteria } from '../../models/delivery-profile-criteria.model';
import { DeliveryProfileModel } from '../../models/delivery-profile.model';
import { SagTableControl, SagTableColumn } from 'sag-table';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import { DeleteConfirmModalComponent } from 'src/app/shared/common/components/delete-confirm-modal/delete-confirm-modal.component';

@Component({
    selector: 'backoffice-delivery-profiles',
    templateUrl: './delivery-profiles.component.html',
    styleUrls: ['./delivery-profiles.component.scss']
})
export class DeliveryProfilesComponent implements OnInit, OnDestroy, SagTableControl {

    searchForm: FormGroup;
    pagination = {
        itemsPerPage: 0,
        currentPage: 0,
        totalItems: 0
    };

    columns = [];
    private tableRequest: any;
    private tableCallback: any;

    coutryOptions = [];
    distributionOptions = [];
    branchOptions = [];
    rows: DeliveryProfileModel[];
    private bsModalRef: BsModalRef;
    private searchCriteria: DeliveryProfileCriteria = new DeliveryProfileCriteria();

    @ViewChild('filterVendorCutOffTime', { static: true }) filterVendorCutOffTime: TemplateRef<any>;
    @ViewChild('filterLastDelivery', { static: true }) filterLastDelivery: TemplateRef<any>;
    @ViewChild('filterLastestTime', { static: true }) filterLastestTime: TemplateRef<any>;
    @ViewChild('filterDeliveryDuration', { static: true }) filterDeliveryDuration: TemplateRef<any>;

    @ViewChild('colVendorCutOffTime', { static: true }) colVendorCutOffTime: TemplateRef<any>;
    @ViewChild('colLastDelivery', { static: true }) colLastDelivery: TemplateRef<any>;
    @ViewChild('colLatestTime', { static: true }) colLatestTime: TemplateRef<any>;
    @ViewChild('colDeliveryDuration', { static: true }) colDeliveryDuration: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private modalService: BsModalService,
        private deliveryProfileService: DeliveryProfileService,
        private router: Router,
        private activedRoute: ActivatedRoute,
        private builder: FormBuilder,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.searchForm = this.builder.group({
            country: EMPTY_STRING,
            deliveryProfileName: EMPTY_STRING,
            distributionBranchCode: EMPTY_STRING,
            deliveryBranchCode: EMPTY_STRING,
            vendorCutOffTime: null,
            lastestTime: null,
            lastDelivery: null,
            deliveryDuration: null
        });

        this.deliveryProfileService.initDropdownData().subscribe(result => {
            const res: any = result;
            const all = [{ label: this.translateService.instant('COMMON.LABEL.ALL'), value: ' ' }];

            this.distributionOptions = (res.distributionBranchSearch || []).map(
                item => ({ value: item && item.branchCode && item.branchCode.toString() || '', label: item && item.branchCode && item.branchCode.toString() || '' }));

            this.distributionOptions = all.concat(this.distributionOptions);

            this.branchOptions = (res.deliveryBranchSearch || []).map(
                item => ({ value: item && item.branchCode && item.branchCode.toString() || '', label: item && item.branchCode && item.branchCode.toString() || '' }));

            this.branchOptions = all.concat(this.branchOptions);

            this.coutryOptions = (res.countries || []).map(
                item => ({ value: item && item.code && item.code.toString() || '', label: item && item.description && item.description.toString() || '' }));

            this.coutryOptions = all.concat(this.coutryOptions);

            this.buildColumns();
        });

    }

    ngOnDestroy(): void {
        if (this.bsModalRef) {
            this.bsModalRef.hide();
        }
    }

    searchTableData({ request, callback }): void {
        this.tableRequest = request;
        this.tableCallback = callback;

        this.setFilterData();

        this.onSearch();
    }

    setFilterData() {
        const { filter, sort, page } = this.tableRequest;
        this.searchCriteria.country = filter.country ? filter.country.value : EMPTY_STRING;
        this.searchCriteria.deliveryProfileName = filter.deliveryProfileName ? filter.deliveryProfileName : EMPTY_STRING;
        this.searchCriteria.distributionBranchCode = filter.distributionBranchCode ? filter.distributionBranchCode.value : EMPTY_STRING;
        this.searchCriteria.deliveryBranchCode = filter.deliveryBranchCode ? filter.deliveryBranchCode.value : EMPTY_STRING;

        if (sort.field) {
            this.onSort(sort.field, sort.direction === 'desc');
        }

        this.searchCriteria.page = page - 1;
    }

    onSort(sortField, isDesc: boolean) {
        this.searchCriteria.setSortModel(sortField, isDesc);
    }

    // onPageChange(pageNumber: number) {
    //     this.searchCriteria.page = pageNumber - 1;
    //     this.onSearch();
    // }

    importCSV() {
        this.bsModalRef = this.modalService.show(DeliveryProfileImporterComponent, {
            ignoreBackdropClick: true,
            class: 'file-uploader'
        });

        this.bsModalRef.content.onClose = () => {
            this.onSearch();
        };
    }

    createDeliveryProfile() {
        this.router.navigate(['../', 'new'], { relativeTo: this.activedRoute });
    }

    goToDetailPage(id: number) {
        this.router.navigate(['../', id], { relativeTo: this.activedRoute });
    }

    deleteProfile(id: number, name = '') {
        this.bsModalRef = this.modalService.show(DeleteConfirmModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-md',
            initialState: {
                modalTitle: 'COMMON.LABEL.CONFIRMATION',
                deleteMessage: 'DELIVERY_PROFILE.DELETE_CONFIRMATION',
                deleteItemName: name
            }
        });
        this.bsModalRef.content.onConfirm = () => {
            this.deliveryProfileService.deleteDeliveryProfile(id).subscribe(res => {
                this.onSearch();
                this.bsModalRef.hide();
            });
        };
    }

    onVendorCutoffTimeChange($event) {
        this.searchCriteria.vendorCutOffTime = $event;
        this.onSearch();
    }

    onLastDeliveryTimeChange($event) {
        this.searchCriteria.lastDelivery = $event;
        this.onSearch();
    }

    onLastestTimeChange($event) {
        this.searchCriteria.lastestTime = $event;
        this.onSearch();
    }

    onDeliveryDurationChange($event) {
        this.searchCriteria.deliveryDuration = $event;
        this.onSearch();
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'country',
                i18n: 'DELIVERY_PROFILE.COUNTRY',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                type: 'select',
                selectSource: this.coutryOptions,
                defaultValue: this.coutryOptions[0],
                width: '11%',
            },
            {
                id: 'deliveryProfileName',
                i18n: 'DELIVERY_PROFILE.DELIVERY_PROFILE_NAME',
                defaultValue: EMPTY_STRING,
                filterable: true,
                sortable: true,
                width: '12%',
            },
            {
                id: 'distributionBranchCode',
                i18n: 'DELIVERY_PROFILE.DISTRIBUTION',
                cellClass: 'text-left',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.distributionOptions,
                defaultValue: this.distributionOptions[0],
                width: '11%',
            },
            {
                id: 'deliveryBranchCode',
                i18n: 'DELIVERY_PROFILE.DELIVERY_BRANCH',
                cellClass: 'text-left',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.branchOptions,
                defaultValue: this.branchOptions[0],
                width: '11%',
            },
            {
                id: 'vendorCutOffTime',
                i18n: 'DELIVERY_PROFILE.CUT_OFF_TIME',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                filterTemplate: this.filterVendorCutOffTime,
                cellTemplate: this.colVendorCutOffTime,
                width: '11%',
            },
            {
                id: 'lastDelivery',
                i18n: 'DELIVERY_PROFILE.LAST_DELIVERY',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                filterTemplate: this.filterLastDelivery,
                cellTemplate: this.colLastDelivery,
                width: '11%',
            },
            {
                id: 'latestTime',
                i18n: 'DELIVERY_PROFILE.LATEST_TIME',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                filterTemplate: this.filterLastestTime,
                cellTemplate: this.colLatestTime,
                width: '11%',
            },
            {
                id: 'deliveryDuration',
                i18n: 'DELIVERY_PROFILE.DELIVERY_DURATION',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                filterTemplate: this.filterDeliveryDuration,
                cellTemplate: this.colDeliveryDuration,
                width: '11%',
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

    private setTableData() {
        if (!this.tableCallback) {
            return;
        }

        this.tableCallback({
            rows: this.rows,
            totalItems: this.pagination.totalItems,
            itemsPerPage: this.pagination.itemsPerPage,
            page: this.pagination.currentPage
        });
    }

    private onSearch(searchModel?: DeliveryProfileCriteria) {
        if (!!searchModel) {
            this.searchCriteria.setSearchModel(searchModel);
        }
        this.deliveryProfileService.search(this.searchCriteria.dto).subscribe(result => {
            const res: any = result;
            this.rows = res.content;
            this.pagination.itemsPerPage = res.size;
            this.pagination.currentPage = res.number + 1;
            this.pagination.totalItems = res.totalElements;
            this.setTableData();
        }, err => {
            this.pagination.itemsPerPage = 0;
            this.pagination.currentPage = 0;
            this.pagination.totalItems = 0;
            this.rows = [];
            this.setTableData();
        });
    }
}
