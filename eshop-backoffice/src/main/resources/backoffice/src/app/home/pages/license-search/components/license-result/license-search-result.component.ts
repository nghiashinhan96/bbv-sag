import { OnInit, OnDestroy, Component, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SagTableControl, SagTableColumn, TablePage } from 'sag-table';
import { EMPTY_STRING, MESSAGE_TIMEOUT } from 'src/app/core/conts/app.constant';
import { Subscription } from 'rxjs';
import { StringHelper } from 'src/app/core/utils/string.util';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { LicenseRequestModel, LicenseRequestModelSorting } from '../../models/license-request.model';
import { LicenseExportService } from '../../services/license-export.service';
import { EXPORT_LICENSE_MODE, LICENSE_SORT_REQUEST_COLUMNS } from '../../enums/license.enum';
import { LicenseModel } from '../../models/license.model';
import { LicenseSearchService } from '../../services/license-search.service';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { LICENSE_SEARCH_PAGE_SIZE } from '../../const/license-search.const';
import { LicenseUpdateService } from '../../services/license-update.service';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'backoffice-license-search-result',
    templateUrl: './license-search-result.component.html',
    styleUrls: ['./license-search-result.component.scss']
})
export class LicenseSearchResultsComponent implements OnInit, OnDestroy, SagTableControl {
    columns = [];
    tableCallback: any;
    licensePackage: any;
    editLicenseData: any;
    deleteLicenseData: any;
    tableRequest: any;
    canExport: boolean = false;
    hasParams: boolean = false;
    isExpandExportOptions: boolean = false;
    exportMode: EXPORT_LICENSE_MODE;
    EXPORT_LICENSE_MODE = EXPORT_LICENSE_MODE;
    notifier: NotificationModel;
    error: NotificationModel = { messages: ['HOME.LABEL.LICENSE_SEARCH.EDIT_MESSAGE.MISSING_PARAMS'], status: false };
    page = new TablePage();

    readonly spinnerSelector = '.license-table';

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;
    @ViewChild('exportModal', { static: true }) exportModal: TemplateRef<any>;
    @ViewChild('editLicenseModal', { static: true }) editLicenseModal: TemplateRef<any>;
    @ViewChild('deleteLicenseModal', { static: true }) deleteLicenseModal: TemplateRef<any>;

    private exportModalRef: BsModalRef;
    private editLicenseModalRef: BsModalRef;
    private deleteLicenseModalRef: BsModalRef;

    private isFirstSearchByParams: boolean = true;
    private subs = new Subscription();
    private licenseRequestModel: LicenseRequestModel;

    constructor(
        private route: ActivatedRoute,
        private licenseSearchService: LicenseSearchService,
        private licenseUpdateService: LicenseUpdateService,
        private licenseExportService: LicenseExportService,
        private modalService: BsModalService,
        private customerService: CustomerService
    ) { }

    ngOnInit(): void {
        this.getLicensePackage();
        this.route.params.subscribe((params) => {
            if (params) {
                try {
                    const inputParam: LicenseRequestModel = JSON.parse(params['queryParam']);
                    this.licenseRequestModel = new LicenseRequestModel(inputParam);
                    this.buildColumns();
                    this.hasParams = true;
                } catch (ex) {
                    // wrong params
                }
            } else {
                this.hasParams = false;
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    buildColumns() {
        this.columns = [
            {
                id: 'customerNr',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.CUSTOMER_NUMBER',
                filterable: true,
                sortable: true,
                width: '10%',
                defaultValue: this.licenseRequestModel.customerNr || EMPTY_STRING
            },
            {
                id: 'typeOfLicense',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.LICENSE_TYPE',
                filterable: true,
                sortable: true,
                width: '10%',
                defaultValue: this.licenseRequestModel.typeOfLicense || EMPTY_STRING
            },
            {
                id: 'packName',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.LICENSE_NAME',
                filterable: true,
                sortable: true,
                width: '20%',
                defaultValue: this.licenseRequestModel.packName || EMPTY_STRING
            },
            {
                id: 'beginDate',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.FROM',
                filterable: false,
                sortable: true,
                width: '15%',
                defaultValue: this.licenseRequestModel.beginDate || EMPTY_STRING
            },
            {
                id: 'endDate',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.TO',
                filterable: false,
                sortable: true,
                width: '15%',
                defaultValue: this.licenseRequestModel.endDate || EMPTY_STRING
            },
            {
                id: 'quantity',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.QUANTITY',
                filterable: true,
                sortable: true,
                width: '10%',
                defaultValue: this.licenseRequestModel.quantity || EMPTY_STRING
            },
            {
                id: 'quantityUsed',
                i18n: 'HOME.LABEL.LICENSE_SEARCH.LICENSE_LIST_TABLE.NUMBER_USED',
                filterable: true,
                sortable: true,
                width: '10%',
                defaultValue: this.licenseRequestModel.quantityUsed || EMPTY_STRING
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                width: '10%',
                cellTemplate: this.colActions,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    searchTableData({ request, callback }): void {
        this.tableRequest = request;
        this.tableCallback = callback;
        this.searchLicenses(this.tableRequest);
        return;
    }

    editLicense(license) {
        this.editLicenseData = license;
        this.editLicenseModalRef = this.modalService.show(this.editLicenseModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    saveLicense() {
        this.doSearch();
        this.closeEditModal();
        this.handleNotifier(['HOME.LABEL.LICENSE_SEARCH.EDIT_MESSAGE.SUCCESS'], false);
    }

    closeEditModal() {
        if (this.editLicenseModalRef) {
            this.editLicenseModalRef.hide();
        }
    }

    deleteLicense(license) {
        this.deleteLicenseData = license;
        this.deleteLicenseModalRef = this.modalService.show(this.deleteLicenseModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false
        });
    }

    removeLicense() {
        this.closeDeleteModal();

        if (!this.deleteLicenseData) {
            return;
        }

        const licenseId = this.deleteLicenseData.id;
        this.subs.add(
            this.licenseUpdateService.deleteLicense(licenseId)
                .subscribe(res => {
                    const callback = () => {
                        this.handleNotifier(['HOME.LABEL.LICENSE_SEARCH.DELETE_MESSAGE.SUCCESS'], false);
                    }
                    this.doSearch(callback);
                    
                }, ({ error }) => {
                    this.handleError(error);
                }
                )
        )
    }

    closeDeleteModal() {
        if (this.deleteLicenseModalRef) {
            this.deleteLicenseModalRef.hide();
        }
    }

    export(mode: EXPORT_LICENSE_MODE) {
        if (!this.canExport) {
            this.handleNotifier(['HOME.LABEL.LICENSE_SEARCH.ERROR_EXPORT_EMPTY_TOKEN'], true);
        } else {
            this.subs.add(
                this.licenseExportService.exportLicenses(mode, this.licenseRequestModel).subscribe(res => {
                    if (res) {
                        this.notifier = res;
                    }
                })
            );
        }
    }

    openPopupConfirmExport(mode: EXPORT_LICENSE_MODE) {
        this.exportMode = mode;
        this.exportModalRef = this.modalService.show(this.exportModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: true,
        });
    }

    closeExportModal() {
        if (this.exportModalRef) {
            this.exportModalRef.hide();
        }
    }

    closeModalAndExport() {
        this.closeExportModal();
        this.export(this.exportMode);
    }

    private updatePagination(res) {
        const { totalElements } = res;
        const numberOfElements = res.pageable.size;
        const currentPage = res.pageable.number + 1;
        const licenseList = this.transferDataToTable(res.content);
        if (this.tableCallback) {
            this.tableCallback({
                rows: licenseList,
                page: currentPage,
                totalItems: totalElements,
                itemsPerPage: numberOfElements,
            });
        }
    }

    private transferDataToTable(response) {
        if (!response) {
            return;
        }

        const licenseList = new Array<LicenseModel>();
        for (const item of response) {
            const id: number = item.id;
            const customerNr: string = StringHelper.getEmptyStringIfNull(item.customerNr);
            const packName: string = StringHelper.getEmptyStringIfNull(item.packName);
            const beginDate: string = StringHelper.getEmptyStringIfNull(item.beginDate);
            const endDate: string = StringHelper.getEmptyStringIfNull(item.endDate);
            const typeOfLicense: string = StringHelper.getEmptyStringIfNull(item.typeOfLicense);
            const quantity: number = item.quantity || 0;
            const quantityUsed: number = item.quantityUsed || 0;
            const licenseSearchDto = new LicenseModel({
                id, customerNr, packName, beginDate, endDate, typeOfLicense, quantity, quantityUsed
            });
            licenseList.push(licenseSearchDto);
        }
        return licenseList;
    }

    private searchLicenses(request) {
        if (!request) {
            return;
        }

        let { page, sort } = request;
        let filter = request.filter;

        if (this.isFirstSearchByParams) {
            filter.affiliate = this.licenseRequestModel.affiliate || filter.affiliate;
            filter.customerNr = this.licenseRequestModel.customerNr || filter.customerNr;
            filter.packName = this.licenseRequestModel.packName || filter.packName;
            filter.beginDate = this.licenseRequestModel.beginDate || filter.beginDate;
            filter.endDate = this.licenseRequestModel.endDate || filter.endDate;
            this.isFirstSearchByParams = false;
        }

        const affiliate = this.licenseRequestModel.affiliate || '';
        this.licenseRequestModel = new LicenseRequestModel(filter);
        this.licenseRequestModel.affiliate = affiliate;
        this.licenseRequestModel.size = LICENSE_SEARCH_PAGE_SIZE;
        this.licenseRequestModel.page = page - 1;
        if (sort.field) {
            this.sort(sort.field, sort.direction);
        }

        this.doSearch();
    }

    private sort(columnName, direction) {
        if (!columnName || !direction) {
            return;
        }

        const isDesc = direction === 'desc' ? true : false;
        const licenseSorting = new LicenseRequestModelSorting();

        switch (columnName) {
            case LICENSE_SORT_REQUEST_COLUMNS.TYPE_OF_LICENSE:
                licenseSorting.orderByTypeOfLicenseDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.CUSTOMER_NUMBER:
                licenseSorting.orderByCustomerNrDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.PACK_NAME:
                licenseSorting.orderByPackNameDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.BEGIN_DATE:
                licenseSorting.orderByBeginDateDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.END_DATE:
                licenseSorting.orderByEndDateDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.QUANTITY:
                licenseSorting.orderByQuantityDesc = isDesc;
                break;
            case LICENSE_SORT_REQUEST_COLUMNS.QUANTITY_USED:
                licenseSorting.orderByQuantityUsedDesc = isDesc;
                break;
            default:
                break;
        }
        this.licenseRequestModel.sorting = licenseSorting;
    }

    private doSearch(callback?) {
        this.subs.add(this.licenseSearchService.searchLicense(this.licenseRequestModel)
        .pipe(finalize(() => {
            if (callback) {
                callback();
            }
        }))
        .subscribe(res => {
            if (res && res.totalElements > 0) {
                this.updatePagination(res);
                this.canExport = true;
            } else {
                this.canExport = false;
                if (this.tableCallback) {
                    this.tableCallback({
                        rows: [],
                        totalItems: 0,
                    });
                }
            }
        }));
    }

    private getLicensePackage() {
        this.customerService.getAllLicenseTypes().subscribe((data) => {
            if (!data) {
                return;
            }

            const res: any = data;
            const licensePackage = [];
            this.licensePackage = licensePackage.concat(
                res.map((item) => {
                    return { value: item.packName.trim(), label: item.packName.trim(), quantity: item.quantity };
                })
            );
        });
    }

    private handleNotifier(message, isError) {
        this.notifier = { messages: message, status: !isError };
        setTimeout(() => {
            this.notifier = undefined;
        }, MESSAGE_TIMEOUT);
    }

    private handleError(error) {
        if (!error) {
            return;
        }

        const errorMess = ApiUtil.handleErrorReponse(error);
        this.handleNotifier([errorMess], true);
    }
}