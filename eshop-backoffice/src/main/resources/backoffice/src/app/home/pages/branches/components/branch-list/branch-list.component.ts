import { Component, OnInit, AfterViewInit, TemplateRef, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder } from '@angular/forms';

import { SagTableControl, SagTableColumn } from 'sag-table';
import { TranslateService } from '@ngx-translate/core';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

import { BranchService } from '../../service/branch.service';
import { BranchRequest } from '../../model/branch-request.model';
import { BranchResponseModel, BranchDto } from '../../model/branch-response.model';
import { ASC, EMPTY_STRING } from 'src/app/core/conts/app.constant';
import {
    EDIT_SELECTED_BRANCH,
    CREATE_NEW_BRANCH,
    CREATE_BRANCH_ROUTE,
    EDIT_BRANCH_ROUTE,
    TIME_REGEX,
    BRANCHES_SORT_MAP,
    DAY_IN_WEEK
} from '../../branches.constant';
import { DeleteConfirmModalComponent } from 'src/app/shared/common/components/delete-confirm-modal/delete-confirm-modal.component';
import { DateUtil } from 'src/app/core/utils/date.util';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
    selector: 'backoffice-branch-list',
    templateUrl: './branch-list.component.html',
    styleUrls: ['./branch-list.component.scss']
})
export class BranchListComponent implements OnInit, OnDestroy, SagTableControl {
    private readonly INIT_PAGE_NUM = 1;
    private destroy$ = new Subject<boolean>();

    branchList: BranchDto[] = [];
    page = this.INIT_PAGE_NUM;
    selectedBranchNr: string;
    noticeMessage: string;
    totalItems: number;
    totalPages: number;
    sortOrder = ASC;
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
    @ViewChild('deletingModal', { static: true }) deletingModal: ElementRef;
    @ViewChild('colTimeList', { static: true }) colTimeList: TemplateRef<any>;

    constructor(
        private router: Router,
        private branchService: BranchService,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private fb: FormBuilder) { }

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
        this.router.navigate([EDIT_BRANCH_ROUTE, selectedBranch.branchNr],
            {
                queryParams: {
                    createNew: EDIT_SELECTED_BRANCH,
                    openingTime: selectedBranch.openingTime,
                    closingTime: selectedBranch.closingTime
                }
            }
        );
    }

    onDeleteBranch(selectedBranch: any): void {
        this.selectedBranchNr = selectedBranch.branchNr;
        this.showDeleteModal();
    }

    onCreateBranch(): void {
        this.router.navigate([CREATE_BRANCH_ROUTE], { queryParams: { createNew: CREATE_NEW_BRANCH } });
    }

    onDeleteSelectedBranch(branchNr: string): void {
        this.branchService.deleteBranch(branchNr).subscribe(() => {
            this.noticeMessage = 'BRANCHES.DELETE_SUCCESSFUL';
            setTimeout(() => {
                this.hideDeleteModal();
                this.page = this.INIT_PAGE_NUM;
                this.loadBranchList();
            }, 1000);
        }, err => {
            this.noticeMessage = 'BRANCHES.DELETE_FAIL';
        });
    }

    onSortWith(field, isDesc): void {
        this.resetOrderRequest();
        this.requestBody[BRANCHES_SORT_MAP[field]] = isDesc;
    }

    // filter() {
    //     this.search(this.requestBody);
    // }

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
        if (!data || !data.branchOpeningTimes) {
            return;
        }
        let timeStrings = [];
        DAY_IN_WEEK.map(day => {
            const dayTime = data.branchOpeningTimes.filter(wssTime => wssTime.weekDay == day)[0];
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
        this.requestBody.branchNr = filter.branchNr ? filter.branchNr : EMPTY_STRING;
        this.requestBody.branchCode = filter.branchCode ? filter.branchCode : EMPTY_STRING;
        this.requestBody.openingTime = this.openingTime ? DateUtil.dateToString(this.openingTime) : EMPTY_STRING;
        this.requestBody.lunchStartTime = this.lunchStartTime ? DateUtil.dateToString(this.lunchStartTime) : EMPTY_STRING;
        this.requestBody.lunchEndTime = this.lunchEndTime ? DateUtil.dateToString(this.lunchEndTime) : EMPTY_STRING;
        this.requestBody.closingTime = this.closingTime ? DateUtil.dateToString(this.closingTime) : EMPTY_STRING;

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
                i18n: 'BRANCHES.BRANCH_LIST_COLUMNS.OPENING',
                filterable: false,
                sortable: false,
                width: '50%',
                cellTemplate: this.colTimeList
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
        this.branchService.searchBranches(branchRequest, page).subscribe((res: BranchResponseModel) => {
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
        this.deletingModalRef = this.modalService.show(DeleteConfirmModalComponent, {
            initialState: {
                modalTitle: 'BRANCHES.DELETE_BRANCH',
                deleteMessage: 'BRANCHES.DELETE_CONFIRM',
                deleteItemName: this.selectedBranchNr,
                noticeMessage: this.noticeMessage,
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
        this.deletingModalRef.content.onConfirm = () => {
            this.onDeleteSelectedBranch(this.selectedBranchNr);
        };
    }

    private resetSort(): void {
        this.noticeMessage = EMPTY_STRING;
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

