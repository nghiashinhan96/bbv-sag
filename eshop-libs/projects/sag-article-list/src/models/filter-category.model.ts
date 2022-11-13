export class FilterCategory {
    gaids: string[] = [];
    suppliers: string[] = [];
    keyword = '';
    filterMode = '';
    offset = 0;
    size = 10;
    contextKey = '';
    totalElementsOfSearching: number;
    idDlnr: string;

    constructor(data?: any) {
        if (data) {
            this.gaids = data.gaids || [];
            this.suppliers = data.suppliers || [];
            this.keyword = data.keyword || '';
            this.offset = data.offset;
            this.size = data.size;
            this.contextKey = data.contextKey || '';
            this.totalElementsOfSearching = data.totalElementsOfSearching;
            this.idDlnr = data.idDlnr;
        }
    }

    setCategory(filter: any): FilterCategory { return this; }
    setKeyword(keyword: string) { this.keyword = keyword; }
    setFilterType(filter?: any) { }
    setKeepDirectAndOriginalMatch(value:boolean) {}
}
