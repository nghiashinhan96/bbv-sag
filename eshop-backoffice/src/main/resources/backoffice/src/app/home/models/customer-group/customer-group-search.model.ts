import { CustomerGroupModel } from './customer-group.model';

export class CustomerGroupSearchModel {
    content: CustomerGroupModel[] = [];
    totalElements: number;
    size: number;
    number: number;

    constructor(data?) {
        if (!data) {
            return;
        }

        if (data.content) {
            data.content.forEach(element => {
                this.content.push(new CustomerGroupModel(element as CustomerGroupModel));
            });
        }

        this.totalElements = data.totalElements;
        this.size = data.size;
        this.number = data.number;

    }
}
