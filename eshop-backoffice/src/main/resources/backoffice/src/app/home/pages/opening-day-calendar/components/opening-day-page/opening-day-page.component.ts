import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Subject, timer } from 'rxjs';
import { take, finalize } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

import { FilterDateService } from '../../service/filter-date.service';
import { INTERVAL_THIRTY_ONE_DAYS } from '../../constant';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { SortModel } from 'src/app/shared/models/sort.model';
import { OpeningDayCalendarService } from '../../service/opening-day-calendar.service';
import { OpeningDayModel, SearchCriteria, SearchCriteriaModel } from '../../model/opening-day.model';

@Component({
    selector: 'backoffice-opening-day-page',
    templateUrl: './opening-day-page.component.html',
    styleUrls: ['./opening-day-page.component.scss']
})
export class OpeningDayPageComponent implements OnInit {
    @ViewChild('fileUpload', { static: false }) fileUpload: ElementRef;
    @ViewChild('deletingModal', { static: true }) deletingModal: ElementRef;
    @ViewChild('importModal', { static: true }) importModal: ElementRef;

    workingCodes: Array<any> = [];
    openingDayList: OpeningDayModel[] = [];
    selectedOpeningDay: OpeningDayModel;
    noticeMessage: string;
    isSuccessful: boolean;
    itemsPerPage: number;
    totalElements: number;
    totalPages: number;
    resetFilterEvent = new Subject<boolean>();
    isLoaded: boolean;
    isClearInput: boolean;
    isUploading: boolean;
    isUploaded = true;
    uploadMessage: string;
    uploadPercentage = 0;
    isNotOverride = true;
    isNotImported = true;
    preFixName = 'default';
    label: string;
    fileName: string;

    private tableRequest: any;
    private tableCallback: any;

    criteria = this.initialCriteria();
    private pageModel;
    private sourceFile: any;

    private deletingModalRef: BsModalRef;
    private importModalRef: BsModalRef;

    private readonly TODAY = 0;
    private readonly FOR_2_DAYS = 1;
    private readonly FOR_7_DAYS = 2;
    private readonly FOR_31_DAYS = 3;

    // Date range was calculated from selected day
    private readonly INTERVAL_TWO_DAYS = 1; // one day plus selected day will be two days
    private readonly INTERVAL_SEVEN_DAYS = 6; // similar to above case
    private readonly ALL_CODES = 'ALL_CODES';

    private readonly OPTION_ALL = {
        value: '0',
        label: this.translateService.instant('COMMON.LABEL.ALL'),
        originalCode: this.ALL_CODES
    };

    constructor(
        private openingDayService: OpeningDayCalendarService,
        private route: ActivatedRoute,
        private translateService: TranslateService,
        private filterDateService: FilterDateService,
        private modalService: BsModalService) { }

    ngOnInit() {
        this.label = this.translateService.instant('COMMON.LABEL.NO_FILE_CHOSEN');
        this.route.data.subscribe((data: any) => {
            if(data && data.openingDayList) {
                this.workingCodes = [this.OPTION_ALL, ...data.openingDayList];
            }
        });
    }

    searchWithDateRange({ from, to }): void {
        this.criteria = this.initialCriteria(from, to);
        this.resetCriteria();
        this.search(this.pageModel);
        this.resetFilterEvent.next(true);
    }

    searchWithSelectedDay({ date }) {
        this.criteria.dateFrom = DatePickerUtil.getDateFromToDatePicker(date, true);
        this.criteria.dateTo = DatePickerUtil.getDateFromToDatePicker(date, true);
        this.resetCriteria();
        this.search();
        this.resetFilterEvent.next(true);
    }

    searchWithPredefinedRange({ filter }): void {
        const today = new Date();
        switch (filter) {
            case this.filterDateService.getFilterOption(this.TODAY).label:
                this.criteria.dateFrom = DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(today).date, true);
                this.criteria.dateTo = DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(today).date, true);
                break;
            case this.filterDateService.getFilterOption(this.FOR_2_DAYS).label:
                this.criteria.dateFrom = DatePickerUtil.getDateFromToDatePicker(this.filterDateService
                    .getDateFromGivenDate(DatePickerUtil.buildDataDatePicker(today).date, this.INTERVAL_TWO_DAYS).date, true);
                this.criteria.dateTo = DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(today).date, true);
                break;
            case this.filterDateService.getFilterOption(this.FOR_7_DAYS).label:
                this.criteria.dateFrom = DatePickerUtil.getDateFromToDatePicker(this.filterDateService
                    .getDateFromGivenDate(DatePickerUtil.buildDataDatePicker(today).date, this.INTERVAL_SEVEN_DAYS).date, true);
                this.criteria.dateTo = DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(today).date, true);
                break;
            case this.filterDateService.getFilterOption(this.FOR_31_DAYS).label:
                this.criteria.dateFrom = DatePickerUtil.getDateFromToDatePicker(this.filterDateService
                    .getDateFromGivenDate(DatePickerUtil.buildDataDatePicker(today).date, INTERVAL_THIRTY_ONE_DAYS).date, true);
                this.criteria.dateTo = DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(today).date, true);
                break;
        }
        this.resetCriteria();
        this.search();
        this.resetFilterEvent.next(true);
    }

    goToPage(page: number): void {
        const pageModel = { page, size: 10 };
        this.search(pageModel);
    }

    sortWith(sortModel: SortModel): void {
        switch (sortModel.field) {
            case 'SORT_COLUMN_DATE':
                this.criteria.orderDescByCountryName = null;
                this.criteria.orderDescByDate = sortModel.direction === 0 ? false : true;
                this.search();
                break;
            case 'SORT_COLUMN_LAND':
                this.criteria.orderDescByDate = null;
                this.criteria.orderDescByCountryName = sortModel.direction === 0 ? false : true;
                this.search();
                break;
        }
    }

    deleteOpeningDayItem(item: OpeningDayModel) {
        this.noticeMessage = EMPTY_STRING;
        this.selectedOpeningDay = item;
        this.showDeleteModal();
    }

    getFile(event) {
        this.sourceFile = event.target;
        this.fileName = this.sourceFile ? this.sourceFile.files.length > 0 ? this.sourceFile.files[0].name : EMPTY_STRING
            : EMPTY_STRING;
        this.isNotImported = this.sourceFile ? false : true;
    }

    overrideImport() {
        this.isUploaded = true;
        this.isUploading = false;
        this.uploadFile(true);
    }

    uploadFile(isOverridden?: boolean) {
        const files: FileList = this.sourceFile.files;
        this.isUploading = true;
        this.uploadPercentage = 0;
        this.uploadMessage = EMPTY_STRING;
        this.openingDayService.uploadOpeningDayFile(files.item(0), isOverridden)
            .subscribe(res => {
                this.uploadPercentage = 100;
                this.isUploaded = true;
                this.uploadMessage = 'COMMON.MESSAGE.UPLOAD_SUCCESSFULLY';
                this.isNotOverride = true;
                this.isClearInput = true;
                this.isNotImported = true;
                this.initiateTimer(this.importModalRef, 1000);
            }, error => {
                this.isUploaded = false;
                this.uploadPercentage = 100;
                this.handleError(error);
            });
    }


    openUploadModal() {
        this.importModalRef = this.modalService.show(this.importModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    closeUploadModal() {
        this.resetImportModal();
        this.importModalRef.hide();
    }

    remove(): void {
        this.openingDayService.removeOpeningDay(this.selectedOpeningDay.id).subscribe((res: Response) => {
            this.isSuccessful = true;
            this.noticeMessage = 'OPENING_DAY.DELETE_SUCCESSFULLY';
            this.search();
            this.initiateTimer(this.deletingModalRef, 1000);
        }, error => {
            this.isSuccessful = false;
            this.noticeMessage = 'COMMON.MESSAGE.DELETE_UNSUCCESFULLY';
        });
    }

    filterWithWorkingCode(event: any): void {
        if (event.exception) {
            this.criteria.expWorkingDayCode = event.option.originalCode === this.ALL_CODES ? null : event.option.originalCode;
        } else {
            this.criteria.workingDayCode = event.option.originalCode === this.ALL_CODES ? null : event.option.originalCode;
        }
        this.search();
    }

    filterTyping(event: any) {
        this.criteria.countryName = event.country;
        this.criteria.expAffiliate = event.affiliate;
        this.criteria.expBranchInfo = event.branch;
        this.criteria.expDeliveryAddressId = event.address;

        this.search();
    }

    closeDeleteModal(): void {
        this.deletingModalRef.hide();
    }

    searchTableData({ request, callback }) {
        this.tableRequest = request;
        this.tableCallback = callback;

        this.setFilterData();

        const pageModel = { page: request.page - 1, size: 10 };
        this.search(pageModel);
    }

    private search(page?): void {
        this.openingDayService.getOpeningDayList(this.criteria, page).pipe(
            finalize(() => {
                this.isLoaded = this.openingDayList.length ? false : true;
            })
        ).subscribe(openingDayList => {
            this.openingDayList = openingDayList.content.map(item => {
                return {
                    id: item.id,
                    date: item.date,
                    workingDayCode: item.workingDayCode,
                    countryName: item.countryName,
                    expAffiliate: item.expAffiliate,
                    expBranchInfo: item.expBranchInfo,
                    expWorkingDayCode: item.expWorkingDayCode ? item.expWorkingDayCode : null,
                    expDeliveryAddressId: item.expDeliveryAddressId
                };
            });
            this.itemsPerPage = openingDayList.numberOfElements;
            this.totalElements = openingDayList.totalElements;
            this.totalPages = openingDayList.totalPages;
            this.setTableData();
        }, error => {
            this.openingDayList = [];
            this.itemsPerPage = 0;
            this.totalElements = 0;
            this.totalPages = 0;
            this.setTableData();
        });
    }

    private initialCriteria(from?: any, to?: any): SearchCriteriaModel {
        const fromDate = new Date();
        const today = new Date();
        const formatDateTo = DatePickerUtil.buildDataDatePicker(today);
        const criteria: SearchCriteria = new SearchCriteria();

        fromDate.setDate(fromDate.getDate() - INTERVAL_THIRTY_ONE_DAYS);
        criteria.dateFrom = from ? DatePickerUtil.getDateFromToDatePicker(from.date, true) :
            DatePickerUtil.getDateFromToDatePicker(DatePickerUtil.buildDataDatePicker(fromDate).date, true);
        criteria.dateTo = to ? DatePickerUtil.getDateFromToDatePicker(to.date, true) :
            DatePickerUtil.getDateFromToDatePicker(formatDateTo.date, true);
        return criteria;
    }

    private resetCriteria(): void {
        this.criteria.countryName = null;
        this.criteria.workingDayCode = null;
        this.criteria.expAffiliate = null;
        this.criteria.expBranchInfo = null;
        this.criteria.expWorkingDayCode = null;
        this.criteria.expDeliveryAddressId = null;
        this.criteria.orderDescByDate = true;
        this.criteria.orderDescByCountryName = false;
    }

    private showDeleteModal(): void {
        this.deletingModalRef = this.modalService.show(this.deletingModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    private initiateTimer(modalRef: BsModalRef, duration: number) {
        return timer(duration).pipe(take(1)).subscribe(() => {
            modalRef.hide();
            this.resetImportModal();
        });
    }

    private handleError(error: any): void {
        let errorJson: any;
        if (!error) {
            this.uploadMessage = 'COMMON.MESSAGE.NOT_UPLOAD_SUCCESSFULLY';
            return;
        }
        errorJson = JSON.parse(error._body);
        switch (errorJson.error_code) {
            case 'IMPORT_DUPLICATED_OPENING_DAYS':
                this.uploadMessage = `OPENING_DAY.IMPORT_DUPLICATED_OPENING_DAYS`;
                this.isNotOverride = false;
                break;
            case 'CSV_SERVICE_ERROR':
                this.uploadMessage = 'COMMON.MESSAGE.NOT_UPLOAD_SUCCESSFULLY';
                break;
        }
    }

    private resetImportModal() {
        this.uploadPercentage = 0;
        this.uploadMessage = EMPTY_STRING;
        this.isUploading = false;
        this.isNotOverride = true;
        this.isNotImported = true;
        this.isClearInput = true;
        this.fileName = EMPTY_STRING;
        // this.fileUpload.nativeElement.value = null; // TODO select the same file will be affective
    }

    private setFilterData() {
        const { filter, sort, page } = this.tableRequest;
        if(filter && Object.keys(filter).length > 0) {
            const expWorkingDayCode = filter.expWorkingDayCode.originalCode === this.ALL_CODES ? null : filter.expWorkingDayCode.originalCode;
            this.criteria.expWorkingDayCode = expWorkingDayCode;

            const workingDayCode = filter.workingDayCode.originalCode === this.ALL_CODES ? null : filter.workingDayCode.originalCode;
            this.criteria.workingDayCode = workingDayCode;

            this.criteria.countryName = filter.countryName ? filter.countryName : null;
            this.criteria.expAffiliate = filter.expAffiliate ? filter.expAffiliate : null;
            this.criteria.expBranchInfo = filter.expBranchInfo ? filter.expBranchInfo : null;
            this.criteria.expDeliveryAddressId = filter.expDeliveryAddressId ? filter.expDeliveryAddressId : null;
        }
    }

    private setTableData() {
        if (!this.tableCallback) {
            return;
        }
        this.tableCallback({
            rows: this.openingDayList,
            totalItems: this.totalElements,
            page: this.tableRequest.page,
            totalPages: this.totalPages
        });
    }
}
