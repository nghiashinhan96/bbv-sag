import { ArticleModel } from 'sag-article-detail';

export class BasketHistoryArticleModel {
    amountNumber: string;
    articleNr: string;
    info: string;
    manufacturer: string;
    vehicle: string;
    article: ArticleModel;
    constructor(article: ArticleModel, vehicleInfo: string) {
        this.amountNumber = (article.amountNumber || '-').toString();
        this.articleNr = article.artnrDisplay || '-';
        this.info = (article.artDesc || '') + (article.genArtTxts || []).reduce((result, genArtTxt) => {
            return `${result}<br>${genArtTxt.gatxtdech || '-'}`;
        }, '');
        this.manufacturer = article.dlnrId;
        this.vehicle = vehicleInfo || '-';
        this.article = article;
    }
}
