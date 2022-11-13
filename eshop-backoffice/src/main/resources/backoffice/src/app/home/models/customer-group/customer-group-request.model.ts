export class CustomerGroupRequestSortingModel {
    orderByCollectionNameDesc: boolean = null;
    orderByAffiliateNameDesc: boolean = null;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.orderByAffiliateNameDesc = data.orderByAffiliateNameDesc;
        this.orderByCollectionNameDesc = data.orderByCollectionNameDesc;
    }
}

export class CustomerGroupRequestModel {
    affiliate = ' ';
    collectionName = '';
    page = 0;
    size = 10;
    customerNr = '';
    sorting: CustomerGroupRequestSortingModel;
    useWholePage = false;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.affiliate = data.affiliate;
        this.collectionName = data.collectionName;
        this.page = data.page;
        this.size = data.size;
        this.customerNr = data.customerNr;
        if (data.sorting) {
            this.sorting = new CustomerGroupRequestSortingModel(data.sorting as CustomerGroupRequestSortingModel);
        }
    }
}
