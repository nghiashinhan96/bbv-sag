import { ArticleModel } from 'sag-article-detail';

export class TyreArticleSortUtil {
    static sortTyreBySupplier(articles: ArticleModel[], ascessding: boolean) {
        const sortedArticles = this.groupInSuppliersAndSortLocation(articles, ascessding);
        return sortedArticles;
    }

    private static groupInSuppliersAndSortLocation(totalArticles: ArticleModel[], ascessding: boolean) {
        const arrayWithStock = [];
        const arrayWithNoStock = [];
        const arrayGroupSuppliersSortLocations = [];
        for (const article of totalArticles) {
            if (article.stock) {
                arrayWithStock.push(article);
            } else {
                arrayWithNoStock.push(article);
            }
        }
        // group with alphabet supplier and sort partPosition array with stock
        arrayWithStock.sort((item1, item2) => {
            return this.comparatorGroupSupplierAndSortLocation(item1, item2, ascessding);
        });
        // group with alphabet supplier and sort partPosition array with  no stock
        arrayWithNoStock.sort((item1, item2) => {
            return this.comparatorGroupSupplierAndSortLocation(item1, item2, ascessding);
        });
        // mix together stock and no stock
        Array.prototype.push.apply(arrayGroupSuppliersSortLocations, arrayWithStock);
        Array.prototype.push.apply(arrayGroupSuppliersSortLocations, arrayWithNoStock);
        return arrayGroupSuppliersSortLocations;
    }

    private static comparatorGroupSupplierAndSortLocation(item1: any, item2: any, ascessding: boolean) {
        if (!item1.supplier) {
            return 1;
        }
        if (!item2.supplier) {
            return -1;
        }
        if (item1.supplier === item2.supplier) {
            if (item1.partPosition > item2.partPosition) {
                return 1;
            }
            if (item1.partPosition < item2.partPosition) {
                return -1;
            }
            return 0;
        }
        if (ascessding) {
            return item1.supplier < item2.supplier ? -1 : 1;
        } else if (!ascessding) {
            return item1.supplier < item2.supplier ? 1 : -1;
        }
    }
}
