import { UserDetail } from 'src/app/core/models/user-detail.model';

export class BasketHistoryItemModel {
    id: number;
    customerNr: number;
    customerName: string;
    saleName: string;
    salesUserId: string;
    basketName: string;
    referenceText: string;
    savingDate: string;
    total: string;
    isDeleteAbled?: boolean;
    constructor(data: BasketHistoryItemModel | any, userDetail: UserDetail) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.customerNr = data.customerNumber;
        this.customerName = data.customerName;
        this.saleName = data.salesFullName || '';
        this.salesUserId = `ID: ${data.salesUserId || ''}`;
        this.basketName = data.basketName;
        this.referenceText = data.customerRefText || '';
        this.savingDate = data.updatedDate;
        this.total = data.grandTotalExcludeVat;
        this.isDeleteAbled = this.checkDeleteAbled(userDetail, data.salesUserId, data.customerNumber, data.createdUserId);
    }

    private checkDeleteAbled(userDetail: UserDetail, saleId, customerNr, createdUserId) {
        const userSaleId = userDetail.salesUser ? userDetail.id : userDetail.salesId;
        // the baskets created by a sales could be deleted by this sale only
        if (saleId) {
            return Number(saleId) === Number(userSaleId);
        }

        // sales can delete all customer's saved baskets
        if (userDetail.salesUser || userDetail.isSalesOnBeHalf) {
            return true;
        }

        // admin user can delete baskets created by other users of the same customer
        if (userDetail.userAdminRole) {
            return Number(userDetail.custNr) === Number(customerNr);
        }

        // A normal user can delete all baskets saved by himself only.
        return Number(userDetail.id) === Number(createdUserId);
    }
}
