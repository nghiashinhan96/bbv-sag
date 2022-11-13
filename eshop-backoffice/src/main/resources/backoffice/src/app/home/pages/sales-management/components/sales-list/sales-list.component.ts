import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { SagTableControl, SagTableColumn } from 'sag-table';

import { SalesSearchItemModel } from '../../model/sales-search-item.model';
import { SalesManagementConstant } from '../../sales-management-constant';
import { SortModel } from 'src/app/shared/models/sort.model';
import { SORT_TYPE, GENDER } from 'src/app/core/enums/enums';
import { ERROR_NOT_FOUND, ASC } from 'src/app/core/conts/app.constant';
import { AadAccountService } from '../../services/aad-accounts/aad-accounts-service';


@Component({
    selector: 'backoffice-sales-list',
    styleUrls: ['./sales-list.component.scss'],
    templateUrl: './sales-list.component.html',
})
export class SalesListComponent implements OnInit, SagTableControl {

    sales: any;
    genderOptions: Array<any>;
    searchModel: SalesSearchItemModel;
    warningMessage: string;
    private currentPage: number;

    columns = [];
    private tableCallback: any;
    private tableRequest: any;

    @ViewChild('colGender', { static: true }) colGender: TemplateRef<any>;
    @ViewChild('colEdit', { static: true }) colEdit: TemplateRef<any>;

    constructor(
        private aadAccountService: AadAccountService,
        private translateService: TranslateService,
        private router: Router) {
    }

    ngOnInit() {
        this.buildGenderOptions();
        this.searchModel = SalesSearchItemModel.getEmptyModel();
        this.buildColumns();
    }

    searchTableData({ request, callback }): void {
        this.tableRequest = request;
        this.tableCallback = callback;

        this.setSearchModel();
        this.search(this.searchModel, { page: this.currentPage, size: 10 });
        return;
    }

    getGenderText(key: string): string {
        if (!key) {
            return '';
        }
        return 'COMMON.LABEL.GENDER.' + key.toUpperCase();
    }

    create() {
        this.router.navigateByUrl(SalesManagementConstant.URL_SALES_CREATING_PAGE);
    }

    edit(item) {
        this.router.navigateByUrl(`${SalesManagementConstant.URL_SALES_EDITING_PAGE}/${item.id}`);
    }

    private setSearchModel() {
        const { filter, sort } = this.tableRequest;
        const { personalNumber, firstName, lastName, primaryContactEmail, legalEntityId, gender } = filter;
        this.searchModel.personalNumber = personalNumber ? personalNumber : null;
        this.searchModel.firstName = firstName ? firstName : null;
        this.searchModel.lastName = lastName ? lastName : null;
        this.searchModel.primaryContactEmail = primaryContactEmail ? primaryContactEmail : null;
        this.searchModel.legalEntityId = legalEntityId ? legalEntityId : null;
        this.searchModel.gender = gender ? gender.value : null;
        this.currentPage = this.tableRequest.page - 1;

        if (sort.field) {
            this.sort({ field: sort.field, direction: sort.direction === 'asc' ? SORT_TYPE.ASC : SORT_TYPE.DESC });
        }
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'personalNumber',
                i18n: 'SALES_MANAGEMENT.PERSONAL_NUMBER',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle word-break',
                width: '16%',
            },
            {
                id: 'firstName',
                i18n: 'SALES_MANAGEMENT.FIRSTNAME',
                cellClass: 'align-middle word-break',
                filterable: true,
                sortable: true,
                width: '14%',
            },
            {
                id: 'lastName',
                i18n: 'SALES_MANAGEMENT.LASTNAME',
                cellClass: 'align-middle word-break',
                filterable: true,
                sortable: true,
                width: '14%',
            },
            {
                id: 'primaryContactEmail',
                i18n: 'SALES_MANAGEMENT.EMAIL',
                cellClass: 'align-middle word-break',
                filterable: true,
                sortable: true,
                width: '22%',
            },
            {
                id: 'legalEntityId',
                i18n: 'SALES_MANAGEMENT.LEGAL_ENTITY_ID_NUMBER',
                cellClass: 'align-middle',
                filterable: true,
                sortable: true,
                width: '14%',
            },
            {
                id: 'gender',
                i18n: 'SALES_MANAGEMENT.GENDER',
                cellClass: 'align-middle',
                type: 'select',
                filterable: true,
                sortable: true,
                cellTemplate: this.colGender,
                selectSource: this.genderOptions,
                defaultValue: this.searchModel.gender,
                width: '14%',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colEdit,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    private search(searchModel: SalesSearchItemModel, page) {
        this.aadAccountService.search(searchModel, page).subscribe(res => {
            this.sales = res;
            this.setTableData();
            this.clearWarningMsg();
        }, err => {
            const { error } = err;
            if (error.code === ERROR_NOT_FOUND) {
                this.markContentIsEmpty();
                this.warningMessage = 'SALES_MANAGEMENT.MESSAGE.NOT_FOUND';
            }
        });
    }

    private setTableData() {

        if (!this.tableCallback || !this.tableRequest) {
            return;
        }
        const { content, numberOfElement, pageable, totalElements, totalPages } = this.sales;

        this.tableCallback({
            rows: content,
            totalItems: totalElements,
            itemsPerPage: numberOfElement,
            page: pageable.pageNumber + 1,
        });
    }

    private buildGenderOptions() {
        this.genderOptions = [];
        this.genderOptions.push({ value: '', label: 'COMMON.LABEL.ALL' });
        this.genderOptions.push({ value: GENDER.MALE, label: 'COMMON.LABEL.GENDER.MALE' });
        this.genderOptions.push({ value: GENDER.FEMALE, label: 'COMMON.LABEL.GENDER.FEMALE' });
    }

    private clearWarningMsg() {
        this.warningMessage = null;
    }

    private markContentIsEmpty() {
        this.sales = {};
        this.sales.content = [];
        this.tableCallback({
            rows: [],
            totalItems: 0,
        });
    }

    private sort(sortModel: SortModel) {
        this.resetSortingCriterias();
        const direction = sortModel.direction;
        this.searchModel[SalesManagementConstant.SORT_FIELD_MAP[sortModel.field]] = (direction === SORT_TYPE.DESC);
    }

    private resetSortingCriterias() {
        this.searchModel.orderDescByPersonalNumber = null;
        this.searchModel.orderDescByFirstName = null;
        this.searchModel.orderDescByLastName = null;
        this.searchModel.orderDescByPrimaryContactEmail = null;
        this.searchModel.orderDescByGender = null;
        this.searchModel.orderDescBylegalEntityId = null;
    }
}
