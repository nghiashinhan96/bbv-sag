import { UserRequestModel } from 'src/app/home/models/user-request.model';

export class CustomerDetailUsers {
    userQuery: UserRequestModel;
    constructor(data?) {
        if (!data) {
            return;
        }
    }
}
