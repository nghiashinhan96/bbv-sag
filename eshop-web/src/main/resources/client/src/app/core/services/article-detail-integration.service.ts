import { Injectable } from '@angular/core';
import { SagArticleDetailIntegrationInterface } from 'sag-article-detail/interfaces/article-detail-integration.interface';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { ShoppingBasketAnalyticService } from 'src/app/analytic-logging/services/shopping-basket-analytic.service';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { UserDetail } from '../models/user-detail.model';
import { ShoppingBasketService } from './shopping-basket.service';
import { UserService } from './user.service';

@Injectable({
    providedIn: 'root'
})
export class ArticleDetailIntegrationService implements SagArticleDetailIntegrationInterface {
    constructor(
        private articlesAnalyticService: ArticlesAnalyticService,
        private shoppingBasketAnalyticService: ShoppingBasketAnalyticService,
        private feedbackRecordingService: FeedbackRecordingService,
        private userService: UserService,
        private shoppingBasketService: ShoppingBasketService,
        private gaService: GoogleAnalyticsService
    ) { }

    get basketType() {
        return this.shoppingBasketService.basketType;
    }

    get userDetail() {
        return this.userService.userDetail;
    }

    sendArticleListEventData(data: any) {
        this.articlesAnalyticService.sendArticleListEventData(data);
    }

    sendWishlistGaData(data: any, modalName = '') {
        this.gaService.addToWishlist(data, modalName);
    }

    sendShoppingBasketEvent(article: any, customBrand?: any) {
        this.shoppingBasketAnalyticService.sendShoppingBasketEvent(article, customBrand);
    }

    recordFeedbackAvailability(articleId, availabilities, isUpdateAvaiForArticleItemAndCartItem?) {
        this.feedbackRecordingService.recordAvailability(articleId, availabilities, isUpdateAvaiForArticleItemAndCartItem);
    }
}
