import { ARTICLE_SEARCH_MODE } from "../enums/article-search.enums";
import { SearchTermUtil } from "../utils/search-term.util";

export class ArticleSearchHistory {
    id: string;
    articleId: string;
    artnrDisplay: string;
    createdBySales: boolean;
    fullName: string;
    searchMode: string;
    searchTermWithArtNr: string;
    searchTerm: string;
    searchTermDisplay: string;
    searchTermDisplayTxt: string;
    selectDate: string;
    freetextSearchMode: string;
    searchMethod: string;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.id = data.id;
        this.articleId = data.article_id;
        this.artnrDisplay = data.artnr_display;
        this.createdBySales = data.createdBySales;
        this.fullName = data.fullName;
        this.searchMode = data.searchMode;
        this.searchTermWithArtNr = data.searchTermWithArtNr;
        this.selectDate = data.select_date;

        const info = SearchTermUtil.getArticleFulltextSearchData(data.search_term);
        this.freetextSearchMode = info.mode;
        this.searchTerm = info.term;
        this.searchTermDisplay = info.term_display;
        this.searchTermDisplayTxt = info.term_display_txt;

        switch (this.searchMode) {
            case ARTICLE_SEARCH_MODE.ARTICLE_ID:
            case ARTICLE_SEARCH_MODE.FREE_TEXT:
                this.searchMethod = 'SEARCH.ARTICLE_FULLTEXT';
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_NUMBER:
                this.searchMethod = 'SEARCH.ARTICLE_TIPS';
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_DESC:
            case ARTICLE_SEARCH_MODE.SHOPPING_LIST:
                this.searchMethod = 'SEARCH.ARTICLE_DESC';
                break;
        }
    }
}
