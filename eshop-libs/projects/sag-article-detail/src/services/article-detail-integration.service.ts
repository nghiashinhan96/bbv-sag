import { Injectable } from '@angular/core';
import { SagArticleDetailIntegrationInterface } from '../interfaces/article-detail-integration.interface';
import { ArticleModel } from '../models/article.model';


@Injectable()
export class SagArticleDetailIntegrationService implements SagArticleDetailIntegrationInterface {
    constructor() { }

    get basketType() {
        throw new Error('Method not implemented.');
    }

    get userDetail() {
        throw new Error('Method not implemented.');
    }

    sendArticleListEventData(data: any) {
        throw new Error('Method not implemented.');
    }

    sendWishlistGaData(data: any, rootModalName?) {
        throw new Error('Method not implemented.');
    }

    sendShoppingBasketEvent(article: any, customBrand?: any) {
        throw new Error('Method not implemented.');
    }

    recordFeedbackAvailability(articleId, availabilities, isUpdateAvaiForArticleItemAndCartItem?) {
        throw new Error('Method not implemented.');
    }
}
