export class LevelCategoryRequest {
    gaids: string[] = [];
    suppliers: string[] = [];
    keyword: string;
    // tslint:disable-next-line: variable-name
    filter_mode: string;
    offset: number;
    size: number;
    // tslint:disable-next-line: variable-name
    context_key: string;
    finalCustomerId: number;
    idDlnr: string;

    constructor(data?: any) {
        if (data) {
            this.gaids = data.gaids;
            this.suppliers = data.suppliers;
            this.keyword = data.keyword;
            this.filter_mode = data.filterMode;
            this.offset = data.offset;
            this.size = data.size;
            this.context_key = data.contextKey;
            this.finalCustomerId = data.finalCustomerId;
            this.idDlnr = data.idDlnr;
        }
    }
}
