import { SagInContextIntegrationInterface } from '../interfaces/articles-in-context-integration.interface';
import { Injectable } from '@angular/core';
import { ArticleBasketModel, CategoryModel } from 'sag-article-detail';
import { Observable } from 'rxjs';

@Injectable()
export class SagInContextIntegrationService implements SagInContextIntegrationInterface {
    constructor() { }

    // User Service

    get userDetail() {
        throw new Error('Method not implemented.');
    }

    get userPrice() {
        throw new Error('Method not implemented.');
    }

    get userSetting$(): Observable<any> {
        throw new Error('Method not implemented.');
    }

    updatePriceSetting() {
        throw new Error('Method not implemented.');
    }

    hasPermission(perms: string[]): Observable<boolean> {
        throw new Error('Method not implemented.');
    }

    toggleNetPriceView() {
        throw new Error('Method not implemented.');
    }

    toggleSingleSelectMode() {
        throw new Error('Method not implemented.');
    }

    // App Context Service

    updateVehicleContext(vehicleDoc?: any): Observable<any> {
        throw new Error('Method not implemented.');
    }

    // Shopping Basket Service
    get basketQuantity$(): Observable<number> {
        throw new Error('Method not implemented.');
    }

    updateOtherProcess(basket: any) {
        throw new Error('Method not implemented.');
    }

    // Article Shopping Basket Service
    observeArticleRemove() {
        throw new Error('Method not implemented.');
    }

    observeArticleUpdate() {
        throw new Error('Method not implemented.');
    }

    loadMiniBasket(): Promise<any> {
        throw new Error('Method not implemented.');
    }

    isAddedInCart(data: ArticleBasketModel): Promise<boolean> {
        throw new Error('Method not implemented.');
    }

    addItemToCart(data: ArticleBasketModel) {
        throw new Error('Method not implemented.');
    }

    updateBasketItem(data: ArticleBasketModel, callback?: any) {
        throw new Error('Method not implemented.');
    }

    updateTheInBasketArticle(articles: any, vehicleId: any) {
        throw new Error('Method not implemented.');
    }

    // Credit Limit Service

    get creditCardInfo$(): Observable<any> {
        throw new Error('Method not implemented.');
    }

    // Article Analytics Service
    get eventSource() {
        throw new Error('Method not implemented.');
    }

    set eventSource(value: string) {
        throw new Error('Method not implemented.');
    }

    sendArticleListEventData(data: any) {
        throw new Error('Method not implemented.');
    }

    sendArticlesGaData(data: any, type?: string) {
        throw new Error('Method not implemented.');
    }

    sendCategoryEventData(categories: CategoryModel[], selectedCategories: CategoryModel[], vehicle: any): void {
        throw new Error('Method not implemented.');
    }

    sendAdsEventData() {
        throw new Error('Method not implemented.');
    }

    // Feedback Recording Service

    clearFeedbackArticleResults() {
        throw new Error('Method not implemented.');
    }

    recordFeedbackArticleResult(article: any) {
        throw new Error('Method not implemented.');
    }

    recordFeedbackVehicleInfo(vehicleInfo: string) {
        throw new Error('Method not implemented.');
    }

    sendArticleResultData(articles, vehicleId?) {
        throw new Error('Method not implemented.');
    }
}
