import { Constant } from 'src/app/core/conts/app.constant';

export class ArticleNumberSearchRequest {
    amountNumber = 0;
    offset = 0;
    size = Constant.DEFAULT_PAGE_SIZE;
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
