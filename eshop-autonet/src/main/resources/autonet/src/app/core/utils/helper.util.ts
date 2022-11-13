import { unescape } from 'lodash';
import { ArticleModel } from 'sag-article-detail';

const NOT_FOUND_AVAIL = {
    availState: 0,
    availStateColor: 'BLACK'
};

export class AppHelperUtil {
    public static convertAutonetData(articles: any[]) {
        return (articles || []).map((article: any) => {
            const autonetInfos = article && article.autonetInfos;
            const avail = autonetInfos && autonetInfos.availability || NOT_FOUND_AVAIL;

            //  this.price && this.price.price && this.price.price.netPrice
            avail.presentPath = `assets/avaiabilities/${avail.availStateColor}.gif`;
            avail.description = `AUTONET.AVAIL_STATE.${avail.availStateColor}`;
            avail.type = 'AUTONET';
            if (avail.availState === 0 || avail.availState === 2) {
                avail.availState = 99;
            }
            article.availabilities = [avail];
            const autonetPrices = autonetInfos && autonetInfos.prices || [];
            let firstPrice = null;
            if (autonetPrices.length > 0) {
                firstPrice = autonetPrices[0];
            }
            article.price = {
                autonet: autonetPrices,
                price: {
                    netPrice: firstPrice && firstPrice.value || null
                }
            };
            const autonetMemos = autonetInfos && autonetInfos.memos || [];
            article.memo = unescape(autonetMemos.map((m: any) => m.text).join(''));

            const idPim = article.id_pim || article.pimId;
            if (!idPim) {
                article.autonetRequested = true;
                article.availabilities = [avail];
            }

            return new ArticleModel(article);
        });
    }

    public static objectToUrl(baseUrl: string, params) {
        const query = Object.keys(params).reduce((queryString: string, k: string) => {
            if (!!params[k]) {
                const val = `${k}=${encodeURIComponent(params[k])}`;
                if (!queryString) {
                    return val;
                }
                return `${queryString}&${val}`;
            }
            return queryString;
        }, '');
        return `${baseUrl}?${query}`;
    }
}
