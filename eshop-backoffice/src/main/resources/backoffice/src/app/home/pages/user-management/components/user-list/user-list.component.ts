import {
    Component,
    OnInit,
    Input,
    ViewChild,
    ElementRef,
    OnChanges,
    TemplateRef,
} from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { SagTableControl, SagTableColumn, TablePage } from 'sag-table';
import { isEmpty } from 'lodash';

import { TableUserDetailRow } from '../../models/table-user-detail-row.model';
import { UserDataManagementService } from '../../../../services/user-data-management.service';
import { TableUserDetailOptions } from '../../models/table-user-detail-options.model';
import {
    UserRequestModel,
    UserRequestModelSorting,
} from 'src/app/home/models/user-request.model';

@Component({
    selector: 'backoffice-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.scss'],
})
export class UserListComponent implements OnInit, SagTableControl {
    @Input()
    query: UserRequestModel;

    @ViewChild('statusSelection', { static: true })
    statusSelection: ElementRef;

    columns = [];
    tableData = [];
    searchModel = {};
    tableCallback: any;
    tableRequest: any;

    page = new TablePage();
    totalPages: number;
    currentPage: number;
    totalItems: number;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private router: Router,
        private dataManagement: UserDataManagementService,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.buildColumns();
    }

    buildColumns() {
        const activeSelectorValue = [
            {
                value: TableUserDetailOptions.StatusAllValue,
                label: 'COMMON.LABEL.ALL',
            },
            {
                value: TableUserDetailOptions.StatusActiveValue,
                label: 'COMMON.LABEL.ACTIVE',
            },
            {
                value: TableUserDetailOptions.StatusInactiveValue,
                label: 'COMMON.LABEL.INACTIVE',
            },
        ];
        this.columns = [
            {
                id: 'companyName',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.COMPANY_NAME',
                sortable: true,
                width: '15%',
            },
            {
                id: 'username',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.USERNAME',
                filterable: true,
                sortable: true,
                defaultValue: '',
                width: '20%',
            },
            {
                id: 'email',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.EMAIL',
                filterable: true,
                sortable: true,
                defaultValue: '',
                width: '30%',
            },
            {
                id: 'role',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.ROLE',
                sortable: true,
                defaultValue: '',
                width: '15%',
            },
            {
                id: 'status',
                i18n: 'USER_MANAGEMENT.TABLE_RESULT.STATUS',
                type: 'select',
                filterable: true,
                sortable: true,
                selectSource: activeSelectorValue,
                defaultValue: activeSelectorValue[0],
                width: '15%',
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

    searchTableData({ request, callback }): void {
        const { filter, page, sort } = request;
        const { username, email, role, status } = filter;
        const query = {
            email,
            isUserActive: status.value,
            userName: username,
            page: page - 1,
            sorting: null,
        };

        if (sort.field) {
            const sorting = this.sort(sort.field, sort.direction);
            query.sorting = sorting;
        }

        this.tableCallback = callback;
        this.tableRequest = request;

        this.queryUser(query);
        return;
    }

    sort(columnName, direction) {
        let userRequestModelSorting = new UserRequestModelSorting();
        userRequestModelSorting = UserRequestModelSorting.matchColumnAndSort(
            userRequestModelSorting,
            columnName,
            direction === 'desc'
        );
        return userRequestModelSorting;
    }

    reloadUsers() {
        this.queryUser(this.dataManagement.userQuery);
    }

    resetUsers(query) {
        this.dataManagement.setUserQuery(query);
        this.queryUser(this.query);
    }

    queryUser(query?) {
        if (!isEmpty(query)) {
            this.dataManagement.mergeUserQuery(query);
        }
        if (!isEmpty(this.dataManagement.userQuery)) {
            this.dataManagement.fetchUsers().subscribe((userResult) => {
                this.buildOrUpdateUserView(userResult, this.dataManagement.userQuery);
            });
        }
    }

    // onSortingTable(config: any) {
    //     let userRequestModelSorting = new UserRequestModelSorting();
    //     const userSearchRequestModel = new UserRequestModel();
    //     for (const item of config.sorting.columns) {
    //         if (item.sort && item.sort !== '') {
    //             userRequestModelSorting = UserRequestModelSorting.matchColumnAndSort(
    //                 userRequestModelSorting,
    //                 item.name,
    //                 item.sort === 'desc'
    //             );
    //             userSearchRequestModel.sorting = userRequestModelSorting;
    //         } else {
    //             userRequestModelSorting = UserRequestModelSorting.matchColumnAndSort(
    //                 userRequestModelSorting,
    //                 item.name,
    //                 null
    //             );
    //             userSearchRequestModel.sorting = userRequestModelSorting;
    //         }
    //     }
    //     this.queryUser(userSearchRequestModel);
    // }

    // onUpdatePage(page) {
    //     this.queryUser(new UserRequestModel({ page: page - 1 }));
    // }

    // onViewCustomerDetail(customer) {
    //     this.router.navigate([
    //         '/home/search/customers/detail',
    //         { affiliate: customer.affiliate, customerNr: customer.customerNumber },
    //     ]);
    // }

    onEditUserDetail(user) {
        this.router.navigate(['/home/search/users/detail', { id: user.id }]);
    }

    buildOrUpdateUserView(userResult, query) {
        this.updateTable(userResult, query);
    }

    createTableData(userResults) {
        const userList = new Array<TableUserDetailRow>();
        for (const user of userResults) {
            const userRow = TableUserDetailRow.createTranslatedRow(
                user,
                this.translateService
            );
            userList.push(userRow);
        }
        return userList;
    }

    updateTable(userResult, query: UserRequestModel) {
        const users = userResult.users || [];
        const tableData = this.createTableData(users);
        this.tableData = tableData;

        let totalItems = 0;
        if (userResult.totalUsers > 0) {
            totalItems = userResult.totalUsers;
        }

        this.totalItems = totalItems;
        this.totalPages = userResult.totalPages;
        this.currentPage = query.page;
        if (this.tableCallback) {
            this.tableCallback({
                rows: this.tableData,
                totalItems,
                itemsPerPage: userResult.itemsPerPage,
                page: query.page + 1,
            });
        }
    }
}
