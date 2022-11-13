import { FinalUserSearchSort } from './final-user-search-sort.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class FinalUserSearchCriteria {
    page = 0;
    size: number = Constant.DEFAULT_PAGE_SIZE;
    userName: string;
    fullName: string;
    userEmail: string;
    type: string;
    sort: FinalUserSearchSort = new FinalUserSearchSort();
    firstName: string;
    lastName: string;
    salutation: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.page = data.page;
        this.size = data.size;
        this.userName = data.userName;
        this.fullName = data.fullName;
        this.userEmail = data.userEmail;
        this.type = data.type;
        this.sort = data.sort || new FinalUserSearchSort();
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.salutation = data.salutation;
    }
}
