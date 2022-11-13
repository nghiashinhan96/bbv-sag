import { BaseModel } from 'src/app/shared/models/base.model';

export class CustomerQuery extends BaseModel {
    constructor(data?: any) {
        super(data);
    }

    customerNr: string;
    affiliate: string;
    companyName: string;
    public page: number;
    public size: number;
}
