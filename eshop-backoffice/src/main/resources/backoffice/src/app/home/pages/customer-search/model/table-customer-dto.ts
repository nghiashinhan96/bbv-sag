export class TableCustomerDto {
    public static COLUMNS_KEY_NAME = ['customerNr', 'affiliate', 'companyName', 'viewDetail'];

    private customerNr: string;
    public affiliate: string;
    public companyName: string;
    public viewDetail?: any = '';
    constructor(data?) {
        if (!data) {
            return;
        }

        this.affiliate = data.affiliate;
        this.companyName = data.companyName;
        this.customerNr = data.customerNr;
    }
}
