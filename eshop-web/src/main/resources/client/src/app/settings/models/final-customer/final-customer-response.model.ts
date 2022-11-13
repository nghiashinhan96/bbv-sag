import { FinalCustomer } from './final-customer.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class FinalCustomerResponse {
    finalCustomers: FinalCustomer[] = [];
    pageNr = 0;
    size = Constant.DEFAULT_PAGE_SIZE;
    total = 0;

    constructor(res?: any) {
        if (res) {
            if (res.content) {
                res.content.forEach(element => {
                    this.finalCustomers.push(new FinalCustomer(element));
                });
            }
            this.pageNr = res.number;
            this.size = res.size;
            this.total = res.totalElements;
        }
    }
}
