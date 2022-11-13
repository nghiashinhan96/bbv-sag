import { get } from 'lodash';
import { ArticleModel, AVAILABILITY_INFO } from 'sag-article-detail';

// @dynamic
export class ArticleSortUtil {

    public static sortByBestand(item1, item2, isAffiliateApplyDeliverableStock?: boolean) {
        let a = item1 && item1.stock && item1.stock.stock || 0;
        let b = item2 && item2.stock && item2.stock.stock || 0;
        if (isAffiliateApplyDeliverableStock) {
            a = item1 && item1.deliverableStock || 0;
            b = item2 && item2.deliverableStock || 0;
        }
        return b - a;
    }

    public static sortByPrioAndBestand(item1, item2, isAffiliateApplyDeliverableStock?: boolean) {
        const comparePrio = this.sortByPrio(item1, item2);
        if (comparePrio === 0) {
            return this.sortByBestand(item1, item2, isAffiliateApplyDeliverableStock);
        }
        return comparePrio;
    }

    public static sortByPrioHasBestand(item1, item2) {
        const a = item1.stock ? 1 : 0;
        const b = item2.stock ? 1 : 0;
        return b - a;
    }

    public static comparatorGroupSupplierAndSortLocation(item1, item2, ascessding) {
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

    public static sortByPrio(item1, item2) {
        return item1.prio - item2.prio;
    }

    public static comparatorAvail(item1, item2) {
        if (item1.availSortState > item2.availSortState) {
            return 1;
        }
        if (item1.availSortState < item2.availSortState) {
            return -1;
        }
        if (item1.availSortState === AVAILABILITY_INFO.IS_TOUR_STATE) {
            // compare time
            return new Date(item1.availabilities[0].arrivalTime).getTime() - new Date(item2.availabilities[0].arrivalTime).getTime();
        }
        return 0;
    }

    public static comparatorAvailCz(item1, item2) {
        if (item1.availSortState > item2.availSortState) {
            return 1;
        }
        if (item1.availSortState < item2.availSortState) {
            return -1;
        }
        if (item1.availSortState === AVAILABILITY_INFO.IS_TOUR_STATE) {
            // compare time
            return new Date(item1.availabilities[1].arrivalTime).getTime() - new Date(item2.availabilities[1].arrivalTime).getTime();
        }
        return 0;
    }

    public static comparatorStockAndPrio(item1, item2, isAffiliateApplyDeliverableStock?: boolean) {
        const sorted = this.sortByBestand(item1, item2, isAffiliateApplyDeliverableStock);
        if (sorted === 0) {
            return this.sortByPrio(item1, item2);
        }
        return sorted;
    }

    public static comparatorAvailAndPrioAndStock(item1, item2) {
        const sorted = this.comparatorAvail(item1, item2);
        if (sorted === 0) {
            return this.sortByPrioAndBestand(item1, item2);
        }
        return sorted;
    }

    public static comparatorAvailAndPrioHasStock(item1, item2) {
        const sorted = this.comparatorAvail(item1, item2);
        if (sorted === 0) {
            return this.sortByPrioHasBestand(item1, item2);
        }
        return sorted;
    }

    public static comparatorAvailAndPrioAndStockCz(item1, item2) {
        const sorted = this.comparatorAvailCz(item1, item2);
        if (sorted === 0) {
            return this.sortByPrioAndBestand(item1, item2);
        }
        return sorted;
    }

    public static comparatorPrioAndStockCz(item1, item2) {
        return this.sortByPrioAndBestand(item1, item2);
    }

    public static comparatorPrioHasStockCz(item1, item2) {
        return this.sortByPrioHasBestand(item1, item2);
    }

    public static groupBy(array, f) {
        const groups = {};
        array.forEach(o => {
            const group = f(o);
            groups[group] = groups[group] || [];
            groups[group].push(o);
        });
        return Object.keys(groups).map(group => {
            return groups[group];
        });
    }

    static sortByGrossPrice(articles: ArticleModel[], ascessding: boolean) {
        const sortedArticles = this.groupInStockAndSortPrice(articles, ascessding, false);
        return sortedArticles;
    }

    static sortByNetPrice(articles: ArticleModel[], ascessding: boolean) {
        const sortedArticles = this.groupInStockAndSortPrice(articles, ascessding, true);
        return sortedArticles;
    }

    private static groupInStockAndSortPrice(totalArticles: ArticleModel[], ascessding: boolean, isNetPrice: boolean) {
        const arrayWithStock = [];
        const arrayWithNoStock = [];
        const arrayPrice = [];
        for (const article of totalArticles) {
            if (article.stock) {
                arrayWithStock.push(article);
            } else {
                arrayWithNoStock.push(article);
            }
        }
        // sort gross price array with stock
        arrayWithStock.sort((item1, item2) => {
            return this.comparatorByPrice(item1, item2, ascessding, isNetPrice);
        });
        // sort gross price array with  no stock
        arrayWithNoStock.sort((item1, item2) => {
            return this.comparatorByPrice(item1, item2, ascessding, isNetPrice);
        });
        // mix together stock and no stock
        Array.prototype.push.apply(arrayPrice, arrayWithStock);
        Array.prototype.push.apply(arrayPrice, arrayWithNoStock);
        return arrayPrice;
    }

    private static comparatorByPrice(item1: ArticleModel, item2: ArticleModel, ascending: boolean, isNetPrice: boolean) {
        if (!this.getPrice(item1, isNetPrice)) {
            return 1;
        } else if (!this.getPrice(item2, isNetPrice)) {
            return -1;
        } else if (this.getPrice(item1, isNetPrice) === this.getPrice(item2, isNetPrice)) {
            return 0;
        } else if (ascending) {
            return this.getPrice(item1, isNetPrice) < this.getPrice(item2, isNetPrice) ? -1 : 1;
        } else if (!ascending) {
            return this.getPrice(item1, isNetPrice) < this.getPrice(item2, isNetPrice) ? 1 : -1;
        }
    }

    private static getPrice(item: ArticleModel, isNetPrice: boolean) {
        if (isNetPrice) {
            return this.getNetPrice(item)
        }
        return this.getGrossPrice(item);
    }

    private static getNetPrice(item: ArticleModel) {
        return get(item, 'price.price.netPrice', null);
    }

    private static getGrossPrice(item: ArticleModel) {
        return get(item, 'price.price.grossPrice', null) || get(item, 'price.price.originalBrandPrice', null);
    }
}
