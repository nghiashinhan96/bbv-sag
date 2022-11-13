import { LevelCategoryRequest } from "src/app/core/models/level-category.request.model";
import { SEARCH_MODE } from 'sag-article-list';

export class WspFilterRequest extends LevelCategoryRequest {
    wsp_search_request: any;
    language: string;

    constructor(data?) {
        super(data);
        if (!data) {
            return;
        }
        this.offset = data.offset || 0;
        this.size = 0; // used for get all item
        this.language = data.language;
        this.filter_mode = SEARCH_MODE.WSP_SEARCH;
        this.wsp_search_request = data.wsp_search_request;
    }
}