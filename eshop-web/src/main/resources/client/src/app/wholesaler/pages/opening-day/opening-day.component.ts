import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { EMPTY_STR } from 'angular-mydatepicker';
import * as moment from 'moment';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { observable, of, Subject, timer } from 'rxjs';
import { finalize, take } from 'rxjs/operators';
import { SagConfirmationBoxComponent } from 'sag-common';

import { DATE_FILTER } from 'src/app/core/enums/date-filter.enum';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { DateUtil } from 'src/app/core/utils/date.util';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { ImportOpeningDayModalComponent } from '../../components/import-opening-day-modal/import-opening-day-modal.component';
import { OpeningDayFormModalComponent } from '../../components/opening-day-form-modal/opening-day-form-modal.component';
import { OpeningDayModel, SearchCriteria, SearchCriteriaModel } from '../../models/opening-day.model';
import { OpeningDayCalendarService } from '../../services/opening-day-calendar.service';

@Component({
    selector: 'connect-opening-day',
    templateUrl: './opening-day.component.html',
    styleUrls: ['./opening-day.component.scss']
})
export class OpeningDayComponent implements OnInit {
    @ViewChild('fileUpload', { static: false }) fileUpload: ElementRef;

    INTERVAL_THIRTY_ONE_DAYS = 30;
    workingCodes: Array<any>;
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

    private tableRequest: any;
    private tableCallback: any;

    criteria = this.initialCriteria();
    private pageModel;
    private sourceFile: any;

    private deletingModalRef: BsModalRef;
    private importModalRef: BsModalRef;

    private readonly ALL_CODES = 'ALL_CODES';

    private readonly OPTION_ALL = {
        value: '0',
        label: this.translateService.instant('COMMON_LABEL.ALL'),
        originalCode: this.ALL_CODES
    };

    constructor(
        private openingDayService: OpeningDayCalendarService,
        private route: ActivatedRoute,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private appModal: AppModalService) { }

    ngOnInit() {
        this.workingCodes = [this.OPTION_ALL];
        this.getWorkingCodes().subscribe(data => {
            this.workingCodes = [this.OPTION_ALL, ...data];
        });
    }

    getWorkingCodes() {
        return this.openingDayService.getWorkingDayCode();
    }

    searchWithDateRange({ from, to }): void {
        this.criteria = this.initialCriteria(from, to);
        this.resetCriteria();
        this.search(this.pageModel);
        this.resetFilterEvent.next(true);
    }

    searchWithSelectedDay({ date }) {
        this.criteria.dateFrom = DateUtil.buildFullDateData(date);
        this.criteria.dateTo = DateUtil.buildFullDateData(date);
        this.resetCriteria();
        this.search();
        this.resetFilterEvent.next(true);
    }

    searchWithPredefinedRange({ filter }): void {
        this.criteria.dateFrom = DateUtil.format(DateUtil.buildDateFromRange(filter.value), DateUtil.SECONDARY_DAY_FORMAT);
        this.criteria.dateTo = DateUtil.format(moment(), DateUtil.SECONDARY_DAY_FORMAT);
        this.resetCriteria();
        this.search();
        this.resetFilterEvent.next(true);
    }

    goToPage(page: number): void {
        const pageModel = { page, size: 10 };
        this.search(pageModel);
    }

    sortWith(sortModel): void {
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
        this.noticeMessage = EMPTY_STR;
        this.selectedOpeningDay = item;
        this.showDeleteModal();
    }

    openCreateModal(id?) {
        this.appModal.modals = this.modalService.show(OpeningDayFormModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                openingId: id,
                onClose: () => {
                    this.search();
                }
            }
        });
    }

    openUploadModal() {
        this.appModal.modals = this.importModalRef = this.modalService.show(ImportOpeningDayModalComponent, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
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
        SpinnerService.start();
        this.openingDayService.getOpeningDayList(this.criteria, page).pipe(
            finalize(() => {
                this.isLoaded = this.openingDayList.length ? false : true;
                SpinnerService.stop();
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
            this.tableRequest.page = openingDayList.number + 1;
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
        const formatDateTo = DateUtil.buildDataDatePicker(today);
        const criteria: SearchCriteria = new SearchCriteria();

        fromDate.setDate(fromDate.getDate() - this.INTERVAL_THIRTY_ONE_DAYS);
        criteria.dateFrom = from ? DateUtil.buildFullDateData(from.singleDate.date) :
            DateUtil.format(fromDate, DateUtil.SECONDARY_DAY_FORMAT);
        criteria.dateTo = to ? DateUtil.buildFullDateData(to.singleDate.date) :
            DateUtil.buildFullDateData(formatDateTo.singleDate.date);
        return criteria;
    }

    private resetCriteria(): void {
        const { dateFrom, dateTo } = this.criteria;
        this.criteria = new SearchCriteria();
        this.criteria.dateFrom = dateFrom;
        this.criteria.dateTo = dateTo;
    }

    private async showDeleteModal() {
        let message = await this.translateService.get('OPENING_DAY.DELETE_CONFIRM').toPromise();
        let messageContent = '';
        if (this.selectedOpeningDay) {
            messageContent = DateUtil.formatDateInDate(this.selectedOpeningDay.date);
            messageContent += '-' + await this.translateService.get(`OPENING_DAY.${this.selectedOpeningDay.workingDayCode}`).toPromise();
            messageContent += '-' + this.selectedOpeningDay.countryName;
            messageContent = `<strong>${messageContent}</strong> ?`;
        }
        message += messageContent;
        this.deletingModalRef = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                title: 'OPENING_DAY.DELETE_TITLE',
                message,
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                close: () => {
                    this.remove();
                }
            }
        });

        this.appModal.modals = this.deletingModalRef;
    }

    private initiateTimer(modalRef: BsModalRef, duration: number) {
        return timer(duration).pipe(take(1)).subscribe(() => {
            modalRef.hide();
        });
    }


    private setFilterData() {
        const { filter } = this.tableRequest;
        const expWorkingDayCode = filter.expWorkingDayCode.originalCode === this.ALL_CODES ? null : filter.expWorkingDayCode.originalCode;
        this.criteria.expWorkingDayCode = expWorkingDayCode;

        const workingDayCode = filter.workingDayCode.originalCode === this.ALL_CODES ? null : filter.workingDayCode.originalCode;
        this.criteria.workingDayCode = workingDayCode;

        this.criteria.countryName = filter.countryName ? filter.countryName : null;
        this.criteria.expAffiliate = filter.expAffiliate ? filter.expAffiliate : null;
        this.criteria.expBranchInfo = filter.expBranchInfo ? filter.expBranchInfo : null;
        this.criteria.expDeliveryAddressId = filter.expDeliveryAddressId ? filter.expDeliveryAddressId : null;
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
