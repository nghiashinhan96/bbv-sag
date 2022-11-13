import { BaseModel } from 'src/app/shared/models/base.model';

export class UserRequestModel extends BaseModel {

    public sorting?: UserRequestModelSorting;
    affiliate: string;
    public page: number;
    public size: number;

    isFiltering: boolean;
    customerNumber: string;
    email: string;
    telephone: string;
    userName: string;
    role: string;
    name: string;
    isUserActive: boolean;

    constructor(data?: any) {
        super(data);
    }
}

export class UserRequestModelSorting extends BaseModel {
    public static columns = {
        companyName: 'companyName',
        customerNumber: 'customerNumber',
        username: 'username',
        email: 'email',
        telephone: 'telephone',
        role: 'role',
        status: 'status',
        editBtn: 'editBtn',
    };
    public orderByOrganisationNameDesc: boolean;
    public orderByCustomerNumberDesc: boolean;
    public orderByUserNameDesc: boolean;
    public orderByEmailDesc: boolean;
    public orderByTelephoneDesc: boolean;
    public orderByRoleDesc: boolean;
    public orderByStatusDesc: boolean;


    constructor(data?: any) {
        super(data);
    }

    public static matchColumnAndSort(userRequestModelSorting: UserRequestModelSorting, columnName, sortDescending) {
        // sortDescending : true, if false -> sortAscending
        switch (columnName) {
            case this.columns.companyName:
                userRequestModelSorting.orderByOrganisationNameDesc = sortDescending;
                break;
            case this.columns.customerNumber:
                userRequestModelSorting.orderByCustomerNumberDesc = sortDescending;
                break;
            case this.columns.username:
                userRequestModelSorting.orderByUserNameDesc = sortDescending;
                break;
            case this.columns.email:
                userRequestModelSorting.orderByEmailDesc = sortDescending;
                break;
            case this.columns.telephone:
                userRequestModelSorting.orderByTelephoneDesc = sortDescending;
                break;
            case this.columns.role:
                userRequestModelSorting.orderByRoleDesc = sortDescending === null ? sortDescending : !sortDescending;
                break;
            case this.columns.status:
                userRequestModelSorting.orderByStatusDesc = sortDescending === null ? sortDescending : !sortDescending;
                break;
            default:
                break;
        }
        return userRequestModelSorting;
    }
}
