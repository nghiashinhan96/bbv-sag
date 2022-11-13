export class FinalUserSearchSort {
    orderByUserNameDesc: boolean = null;
    orderByFullNameDesc: boolean = null;
    orderByEmailDesc: boolean = null;
    orderByFirstNameDesc: boolean = null;
    orderByLastNameDesc: boolean = null;

    constructor(sortObj?) {
        if (!sortObj) {
            return;
        }
        this.orderByUserNameDesc = sortObj.orderByUserNameDesc;
        this.orderByFullNameDesc = sortObj.orderByFullNameDesc;
        this.orderByEmailDesc = sortObj.orderByEmailDesc;
        this.orderByFirstNameDesc = sortObj.orderByFirstNameDesc;
        this.orderByLastNameDesc = sortObj.orderByLastNameDesc;
    }

    reset() {
        this.orderByUserNameDesc = null;
        this.orderByFullNameDesc = null;
        this.orderByEmailDesc = null;
        this.orderByFirstNameDesc = null;
        this.orderByLastNameDesc = null;
    }
}
