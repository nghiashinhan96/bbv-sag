import { ArticleModel, RELEVANCE_GROUP } from "sag-article-detail";
import { ArticleSortUtil } from "./article-sort.util";

export class ArticleGroupUtil {
    public static groupByRelevance(articles: ArticleModel[]) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.relevanceGroupType]);
        groups.sort((g1, g2) => g1[0].relevanceGroupOrder - g2[0].relevanceGroupOrder);
        return groups.map(g => {
            const key = g[0].relevanceGroupType;
            return {
                key,
                ignoreNonAvailFilter: key === RELEVANCE_GROUP.DIRECT_MATCH || key === RELEVANCE_GROUP.ORIGINAL_PART,
                originalValues: g,
                values: g
            };
        });
    }
}