import { OfferArticleModel } from './offer-article.model';

export class OfferArticlesResponse {
    public articles: OfferArticleModel[];
    public pagi: number;
    public size: number;
    public total: number;

    constructor(res: any) {
        this.articles = res.content.map(item => new OfferArticleModel(item));
        this.pagi = res.number;
        this.size = res.size;
        this.total = res.totalElements;
    }
}
