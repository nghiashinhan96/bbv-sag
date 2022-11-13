import { Component, ElementRef, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { SagTableControl, SagTableColumn } from 'sag-table';
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, finalize } from 'rxjs/operators';

import { Constant, DAY_IN_WEEK } from 'src/app/core/conts/app.constant';
import { DateUtil } from 'src/app/core/utils/date.util';
import { BranchRequest } from '../../models/branch-request.model';
import { BranchDto, BranchResponseModel } from '../../models/branch-response.model';
import { BranchService } from '../../services/branch.service';

import {
    TIME_REGEX,
    BRANCHES_SORT_MAP
} from '../../services/constant';
import { OpeningTimeDeleteModalComponent } from '../../components/opening-time-delete-modal/opening-time-delete-modal.component';
import { OpeningTimeDetailFormComponent } from '../../components/opening-time-detail-form/opening-time-detail-form.component';
import { SagConfirmationBoxComponent } from 'sag-common';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Component({
    selector: 'connect-opening-time',
    templateUrl: './opening-time.component.html',
    styleUrls: ['./opening-time.component.scss']
})
export class OpeningTimeComponent implements OnInit, OnDestroy {

    private readonly INIT_PAGE_NUM = 0;
    private destroy$ = new Subject<boolean>();

    branchList: BranchDto[] = [];
    page = this.INIT_PAGE_NUM;
    selectedBranchNr: string;
    noticeMessage: string;
    isSuccessful: boolean;
    isFail: boolean;
    totalItems: number;
    totalPages: number;
    sortOrder = Constant.ASC;
    isEmptyBranchList = false;
    requestBody: BranchRequest;

    private readonly SIZE = 10;
    private initialLoad: boolean;

    columns = [];
    private tableRequest: any;
    private tableCallback: any;

    private deletingModalRef: BsModalRef;

    searchTimeForm: FormGroup;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;
    @ViewChild('colTimeList', { static: true }) colTimeList: TemplateRef<any>;
    @ViewChild('deletingModal', { static: true }) deletingModal: ElementRef;


    constructor(
        private router: Router,
        private branchService: BranchService,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private fb: FormBuilder,
        private appModal: AppModalService) { }

    ngOnInit() {
        this.initialLoad = true;
        this.prepareDefaultRequestBody();
        this.buildTimeFormSearch();
        this.buildColumns();
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    loadBranchList(): number {
        this.search(this.requestBody, { page: this.page, size: this.SIZE });
        return this.page;
    }

    searchTableData({ request, callback }): void {
        this.tableRequest = request;
        this.tableCallback = callback;
        this.setRequestBody();
        this.loadBranchList();
    }

    onEditBranch(selectedBranch: BranchDto): void {
        this.deletingModalRef = this.modalService.show(OpeningTimeDetailFormComponent, {
            initialState: {
                modalTitle: 'BRANCHES.BRANCH_DETAIL_FORM.TITLE',
                isCreatedNewBranch: false,
                branchId: selectedBranch.branchNr,
                onClose: () => {
                    this.loadBranchList();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });

        this.appModal.modals = this.deletingModalRef;
    }

    onDeleteBranch(selectedBranch: any): void {
        this.selectedBranchNr = selectedBranch.branchNr;
        this.showDeleteModal();
    }

    onCreateBranch(): void {
        this.deletingModalRef = this.modalService.show(OpeningTimeDetailFormComponent, {
            initialState: {
                modalTitle: 'BRANCHES.BRANCH_DETAIL_FORM.TITLE',
                isCreatedNewBranch: true,
                onClose: () => {
                    this.loadBranchList();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
        this.appModal.modals = this.deletingModalRef;
    }

    onDeleteSelectedBranch(branchNr: string): void {
        this.branchService.deleteBranch(branchNr).subscribe(() => {
            this.isSuccessful = true;
            this.noticeMessage = 'BRANCHES.DELETE_SUCCESSFUL';
            setTimeout(() => {
                this.hideDeleteModal();
                this.page = this.INIT_PAGE_NUM;
                this.loadBranchList();
            }, 1000);
        }, ({ error }) => {
            this.isFail = true;
            this.hideDeleteModal();
            this.handleDeleteBranchError(error);
        });
    }

    handleDeleteBranchError(error) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'BRANCHES.UNABLE_DELETE_ASSIGNED_BRANCH',
                okButton: 'COMMON_LABEL.YES',
                showCancelButton: false,
                showHeaderIcon: false,
                showCloseButton: true,
            }
        });
    }

    onSortWith(field, isDesc): void {
        this.resetOrderRequest();
        this.requestBody[BRANCHES_SORT_MAP[field]] = isDesc;
    }

    validateTimeInput(input: string) {
        return !input.match(TIME_REGEX);
    }

    filter() {
        this.search(this.requestBody);
    }

    filterWithTime(input: string, control: string) {
    }

    hideDeleteModal() {
        this.deletingModalRef.hide();
        this.resetSort();
    }

    get openingTime() {
        return this.searchTimeForm.get('openingTime').value;
    }

    get lunchStartTime() {
        return this.searchTimeForm.get('lunchStartTime').value;
    }

    get lunchEndTime() {
        return this.searchTimeForm.get('lunchEndTime').value;
    }

    get closingTime() {
        return this.searchTimeForm.get('closingTime').value;
    }

    reloadTable() {
        this.setRequestBody();
        this.loadBranchList();
    }

    generateTimes(data: BranchDto) {
        if (!data || !data.wssBranchOpeningTimes) {
            return;
        }
        let timeStrings = [];
        DAY_IN_WEEK.forEach(day => {
            const dayTime = data.wssBranchOpeningTimes.filter(wssTime => wssTime.weekDay == day)[0];
            if (dayTime) {
                let time = this.translateService.instant(`DAY_IN_WEEK.${day.substr(0, 3)}`).substr(0, 2) + '=>';
                if (dayTime.lunchStartTime) {
                    time = `${time}${dayTime.openingTime}-${dayTime.lunchStartTime};${dayTime.lunchEndTime}-${dayTime.closingTime}`
                } else {
                    time = `${time}${dayTime.openingTime}-${dayTime.closingTime}`
                }
                timeStrings.push(time);
            }
        })
        return timeStrings.join('; ');
    }

    private setRequestBody() {
        const { filter, sort, page } = this.tableRequest;
        this.requestBody.branchNr = filter.branchNr ? filter.branchNr : Constant.EMPTY_STRING;
        this.requestBody.branchCode = filter.branchCode ? filter.branchCode : Constant.EMPTY_STRING;
        this.requestBody.openingTime = this.openingTime ? DateUtil.formatDateInTime(this.openingTime) : Constant.EMPTY_STRING;
        this.requestBody.lunchStartTime = this.lunchStartTime ? DateUtil.formatDateInTime(this.lunchStartTime) : Constant.EMPTY_STRING;
        this.requestBody.lunchEndTime = this.lunchEndTime ? DateUtil.formatDateInTime(this.lunchEndTime) : Constant.EMPTY_STRING;
        this.requestBody.closingTime = this.closingTime ? DateUtil.formatDateInTime(this.closingTime) : Constant.EMPTY_STRING;

        this.onSortWith(sort.field, sort.direction === 'desc');
        this.page = page - 1;
    }

    private buildTimeFormSearch() {
        this.searchTimeForm = this.fb.group({
            openingTime: null,
            lunchStartTime: null,
            lunchEndTime: null,
            closingTime: null
        });

        this.searchTimeForm.valueChanges.pipe(
            takeUntil(this.destroy$),
            debounceTime(600)
        ).subscribe((val) => {
            this.reloadTable();
        });
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'branchNr',
                i18n: 'BRANCHES.BRANCH_LIST_COLUMNS.ID',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '14%',
            },
            {
                id: 'branchCode',
                i18n: 'BRANCHES.BRANCH_LIST_COLUMNS.CODE',
                filterable: true,
                sortable: true,
                width: '16%',
            },
            {
                id: 'openingTime',
                i18n: 'OPENING_DAY.OPENING_DAY_MENU_TITLE',
                cellClass: 'text-left',
                cellTemplate: this.colTimeList,
                width: '48%',
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

    private search(branchRequest: BranchRequest, page?) {
        SpinnerService.start();
        this.branchService.searchBranches(branchRequest, page)
            .pipe(
                finalize(() => {
                    SpinnerService.stop();
                })
            )
            .subscribe((res: BranchResponseModel) => {
                this.totalItems = res.totalElements;
                this.branchList = res.content;
                this.isEmptyBranchList = this.branchList.length || this.totalItems ? false : true;
                this.totalPages = res.totalPages;
                this.setTableData();

            }, err => {
                this.totalItems = 0;
                this.branchList = [];
                this.isEmptyBranchList = true;
                this.setTableData();
            });
    }


    private setTableData() {
        if (!this.tableCallback || !this.tableRequest) {
            return;
        }
        if (this.branchList.length > 0) {
            this.tableCallback({
                rows: this.branchList,
                totalItems: this.totalItems,
                page: this.page + 1,
                totalPages: this.totalPages
            });
        } else {
            this.tableCallback({
                rows: [],
                totalItems: 0,
            });
        }
    }

    private showDeleteModal(): void {
        let message = `${this.translateService.instant(`BRANCHES.DELETE_CONFIRM`)} ${this.selectedBranchNr}`;
        this.deletingModalRef = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                title: 'COMMON_LABEL.CONFIRMATION',
                message,
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                close: () => {
                    this.onDeleteSelectedBranch(this.selectedBranchNr);
                }
            },
        });

        this.appModal.modals = this.deletingModalRef;
    }

    private resetSort(): void {
        this.noticeMessage = Constant.EMPTY_STRING;
    }

    private prepareDefaultRequestBody(): void {
        this.requestBody = new BranchRequest();

        if (this.initialLoad) {
            this.onSortWith('branchNr', false);
        }
        this.initialLoad = false;
    }

    private resetOrderRequest() {
        if (!this.requestBody) {
            return;
        }
        this.requestBody.orderDescByBranchNr = null;
        this.requestBody.orderDescByBranchCode = null;
        this.requestBody.orderDescByOpeningTime = null;
        this.requestBody.orderDescByLunchStartTime = null;
        this.requestBody.orderDescByLunchEndTime = null;
        this.requestBody.orderDescByClosingTime = null;
    }
}
