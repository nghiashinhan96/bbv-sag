import { Injectable, TemplateRef } from '@angular/core';

import { SagInContextIntegrationInterface } from 'sag-in-context';
import { UserService } from 'src/app/core/services/user.service';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { ArticleBasketModel, CategoryModel } from 'sag-article-detail';
import { Observable } from 'rxjs';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';

@Injectable()
export class ArticlesInContextIntegrationService implements SagInContextIntegrationInterface {
    constructor(
        private userService: UserService,
        private appContextService: AppContextService,
        private shoppingBasketService: ShoppingBasketService,
        private articleShoppingBasketService: ArticleShoppingBasketService,
        private creditLimitService: CreditLimitService,
        private fbRecordingService: FeedbackRecordingService,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService
    ) { }

    // User Service

    get userDetail() {
        return this.userService.userDetail;
    }

    get userPrice() {
        return this.userService.userPrice;
    }

    get userSetting$() {
        return this.userService.userSetting$;
    }

    toggleNetPriceView() {
        return this.userService.toggleNetPriceView();
    }

    updatePriceSetting(setting: any) {
        return this.userService.updatePriceSetting(setting);
    }

    hasPermission(perms: string[]): Observable<boolean> {
        return this.userService.hasPermissions(perms);
    }

    toggleSingleSelectMode() {
        return this.userService.toggleSingleSelectMode();
    }

    // App Context Service

    updateVehicleContext(vehicleDoc?: any): Observable<any> {
        return this.appContextService.updateVehicleContext(vehicleDoc);
    }

    // Shopping Basket Service

    get basketQuantity$() {
        return this.shoppingBasketService.basketQuantity$;
    }

    updateOtherProcess(basket: any) {
        return this.shoppingBasketService.updateOtherProcess(basket);
    }

    // Article Shopping Basket Service

    observeArticleRemove(): void {
        return this.articleShoppingBasketService.observeArticleRemove();
    }

    observeArticleUpdate(): void {
        return this.articleShoppingBasketService.observeArticleUpdate();
    }

    loadMiniBasket(): Promise<any> {
        return this.articleShoppingBasketService.loadMiniBasket();
    }

    isAddedInCart(data: ArticleBasketModel): Promise<boolean> {
        return this.articleShoppingBasketService.isAddedInCart(data);
    }

    addItemToCart(data: ArticleBasketModel): void {
        return this.articleShoppingBasketService.addItemToCart(data);
    }

    updateBasketItem(data: ArticleBasketModel, callback?: any): void {
        return this.articleShoppingBasketService.updateBasketItem(data, callback);
    }

    updateTheInBasketArticle(articles: any, vehicleId: any): void {
        return this.articleShoppingBasketService.updateTheInBasketArticle(articles, vehicleId);
    }

    // Credit Limit Service

    get creditCardInfo$() {
        return this.creditLimitService.creditCardInfo$;
    }

    // Article Analytics Service

    get eventSource() {
        return this.articlesAnalyticService.eventSource;
    }

    set eventSource(value: string) {
        this.articlesAnalyticService.eventSource = value;
    }

    sendArticleListEventData(data: any) {
        this.articlesAnalyticService.sendArticleListEventData(data);
        this.gaService.viewProductDetails(data.article);
    }

    sendArticlesGaData(data: any, modalName = '') {
        return this.articlesAnalyticService.sendArticleListGaData(data, modalName);
    }

    sendCategoryEventData(categories: CategoryModel[], selectedCategories: CategoryModel[], vehicle: any) {
        return this.articlesAnalyticService.sendCategoryEventData(
            categories,
            selectedCategories,
            vehicle
        );
    }

    sendAdsEventData() {
        return this.articlesAnalyticService.sendAdsEventData();
    }

    // Feedback recording Service

    clearFeedbackArticleResults() {
        return this.fbRecordingService.clearArticleResults();
    }

    recordFeedbackArticleResult(article: any) {
        return this.fbRecordingService.recordArticleResult(article);
    }

    recordFeedbackVehicleInfo(vehicleInfo: string) {
        return this.fbRecordingService.recordVehicleInfo(vehicleInfo);
    }

    sendArticleResultData(articles, vehicleId?) {
        this.articlesAnalyticService.sendArticleResults(articles, vehicleId);
    }
}
