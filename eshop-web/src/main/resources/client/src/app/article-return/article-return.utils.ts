import { DEPOT_ARTICLE_SUFFIX } from "./consts/article-return.const";

export class ArticleReturnUtils {
    static isDepotArticle(article) {
        return (article.articleKeyword || '').endsWith(DEPOT_ARTICLE_SUFFIX) || (article.articleName || '').endsWith(DEPOT_ARTICLE_SUFFIX);
    }
}