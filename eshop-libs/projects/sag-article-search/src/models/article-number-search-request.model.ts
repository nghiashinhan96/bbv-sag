import { LIB_VEHICLE_DEFAULT_PAGE_SIZE } from '../constant';

export class ArticleNumberSearchRequest {
    amountNumber = 0;
    offset = 0;
    size = LIB_VEHICLE_DEFAULT_PAGE_SIZE;
    isDeepLink = false;

    constructor(data?: any) {
        if (data) {
            this.amountNumber = data.amountNumber;
            this.offset = data.offset;
            this.size = data.size;
            this.isDeepLink = data.isDeepLink;
        }
    }
}
