export class PotentialCustomerFieldModel {
    key: string;
    value: string;
    title: string;

    constructor(data?: any) {
        if(data) {
            this.key = data.key;
            this.value = data.value;
            this.title = data.title;
        }
    }
}