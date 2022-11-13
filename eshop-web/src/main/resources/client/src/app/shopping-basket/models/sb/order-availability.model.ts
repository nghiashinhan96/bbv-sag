import { ArticleModel } from 'sag-article-detail';
import { ArticleUtil } from 'src/app/core/utils/article.util';
export class OrderAvailabilityModel {
    article: ArticleModel;
    availabilityResponse: any;
    recycle: boolean;
    depot: boolean;
    pfand: boolean;
    voc: boolean;
    vrg: boolean;
    attachArticles: any;
    productTextWithArt: string;

    constructor(data = null) {
        if(data) {
            if(data.article) {
                data.article = new ArticleModel(data.article);
            }

            if(data.attachArticles) {
                data.attachArticles = (data.attachArticles || []).map(item => new ArticleModel(item));
            }

            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });

            const info = {
                depot: this.depot,
                pfand: this.pfand,
                recycle: this.recycle,
                voc: this.voc,
                vrg: this.vrg
            };

            this.productTextWithArt = ArticleUtil.getProductText(this.article, info);
        }
    }
}