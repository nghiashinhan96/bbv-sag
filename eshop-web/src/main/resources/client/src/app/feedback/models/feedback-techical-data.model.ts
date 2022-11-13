import { FeedbackVehicleSearch } from './feedback-vehicle-search.model';
import { FeedbackArticleSearch } from './feedback-article-search.model';
import { FeedbackUserSetting } from './feedback-user-setting.model';
import { FeedbackArticleResult } from './feedback-article-result.model';
import { FeedbackShoppingCartItem } from './feedback-shopping-cart-model';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackTechicalData {
    userId: number;
    website: string;
    freeTextSearchKey: string;
    vehicleSearch: FeedbackVehicleSearch;
    articleSearch: FeedbackArticleSearch;
    offerNr: string;
    vehicle: string;
    userSettings: FeedbackUserSetting;
    articleResults: FeedbackArticleResult[];
    cartItems: FeedbackShoppingCartItem[];

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.userId = data.userId;
        this.website = data.website;
        this.freeTextSearchKey = data.freeTextSearchKey;
        this.offerNr = data.offerNr;
        this.vehicle = data.vehicle;
        this.userSettings = new FeedbackUserSetting(data.userSettings);

        if (data.vehicleSearch) {
            const vehicleSearch = data.vehicleSearch;
            this.vehicleSearch = new FeedbackVehicleSearch({
                type: vehicleSearch.type,
                searchText: vehicleSearch.searchText
            });
        }

        if (data.articleSearch) {
            const articleSearch = data.articleSearch;
            this.articleSearch = new FeedbackArticleSearch({
                articleNr: articleSearch.articleNr,
                articleName: articleSearch.articleName
            });
        }

        if (data.articleResults && data.articleResults.length) {
            this.articleResults = [];
            data.articleResults.forEach(element => {
                this.articleResults.push(new FeedbackArticleResult(element));
            });
        }

        if (data.cartItems && data.cartItems.length) {
            this.cartItems = [];
            data.cartItems.forEach(element => {
                this.cartItems.push(new FeedbackShoppingCartItem(element));
            });
        }
    }

    get websiteValue(): string {
        return FeedbackUtils.getValue(this.website);
    }

    get freeTextSearchKeyValue(): string {
        return FeedbackUtils.getValue(this.freeTextSearchKey);
    }

    get offerNrValue(): string {
        return FeedbackUtils.getValue(this.offerNr);
    }

    get vehicleValue(): string {
        return FeedbackUtils.getValue(this.vehicle);
    }
}
