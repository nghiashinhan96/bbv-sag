import { ArticleModel } from "./article.model";

export class FavoriteItem {
    id: string;
    addItem: boolean;
    comment: string;
    articleId: string;
    gaId: string;
    leafId: string;
    treeId: string;
    title: string;
    type: string;
    vehicleId: string;
    vinId: string;
    articleItem?: ArticleModel;

    constructor(data?) {
        this.id = data.id;
        this.addItem = data.addItem;
        this.articleId = data.articleId;
        this.gaId = data.gaId;
        this.leafId = data.leafId;
        this.treeId = data.treeId;
        this.title = data.title;
        this.type = data.type;
        this.comment = data.comment;
        this.vehicleId = data.vehicleId;
        this.vinId = data.vinId;
        
        this.articleItem = data.articleItem;
    }
}

export class FavoriteRequest {
    keySearch: string;
    page: number;
    size: number;
    type: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.keySearch = data.keySearch;
        this.page = data.page;
        this.size = data.size;
        this.type = data.type;
    }
}