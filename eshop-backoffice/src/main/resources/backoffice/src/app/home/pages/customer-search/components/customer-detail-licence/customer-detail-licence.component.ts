import { DatePipe } from '@angular/common';
import { Component, ElementRef, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { SagTableColumn, SagTableControl, TablePage } from 'sag-table';

import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { DateUtil } from 'src/app/core/utils/date.util';
import { CustomerService } from 'src/app/home/services/customer/customer.service';

@Component({
    selector: 'backoffice-customer-detail-licence',
    templateUrl: './customer-detail-licence.component.html',
})
export class CustomerDetailLicenceComponent implements OnInit, SagTableControl {
    @Input() customerNr: any;
    @ViewChild('collapseBtn', { static: true }) collapseBtn: ElementRef;

    @ViewChild('colEdit', { static: true }) colEdit: TemplateRef<any>;
    @ViewChild('colRemove', { static: true }) colRemove: TemplateRef<any>;

    @ViewChild('creatingLicenceModal', { static: true }) creatingLicenceModal: ElementRef;
    @ViewChild('editingLicenceModal', { static: true }) editingLicenceModal: ElementRef;
    @ViewChild('deletingLicenceModal', { static: true }) deletingLicenceModal: ElementRef;

    customerLicence: any;
    public licenceTypes: any;
    // public formatDate = DateUtil.dateFormat;
    public creatingLicenceModalId = 'create-lisence-modal';
    public editingLicenceModalId = 'edit-lisence-modal';
    public deletingLicenceModalId = 'delete-licence-modal';

    private creatingLicenceModalRef: BsModalRef = null;
    private editingLicenceModalRef: BsModalRef = null;
    private deletingLicenceModalRef: BsModalRef = null;

    public deletingLicence: any;

    isFormExpanded = false;

    columns = [];
    tableCallback: any;
    tableRequest: any;

    public page = new TablePage();

    public creatingLicence;
    public editingLicence;


    constructor(
        private customerService: CustomerService,
        private datePipe: DatePipe,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        // this.getCustomerLicence(this.customerNr, this.page);
        this.getLicenseTypes();
        this.buildColumns();
    }

    searchTableData({ request, callback }) {
        this.tableCallback = callback;
        this.tableRequest = request;
        const { page } = request;
        this.page.currentPage = page;
        this.getCustomerLicence(this.customerNr, this.page);
    }

    buildColumns() {
        this.columns = [
            {
                id: 'typeOfLicense',
                i18n: 'CUSTOMER.LICENSE.LICENSE_TYPE',
                cellClass: 'align-middle',
                width: '18%',
            },
            {
                id: 'packName',
                i18n: 'CUSTOMER.LICENSE.PACK_NAME',
                width: '18%',
            },
            {
                id: 'beginDate',
                i18n: 'CUSTOMER.LICENSE.FROM',
                cellClass: 'align-middle',
                width: '16%',
            },
            {
                id: 'endDate',
                i18n: 'CUSTOMER.LICENSE.TO',
                cellClass: 'align-middle',
                width: '16%',
            },
            {
                id: 'quantity',
                i18n: 'CUSTOMER.LICENSE.QUANTITY',
                cellClass: 'align-middle',
                width: '10%',
            },
            {
                id: 'quantityUsed',
                i18n: 'CUSTOMER.LICENSE.USED_QUANTITY',
                cellClass: 'align-middle',
                width: '10%',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colEdit,
                cellClass: 'align-middle',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colRemove,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    initCreateLicensePopup() {
        this.creatingLicence = {
            modalId: this.creatingLicenceModalId,
            customerNr: this.customerNr,
            licenceTypes: this.licenceTypes,
            fromDate: DatePickerUtil.initDate(),
            toDate: DatePickerUtil.initDate(12),
            quantity: this.licenceTypes[0].quantity,
        };
        this.openModal(this.creatingLicenceModalId);
    }

    initEditLicensePopup(licence) {
        const type = this.licenceTypes.filter((item) => item.packName.trim() === licence.packName.trim())[0];
        this.editingLicence = Object.assign({
            modalId: this.editingLicenceModalId,
            selectedLicenceId: licence.id,
            customerNr: this.customerNr,
            licenceTypes: Object.assign([], this.licenceTypes),
            selectedType: type ? Object.assign({}, type) : null,
            fromDate: DatePickerUtil.buildDataDatePicker(new Date(licence.beginDate)),
            toDate: DatePickerUtil.buildDataDatePicker(new Date(licence.endDate)),
            quantity: licence.quantity,
        });

        this.openModal(this.editingLicenceModalId);
    }

    // updatePage(event) {
    //     this.page.currentPage = event;
    //     this.getCustomerLicence(this.customerNr, this.page);
    // }

    saveLicence() {
        this.reloadListLicence();
        if (this.editingLicenceModalRef) {
            this.editingLicenceModalRef.hide();
        }
    }

    deleteLicence(deletingLicence) {
        this.deletingLicence = deletingLicence;
        this.openModal(this.deletingLicenceModalId);
    }

    handleDeleteSuccessEvent() {
        this.getCustomerLicence(this.customerNr, this.page);
        this.getLicenseTypes();
    }

    closeModal(modalId) {
        switch (modalId) {
            case this.creatingLicenceModalId:
                this.creatingLicenceModalRef.hide();
                break;
            case this.editingLicenceModalId:
                this.editingLicenceModalRef.hide();
                break;
            case this.deletingLicenceModalId:
                this.deletingLicenceModalRef.hide();
                break;
            default:
                break;
        }
    }

    closeDeleteModal() {
        this.closeModal(this.deletingLicenceModalId);
    }

    open() {
        this.isFormExpanded = true;
    }

    private openModal(modalId) {
        switch (modalId) {
            case this.creatingLicenceModalId:
                this.creatingLicenceModalRef = this.modalService.show(this.creatingLicenceModal, {
                    class: 'modal-user-form  modal-lg',
                    ignoreBackdropClick: false,
                });
                break;
            case this.editingLicenceModalId:
                this.editingLicenceModalRef = this.modalService.show(this.editingLicenceModal, {
                    class: 'modal-user-form  modal-lg',
                    ignoreBackdropClick: false,
                });
                break;
            case this.deletingLicenceModalId:
                this.deletingLicenceModalRef = this.modalService.show(this.deletingLicenceModal, {
                    class: 'modal-user-form  modal-lg',
                    ignoreBackdropClick: false,
                });
                break;
            default:
                break;
        }

    }

    private getCustomerLicence(customerNr, page) {
        this.customerService
            .getCustomerLicence(customerNr, page.itemsPerPage, page.currentPage)
            .subscribe((result) => {
                const res: any = result;
                this.customerLicence = res;
                this.page.itemsPerPage = res.size;
                this.page.totalItems = res.totalElements;

                // convert date format
                const licenseFormatDate = 'yyyy-MM-dd';
                const rows = [...this.customerLicence.content];
                rows.forEach((row) => {
                    row.beginDate = row.beginDate ? this.datePipe.transform(row.beginDate, licenseFormatDate) || '' : '';
                    row.endDate = row.endDate ? this.datePipe.transform(row.endDate, licenseFormatDate) || '' : '';
                });

                if (this.tableCallback) {
                    this.tableCallback({
                        rows,
                        page: this.page.currentPage,
                        totalItems: this.page.totalItems,
                        itemsPerPage: this.page.itemsPerPage,
                    });
                }
            });
    }

    private getLicenseTypes(): any {
        this.customerService.getAllLicenseTypes().subscribe((res) => {
            if (res) {
                this.licenceTypes = res;
            }
        });
    }

    private reloadListLicence() {
        this.getCustomerLicence(this.customerNr, this.page);
    }
}
