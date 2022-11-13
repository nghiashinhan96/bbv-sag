import { ArticleModel } from 'sag-article-detail';

export class SortedArticleGroup {
    displayedArticles: ArticleModel[];
    nonDisplayedArticles?: ArticleModel[];
    type?: 'BRAND' | 'NO_BRAND';
}
