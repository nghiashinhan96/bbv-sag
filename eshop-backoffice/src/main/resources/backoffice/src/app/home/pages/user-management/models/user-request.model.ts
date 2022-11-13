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
}
