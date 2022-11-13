import { Constant } from 'src/app/core/conts/app.constant';
import { FinalCustomersUsersSort } from './final-customer-user-sort.model';

export class FinalCustomersUsersCriteria {
    page = 0;
    size: number = Constant.DEFAULT_PAGE_SIZE;
    userName: string;
    fullName: string;
    userEmail: string;
    type: string;
    sort: FinalCustomersUsersSort = new FinalCustomersUsersSort();
    firstName: string;
    lastName: string;
    salutation: string;

    constructor(data?: any | FinalCustomersUsersCriteria) {
        if (!data) {
            return;
        }
        this.page = data.page;
        this.size = data.size;
        this.userName = data.userName;
        this.fullName = data.fullName;
        this.userEmail = data.userEmail;
        this.type = data.type;
        this.sort = data.sort || new FinalCustomersUsersSort();
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.salutation = data.salutation;
    }
}
