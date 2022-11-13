export class LevelCategory {
    gaids: string[] = [];
    suppliers: string[] = [];
    keyword = '';
    filterMode = '';
    offset = 0;
    size = 10;
    contextKey = '';
    totalElementsOfSearching: number;
    idDlnr: string;
    multipleLevelGaids?: any;
    isShoppingList?: boolean;

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
            this.multipleLevelGaids = data.multipleLevelGaids;
            this.isShoppingList = data.isShoppingList;
        }
    }

    setCategory(filter: any): LevelCategory { return this; }
    setKeyword(keyword: string) { this.keyword = keyword; }
    setFilterType(filter?: any) { }
    setKeepDirectAndOriginalMatch(value: boolean) { }
    setIsShoppingList(value: boolean) { this.isShoppingList = value }
}
