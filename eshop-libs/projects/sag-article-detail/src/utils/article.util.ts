import { ArticleModel } from "../models/article.model";

export class ArticleUtil {
    static assignArticleErpInfo(article: ArticleModel, artInfo: any, erpRequest: any, assignAvail = true, isEhCz = false) {
        if (!artInfo) {
            return;
        }
        if (erpRequest.availabilityRequested && !article.availRequested) {
            if (assignAvail) {
                article.availabilities = artInfo.availabilities;
            }
            article.availRequested = true;
        }
        if (erpRequest.stockRequested && !article.stockRequested) {
            article.stock = artInfo.stock;
            article.totalAxStock = artInfo.totalAxStock;
            article.stockRequested = true;
            article.deliverableStock = artInfo.deliverableStock;
        }
        if (erpRequest.priceRequested && !article.priceRequested) {
            article.price = artInfo.price;
            article.priceRequested = true;

            article.finalCustomerNetPrice = artInfo.finalCustomerNetPrice;
            article.totalFinalCustomerNetPrice = artInfo.totalFinalCustomerNetPrice;
            article.finalCustomerNetPriceWithVat = artInfo.finalCustomerNetPriceWithVat;
            article.totalFinalCustomerNetPriceWithVat = artInfo.totalFinalCustomerNetPriceWithVat;
        }
        article.allowedAddToShoppingCart = artInfo.allowedAddToShoppingCart;
        if (artInfo.depositArticle) {
            article.depositArticle = new ArticleModel(artInfo.depositArticle);
        }
        if (artInfo.vocArticle) {
            article.vocArticle = new ArticleModel(artInfo.vocArticle);
        }
        if (artInfo.vrgArticle) {
            article.vrgArticle = new ArticleModel(artInfo.vrgArticle);
        }
        if (artInfo.pfandArticle) {
            article.pfandArticle = new ArticleModel(artInfo.pfandArticle);
        }
        if (artInfo.memos) {
            article.memos = artInfo.memos;
        }
    }
}