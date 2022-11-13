import { MultiLevelCategory } from './multi-level-category.model';
import { LevelCategoryRequest } from '../../core/models/level-category.request.model';

export class MultiLevelCategoryRequest extends LevelCategoryRequest {
    // tslint:disable-next-line: variable-name
    multiple_level_gaids = [];
    useMultipleLevelAggregation = true;
    accessory_search_request: any;
    article_part_list_search_request: any;
    crossReferenceRequest: any;
    keepDirectAndOriginalMatch?: boolean;
    isShoppingList?: boolean;

    constructor(data: any, hasChanged = false) {
        super(data);
        if (data) {
            this.multiple_level_gaids = data.multipleLevelGaids;
            this.useMultipleLevelAggregation = data.useMultipleLevelAggregation;
            this.accessory_search_request = data.accessorySearchRequest;
            this.article_part_list_search_request = data.articlePartListSearchRequest;
            this.crossReferenceRequest = data.crossReferenceRequest;
            this.keepDirectAndOriginalMatch = data.keepDirectAndOriginalMatch;
            this.isShoppingList = data.isShoppingList;
        }
        if (hasChanged) {
            this.context_key = '';
        }
    }
}
