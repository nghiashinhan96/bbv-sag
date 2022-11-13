import { PotentialCustomerFieldModel } from './potential-customer-field.model';

export class PotentialCustomerModel {
    collectionShortName: string;
    langCode: string;
    fields: PotentialCustomerFieldModel[] = [];

    constructor(data?: any) {
        if(data) {
            this.collectionShortName = data.collectionShortName;
            this.langCode = data.langCode;

            if(data.fields) {
                data.fields.forEach(element => {
                    this.fields.push(new PotentialCustomerFieldModel(element));
                });
            }
        }
    }
} 