import { FilterCategoryRequest } from './filter-category-request.model';

export class MerkmaleCategoryRequest extends FilterCategoryRequest {
    // tslint:disable-next-line: variable-name
    multiple_level_gaids = [];
    useMultipleLevelAggregation = true;
    accessory_search_request: any;
    article_part_list_search_request: any;
    crossReferenceRequest: any;
    keepDirectAndOriginalMatch?: boolean;

    constructor(data: any, hasChanged = false) {
        super(data);
        if (data) {
            this.multiple_level_gaids = data.multipleLevelGaids;
            this.useMultipleLevelAggregation = data.useMultipleLevelAggregation;
            this.accessory_search_request = data.accessorySearchRequest;
            this.article_part_list_search_request = data.articlePartListSearchRequest;
            this.crossReferenceRequest = data.crossReferenceRequest;
            this.keepDirectAndOriginalMatch = data.keepDirectAndOriginalMatch;
        }
        if (hasChanged) {
            this.context_key = '';
        }
    }
}
