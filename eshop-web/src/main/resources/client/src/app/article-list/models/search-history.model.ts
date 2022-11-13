import { ArticleModel } from 'sag-article-detail';
import { SEARCH_MODE } from 'sag-article-list';
import { SagMessageData } from 'sag-common';

export class SearchHistory {
    searchTerm: string;
    emitSearch?: boolean;
    isHadResult?: boolean;
    isAddedToCart?: boolean;
    isImported?: boolean;
    message?: SagMessageData;
    amount?: number;
    articles?: ArticleModel[];
    filter?: any;
    cartKeys?: any[] = [];
    searchType?: SEARCH_MODE;
    isShoppingList?: boolean;
    isImportedFromFile?: boolean;
}
