import { Component, OnDestroy, OnInit, ViewChild, TemplateRef, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';

import { TranslateService } from '@ngx-translate/core';
import { SagTableControl, SagTableColumn } from 'sag-table';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';

import { ExternalVendorService } from '../../services/external-vendor.service';
import { ExternalVendorDetailRequest } from '../../model/external-vendor-item.model';
import { ExternalVendorResponse } from '../../model/external-vendor-response.model';
import { ExternalVendorSearch } from '../../model/external-vendor-search.model';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { DeleteConfirmModalComponent } from 'src/app/shared/common/components/delete-confirm-modal/delete-confirm-modal.component';
import { ExternalVendorImportModalComponent } from '../external-vendor-import-modal/external-vendor-import-modal.component';

@Component({
    selector: 'backoffice-app-external-vendor-list',
    templateUrl: './external-vendor-list.component.html',
    styleUrls: ['./external-vendor-list.component.scss']
})
export class ExternalVendorListComponent implements OnInit, OnDestroy, SagTableControl {
    // externalVendorResponse: ExternalVendorResponse;
    externalVendors: ExternalVendorDetailRequest[];
    // search$ = new Subject();
    countries = new Array<any>();
    // selectedCoutry: any;
    selectedVendorId: any;
    filters = new ExternalVendorSearch();
    deliveryProfiles: any[];
    availTypes = new Array<any>();
    page = 0;
    pagination: { itemsPerPage: number, currentPage: number, totalItems: number };
    sagNotifier: NotificationModel;
    isPaginationShown: boolean;

    private destroy$ = new Subject();
    private modalConfig: ModalOptions = {
        keyboard: true,
        backdrop: true,
        ignoreBackdropClick: true
    };
    private importModalRef: BsModalRef = null;
    private deleteModalRef: BsModalRef = null;
    private readonly OFFSITE = 10;

    columns = [];
    private tableRequest: any;
    private tableCallback: any;

    // @ViewChild('deletingModal', { static: true }) deletingModal: ElementRef;
    // @ViewChild('importModal', { static: true }) importModal: ElementRef;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private router: Router,
        private modalService: BsModalService,
        private externalVendorService: ExternalVendorService,
        private translateService: TranslateService) { }

    ngOnInit() {
        this.setInitData();
    }

    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();

        if (this.importModalRef) {
            this.importModalRef.hide();
        }

        if (this.deleteModalRef) {
            this.deleteModalRef.hide();
        }
    }

    searchTableData({ request, callback }): void {
        this.tableCallback = callback;
        this.tableRequest = request;

        this.setFilterData();
        this.setSortData();
        this.search();
    }

    sort(sortField, sortDir) {
        this.filters.resetSort();
        const direction = sortDir === 'desc' ? 1 : 0;
        const field = this.filters.getSortFieldByCriteriaName(sortField)
        this.filters.sort[field] = direction !== 0;
    }

    createOrUpdate(id?: string) {
        if (id) {
            this.router.navigate(['home/external-vendors', id]);
        } else {
            this.router.navigate(['home/external-vendors/create']);
        }
    }

    delete(id: string, name = '') {
        this.modalConfig.initialState = {
            modalTitle: 'COMMON.LABEL.CONFIRMATION',
            deleteMessage: this.translateService.instant('EXTERNAL_VENDOR.DELETE_CONFIRM'),
            deleteItemName: name
        };
        this.selectedVendorId = id;
        this.deleteModalRef = this.modalService.show(DeleteConfirmModalComponent, this.modalConfig);
        this.deleteModalRef.content.onConfirm = (onError, onSucces) => this.deleteItem(id, onError, onSucces);
    }

    importCSV() {
        this.importModalRef = this.modalService.show(ExternalVendorImportModalComponent, {
            keyboard: true,
            backdrop: true,
            ignoreBackdropClick: true
        });
        this.importModalRef.content.closeModal = () => {
            this.closeImportModal();
        };
    }

    updatePage(page: number) {
        this.pagination.currentPage = page;
        this.page = page;
        this.filters.page = this.page - 1;
        this.search();
    }

    deleteItem(id, onError, onSuccess) {
        SpinnerService.start();
        this.externalVendorService.delete(id).pipe(
            finalize(() => SpinnerService.stop())
        ).subscribe(() => {
            onSuccess('COMMON.MESSAGE.DELETE_SUCCESFULLY');
            this.closeDeletingModal()
        }, () => {
            onError('COMMON.MESSAGE.DELETE_UNSUCCESFULLY')
        });
    }

    private setFilterData() {
        const { filter, page } = this.tableRequest;
        this.filters.country = filter.country ? filter.country.value : '';
        this.filters.vendorId = filter.vendorId ? filter.vendorId : '';
        this.filters.vendorName = filter.vendorName ? filter.vendorName : '';
        this.filters.vendorPriority = filter.vendorPriority ? filter.vendorPriority : '';
        this.filters.deliveryProfileName = filter.deliveryProfileName ? filter.deliveryProfileName.value : '';
        this.filters.availabilityTypeId = filter.availabilityTypeId ? filter.availabilityTypeId.value : '';

        this.filters.page = page - 1;
    }

    private setSortData() {
        const { sort } = this.tableRequest;
        if (sort.field) {
            this.sort(sort.field, sort.direction)
        }
    }


    private buildColumns() {
        this.columns = [
            {
                id: 'country',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.COUNTRY',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                type: 'select',
                selectSource: this.countries,
                defaultValue: '',
                width: '15%',
            },
            {
                id: 'vendorId',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.ID',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                width: '12%',
            },
            {
                id: 'vendorName',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.NAME',
                cellClass: 'text-left',
                filterable: true,
                sortable: true,
                width: '17%',
            },
            {
                id: 'vendorPriority',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.PRIORITY',
                cellClass: 'text-right',
                filterable: true,
                sortable: true,
                width: '15%',
            },
            {
                id: 'deliveryProfileName',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.DELIVERY_PROFILE_NAME',
                cellClass: 'text-left',
                type: 'select',
                selectSource: this.deliveryProfiles,
                defaultValue: '',
                filterable: true,
                sortable: true,
                width: '14%',
            },
            {
                id: 'availabilityTypeId',
                i18n: 'EXTERNAL_VENDOR.TABLE_COLUMNS.AVAILABILITY_TYPE',
                type: 'select',
                selectSource: this.availTypes,
                defaultValue: '',
                cellClass: 'text-left',
                filterable: true,
                width: '15%',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
                cellClass: 'align-middle',
                width: '12%',
            },
        ] as SagTableColumn[];
    }

    private search() {
        this.externalVendors = [];
        this.sagNotifier = null;

        this.externalVendorService
            .search(this.filters, { page: this.filters.page, size: this.filters.size })
            .pipe(
                takeUntil(this.destroy$)
            )
            .subscribe(
                data => {
                    this.externalVendors = [...data.content];
                    this.setPagination(this.OFFSITE, this.page, data.totalElements);
                    this.isPaginationShown = data && data.totalElements > this.OFFSITE;
                    this.setTableData();
                }, () => {
                    this.externalVendors = [];
                    this.sagNotifier = { messages: ['COMMON.MESSAGE.EMPTY_LIST'], status: false };
                    if (this.page > 1) {
                        this.updatePage(this.page - 1);
                    }
                    this.setTableData();
                }
            );
    }

    private setPagination(offsite: number, page: number, totalItems: number) {
        this.pagination = {
            itemsPerPage: offsite,
            currentPage: page,
            totalItems: totalItems || 0
        };
    }

    private setTableData() {
        if (!this.tableCallback || !this.tableRequest) {
            return;
        }

        if (this.externalVendors.length > 0) {
            this.tableCallback({
                rows: this.externalVendors,
                totalItems: this.pagination.totalItems,
                page: this.filters.page + 1,
                itemsPerPage: this.pagination.itemsPerPage
            });
        } else {
            this.tableCallback({
                rows: [],
                totalItems: 0,
            });
        }
    }

    private setInitData() {
        const allOption = 'COMMON.LABEL.ALL';
        this.externalVendorService.getInitData().subscribe(aggregation => {
            this.countries = aggregation.countries.map(country => {
                return {
                    label: country.description,
                    value: country.code
                };
            });
            this.countries = [{ label: allOption, value: '' }, ...this.countries];

            this.availTypes = aggregation.availabilityType.map(value => {
                return {
                    label: value,
                    value
                };
            });

            this.deliveryProfiles = aggregation.deliveryProfile.map((profile) => {
                return {
                    label: profile.deliveryProfileName,
                    value: profile.deliveryProfileName
                };
            });

            this.deliveryProfiles = [{ label: allOption, value: '' }, ...this.deliveryProfiles];

            this.availTypes = [{ label: allOption, value: '' }, ...this.availTypes];
            this.buildColumns();
        });
    }

    private closeDeletingModal() {
        this.deleteModalRef.hide();
        this.search();
    }

    private closeImportModal() {
        this.importModalRef.hide();
        this.search();
    }
}
