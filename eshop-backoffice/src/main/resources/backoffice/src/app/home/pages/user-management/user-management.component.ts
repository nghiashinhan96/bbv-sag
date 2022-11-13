import { Component, ElementRef, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { SagTableColumn, SagTableControl, TablePage } from 'sag-table';

import { AuthService } from 'src/app/authentication/services/auth.service';
import { UserService } from 'src/app/core/services/user.service';
import AffUtils from 'src/app/core/utils/aff-utils';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { StringHelper } from 'src/app/core/utils/string.util';
import { UserExportRequest } from './models/user-export-request.model';
import {
    UserRequestModel,
    UserRequestModelSorting
} from './models/user-request.model';


@Component({
    selector: 'backoffice-user-management',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.scss'],
})
export class UserManagementComponent implements OnInit, SagTableControl {
    @ViewChild('colStatus', { static: true }) colStatus: TemplateRef<any>;
    @ViewChild('colRole', { static: true }) colRole: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;
    @ViewChild('exportModal', { static: true }) exportModal: ElementRef;

    public userSearchRequestModel = new UserRequestModel();
    // isAuthed: boolean;
    public users: any;
    public statuseOptions: any;
    public currentPage: number;
    public totalPages: number;
    public totalItems: number;
    public itemsPerPage: number;
    public statusModel: any;
    // public exportConfirm = 'confirm-export-large-file';
    public errorMessage = null;
    public isCh: boolean;

    private timer: any;
    private isFirstSearchByParams = true;
    private exportModalRef: BsModalRef;

    columns = [];
    page = new TablePage();
    searchModel = {};
    tableCallback: any;
    tableRequest: any;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private translateService: TranslateService,
        private authService: AuthService,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        this.isCh = AffUtils.isCH();
        this.getdoFilteringOptions();
        this.statusModel = this.statuseOptions[1].value;
        this.route.params.subscribe((params) => {
            const inputParam: UserRequestModel = JSON.parse(params.queryParam);
            Object.assign(this.userSearchRequestModel, inputParam);
            this.userSearchRequestModel.isUserActive = this.statusModel;
            this.buildColumns();
        });
    }

    searchTableData({ request, callback }): void {
        this.tableCallback = callback;
        this.tableRequest = request;

        this.userSearchRequestModel.page = request.page - 1;
        this.currentPage = request.page;

        this.setFilterData();

        this.doSearchUser(this.userSearchRequestModel);
    }

    buildColumns() {
        this.columns = [
            {
                id: 'organisationCode',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.CUSTOMER_NUMBER_SHORTER',
                filterable: true,
                sortable: true,
                width: '14%',
                defaultValue: this.userSearchRequestModel.customerNumber
            },
            {
                id: 'organisationName',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.COMPANY_NAME',
                sortable: true,
                width: '14%',
            },
            {
                id: 'userName',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.USERNAME',
                filterable: true,
                sortable: true,
                width: '14%',
                defaultValue: this.userSearchRequestModel.userName
            },
            {
                id: 'email',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.EMAIL',
                filterable: true,
                sortable: true,
                width: '18%',
                defaultValue: this.userSearchRequestModel.email
            },
            {
                id: 'roleName',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.ROLE',
                sortable: true,
                cellTemplate: this.colRole,
                width: '14%',
            },
            {
                id: 'isUserActive',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.STATUS',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.statuseOptions,
                defaultValue: this.statuseOptions[1],
                cellTemplate: this.colStatus,
                width: '14%',
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


    getdoFilteringOptions() {
        this.statuseOptions = [];
        this.statuseOptions.push({
            value: null,
            label: 'COMMON.LABEL.ALL',
        });
        this.statuseOptions.push({
            value: 'true',
            label: 'COMMON.LABEL.ACTIVE',
            selected: 'selected',
        });
        this.statuseOptions.push({
            value: 'false',
            label: 'COMMON.LABEL.INACTIVE',
        });
    }

    sort(fieldName: string, direction: string) {
        const userRequestModelSorting = new UserRequestModelSorting();
        const sortDesc = direction === 'desc';
        switch (fieldName) {
            case 'organisationName':
                userRequestModelSorting.orderByOrganisationNameDesc = sortDesc;
                break;
            case 'organisationCode':
                userRequestModelSorting.orderByCustomerNumberDesc = sortDesc;
                break;
            case 'userName':
                userRequestModelSorting.orderByUserNameDesc = sortDesc;
                break;
            case 'email':
                userRequestModelSorting.orderByEmailDesc = sortDesc;
                break;
            case 'roleName':
                userRequestModelSorting.orderByRoleDesc =
                    sortDesc === null ? sortDesc : !sortDesc
                    ;
                break;
            case 'isUserActive':
                userRequestModelSorting.orderByStatusDesc =
                    sortDesc === null ? sortDesc : !sortDesc
                    ;
                break;
        }
        this.userSearchRequestModel.sorting = userRequestModelSorting;
    }

    viewUserDetail(user) {
        this.router.navigate(['/home/search/users/detail', { id: user.id }]);
    }

    get canExport() {
        return AffUtils.isCH() || this.haveAffiliate();
    }

    export() {
        this.errorMessage = null;
        if (AffUtils.isCH() && !this.haveAffiliate()) {
            this.exportModalRef = this.modalService.show(this.exportModal, {
                class: 'modal-user-form  modal-lg',
                ignoreBackdropClick: true,
            }
            );
            return;
        }
        if (AffUtils.isAT() && !this.haveAffiliate()) {
            return;
        }

        this.exportUsers();
    }

    exportUsers() {
        this.errorMessage = null;
        const request: UserExportRequest = {
            affiliate: this.userSearchRequestModel.affiliate,
            customerNumber: this.userSearchRequestModel.customerNumber,
            userName: this.userSearchRequestModel.userName,
            email: this.userSearchRequestModel.email,
            isUserActive: this.userSearchRequestModel.isUserActive,
        };
        SpinnerService.start();
        this.userService.exportUsersByCriteria(request, (res) => {
            if (res && res.error) {
                this.errorMessage = 'USER_MANAGEMENT.EXPORT.ERROR';
            }
        });
    }

    closeModalAndExport() {
        this.closeModal();
        this.exportUsers();
    }

    closeModal() {
        this.exportModalRef.hide();
    }

    // getStatusText(active: boolean) {
    //     return this.translateService.instant(
    //         'COMMON.LABEL.' + (active ? 'ACTIVE' : 'INACTIVE')
    //     );
    // }

    // getRoleText(roleName: boolean) {
    //     return this.translateService.instant('COMMON.LABEL.ROLE.' + roleName);
    // }

    doFilter() {
        this.userSearchRequestModel.isUserActive = this.statusModel;
        this.doSearchUser(this.userSearchRequestModel);
    }

    // paging(page: number) {
    //     this.currentPage = page - 1;
    //     this.userSearchRequestModel.page = this.currentPage;
    //     this.doSearchUser(this.userSearchRequestModel);
    // }

    // filter(timeout?: number) {
    //     if (!timeout) {
    //         this.doFilter();
    //     } else {
    //         clearTimeout(this.timer);
    //         this.timer = setTimeout(() => {
    //             this.doFilter();
    //         }, timeout);
    //     }
    // }

    private haveAffiliate() {
        return !StringHelper.isBlank(this.userSearchRequestModel.affiliate);
    }

    private setFilterData() {
        const { filter, sort } = this.tableRequest;
        let { organisationCode, userName, email, isUserActive } = filter;

        isUserActive = isUserActive.value;
        if (this.isFirstSearchByParams) {
            organisationCode = this.userSearchRequestModel.customerNumber || organisationCode;
            userName = this.userSearchRequestModel.userName || userName;
            email = this.userSearchRequestModel.email || email;
            this.isFirstSearchByParams = false;
        }

        this.userSearchRequestModel.customerNumber = organisationCode;
        this.userSearchRequestModel.userName = userName;
        this.userSearchRequestModel.email = email;
        this.userSearchRequestModel.isUserActive = isUserActive;

        if (sort.field) {
            this.sort(sort.field, sort.direction);
        }
    }

    private doSearchUser(params) {
        this.errorMessage = null;
        this.userService.searchUser(params).subscribe((response) => {
            this.users = response;

            this.totalItems = this.users.totalElements;
            this.totalPages = this.users.totalpages;
            this.currentPage = this.users.pageable.pageNumber + 1;
            this.itemsPerPage = this.users.size;

            if (this.tableCallback) {
                this.tableCallback({
                    rows: this.users.content,
                    totalItems: this.totalItems,
                    page: this.currentPage,
                    itemsPerPage: this.itemsPerPage,
                });
            }
        });
    }
}
