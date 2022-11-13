export class ArticleUtil {
    public static getProductText(article, data, isShownArtText = true) {
        if (data.depot) {
            if (data.pfand) {
                return 'SHOPPING_BASKET.PFAND_TEXT';
            }
            return 'SHOPPING_BASKET.DEPOT_TEXT';
        }

        if (data.recycle) {
            return 'SHOPPING_BASKET.RECYCLING_TEXT';
        }

        if (data.voc) {
            return 'SHOPPING_BASKET.VOC_TEXT';
        }

        if (data.vrg) {
            return 'SHOPPING_BASKET.VRG_TEXT';
        }

        if (!article) {
            return '';
        }

        if (isShownArtText) {
            return article.genArtTxts && article.genArtTxts.length > 0 ? article.genArtTxts[0].gatxtdech : '';
        }

        return '';
    }
}