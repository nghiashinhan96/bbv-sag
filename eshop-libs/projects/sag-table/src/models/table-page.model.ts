const DEFAUTL_ITEM_PER_PAGE = 10;
export class TablePage {
    totalItems: number;
    itemsPerPage: number;
    currentPage: number;
    constructor(page?: TablePage) {
        this.totalItems = page && page.totalItems || 0;
        this.itemsPerPage = page && page.itemsPerPage || DEFAUTL_ITEM_PER_PAGE;
        this.currentPage = page && page.currentPage || 1;
    }
}
