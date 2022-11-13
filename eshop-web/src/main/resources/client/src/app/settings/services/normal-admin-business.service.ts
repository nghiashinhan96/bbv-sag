import { Injectable, TemplateRef } from '@angular/core';

import { SagTableColumn } from 'sag-table';


@Injectable()
export class NormalAdminBusinessService {

    constructor() { }

    buildUserListColumns(templateRef: TemplateRef<any>[], selects: any[]) {
        return [
            {
                id: 'userName',
                i18n: 'SETTINGS.USER_MANAGEMENT.TABLE_COLUMNS.USER_NAME',
                filterable: true,
                sortable: true,
                width: '200px'
            },
            {
                id: 'salutation',
                i18n: 'SETTINGS.USER_MANAGEMENT.TABLE_COLUMNS.SALUTATION',
                filterable: true,
                sortable: true,
                cellTemplate: templateRef[0],
                type: 'input',
                width: '115px'
            },
            {
                id: 'firstName',
                i18n: 'SETTINGS.USER_MANAGEMENT.TABLE_COLUMNS.FIRST_NAME',
                filterable: true,
                sortable: true,
                width: 'auto'
            },
            {
                id: 'lastName',
                i18n: 'SETTINGS.USER_MANAGEMENT.TABLE_COLUMNS.LAST_NAME',
                filterable: true,
                sortable: true,
                width: '180px'
            },
            {
                id: 'role',
                i18n: 'SETTINGS.USER_MANAGEMENT.TABLE_COLUMNS.ROLE_NAME',
                filterable: true,
                sortable: true,
                cellTemplate: templateRef[1],
                type: 'input',
                width: '210px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: templateRef[2],
                width: '100px'
            },
        ] as SagTableColumn[];
    }
}
