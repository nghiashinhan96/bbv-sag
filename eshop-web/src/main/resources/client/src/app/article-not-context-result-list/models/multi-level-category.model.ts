import { LevelCategory } from './level-category.model';
import { SEARCH_MODE } from 'sag-article-list';

export class MultiLevelCategory extends LevelCategory {
    filterMode = SEARCH_MODE.FREE_TEXT.toString();
    multipleLevelGaids = [];
    useMultipleLevelAggregation = true;
    accessorySearchRequest: any;
    articlePartListSearchRequest: any;
    crossReferenceRequest?: any;
    keepDirectAndOriginalMatch?: boolean;

    constructor(searchMode: string, data?: any) {
        super(data);
        if (searchMode) {
            this.filterMode = searchMode;
        }
    }

    setCategory(filter: any) {
        this.multipleLevelGaids = filter;
        return this;
    }

    setAccessory(accessorySearchRequest: any) {
        this.accessorySearchRequest = accessorySearchRequest;
    }

    setArticlePartList(articlePartListSearchRequest: any) {
        this.articlePartListSearchRequest = articlePartListSearchRequest;
    }

    setCrossReference(crossReferenceRequest: any) {
        this.crossReferenceRequest = crossReferenceRequest;
    }

    setKeepDirectAndOriginalMatch(value: boolean) {
        this.keepDirectAndOriginalMatch = value;
    }
}
