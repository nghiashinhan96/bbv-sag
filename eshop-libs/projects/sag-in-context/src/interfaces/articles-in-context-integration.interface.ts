import { ArticleBasketModel, CategoryModel } from 'sag-article-detail';
import { Observable } from 'rxjs';

export abstract class SagInContextIntegrationInterface {
    constructor() { }

    // User Service
    abstract get userDetail(): any;
    abstract get userPrice(): any;
    abstract toggleNetPriceView(): void;
    abstract updatePriceSetting(setting?: any): void;
    abstract hasPermission(perms: string[]): Observable<boolean>;

    // App Context Service
    abstract updateVehicleContext(vehicleDoc?: any): Observable<any>;

    // Shopping Basket Service
    abstract get basketQuantity$(): Observable<number>;
    abstract updateOtherProcess(basket: any);

    // Article Shopping Basket Service
    abstract observeArticleRemove(): void;
    abstract observeArticleUpdate(): void;
    abstract loadMiniBasket(): Promise<any>;
    abstract isAddedInCart(data: ArticleBasketModel): Promise<boolean>;
    abstract addItemToCart(data: ArticleBasketModel): void;
    abstract updateBasketItem(data: ArticleBasketModel, callback?: any): void;
    abstract updateTheInBasketArticle(articles, vehicleId): void;

    // Credit Limit Service
    abstract get creditCardInfo$(): Observable<any>;

    // Article Analytics Service
    abstract get eventSource(): string;
    abstract set eventSource(value: string);
    abstract sendArticleListEventData(data: any): void;
    abstract sendArticlesGaData(data: any, type?: string): void;
    abstract sendCategoryEventData(categories: CategoryModel[], selectedCategories: CategoryModel[], vehicle: any): void;
    abstract sendAdsEventData(): void;

    // Feedback Recording Service
    abstract clearFeedbackArticleResults(): void;
    abstract recordFeedbackArticleResult(article: any): void;
    abstract recordFeedbackVehicleInfo(vehicleInfo: string): void;
    abstract sendArticleResultData(articles: any, vehicleId?: any): void;
}
