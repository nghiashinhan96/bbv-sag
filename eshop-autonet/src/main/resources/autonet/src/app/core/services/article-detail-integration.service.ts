import { Injectable } from '@angular/core';
import { SagArticleDetailIntegrationService } from 'sag-article-detail';

@Injectable({
    providedIn: 'root'
})
export class ArticleDetailIntegrationService implements SagArticleDetailIntegrationService {
    constructor() { }

    get userDetail() {
        return {} as any;
    }

    get basketType() {
        return null;
    }

    sendArticleListEventData() {
    }

    sendWishlistGaData(data: any) {
    }

    sendShoppingBasketEvent() {
    }

    recordFeedbackAvailability() {
    }
}
