import { ArticleBasketModel } from "sag-article-detail";

export interface SagArticleListIntegrationInterface {
    getArticleListAreAddedToCart(data: ArticleBasketModel[]): Promise<any[]>;
}
