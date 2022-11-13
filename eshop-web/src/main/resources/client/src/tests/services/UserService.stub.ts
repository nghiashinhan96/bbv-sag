import { of, Subject } from "rxjs";

export class UserServiceStub {
    public userDetail$ = new Subject<any>();
    public userPrice$ = new Subject<any>();

    getPaymentSetting() {
        return of(null);
    }

    hasPermissions() {
        return of(null);
    }
}