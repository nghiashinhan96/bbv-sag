import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { get } from 'lodash';
import { AnalyticEventType, ShoppingBasketEventType } from '../enums/event-type.enums';
import { ARTICLE_EVENT_SOURCE } from 'sag-common';
import { AnalyticLoggingService } from './analytic-logging.service';
import { GoogleAnalyticsService } from './google-analytics.service';
import { ArticleModel, CategoryModel } from 'sag-article-detail';
import {
    LIB_VEHICLE_ARTICLE_NUMBER_SEARCH,
    LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH
} from 'sag-article-search';
import { ARTICLE_SEARCH_TYPE } from '../enums/article-search-type.enums';

@Injectable({ providedIn: 'root' })
export class ArticlesAnalyticService {
    eventSource: string;

    constructor(
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService
    ) { }

    sendAddToBasketEventData(article: any, response: any, customBrand?: any) {
        setTimeout(() => {
            const shoppingBasketEventData = this.analyticService.createShoppingBasketEventData(
                response,
                {
                    source: this.getEventSource(article),
                    selectedArticles: [article.artid],
                    totalPrice: article.price && article.price.price && article.price.price.totalNetPrice || null,
                    addedArticle: article,
                    customBrand
                }
            );
            this.analyticService
                .postEventFulltextSearch(shoppingBasketEventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendCategoryEventData(categories: CategoryModel[], selectedCategories: CategoryModel[], vehicle: any) {
        setTimeout(() => {
            const eventData = {
                vehicleId: vehicle.id,
                categories,
                selectedCategories,
                eventSource: this.eventSource || ARTICLE_EVENT_SOURCE.EVENT_SOURCE_USER
            };

            if (selectedCategories && selectedCategories.length) {
                const dataEvent = this.analyticService.createArticleCategoryEventData(eventData);
                this.analyticService
                    .postEventFulltextSearch(dataEvent, AnalyticEventType.ARTICLE_CATEGORY_EVENT)
                    .pipe(first())
                    .toPromise();
            }
            this.eventSource = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_USER;
        });
    }

    sendArticleListEventData(data: any) {
        setTimeout(() => {
            const detailEventData = this.analyticService.createArticleListEventData(data.article);
            this.analyticService
                .postEventFulltextSearch(detailEventData, AnalyticEventType.ARTICLE_LIST_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendAdsEventData() {
        setTimeout(() => {
            this.analyticService.sendAdsEvent(AnalyticEventType.ARTICLE_LIST_EVENT);
        });
    }

    sendArticleListGaData(data: any, modalName = '') {
        setTimeout(() => {
            const googleAnalyticsArgs = this.gaService.prepareImpressionFieldObjectArgs(data);
            this.gaService.viewProductList(googleAnalyticsArgs, modalName);
        });
    }

    sendArticleResults(articles: any, vehicleId?) {
        setTimeout(() => {
            const shoppingBasketEventData = this.analyticService.createArticleResultsEventData(articles, vehicleId);
            this.analyticService
                .postEventFulltextSearch(shoppingBasketEventData, AnalyticEventType.ARTICLE_RESULTS)
                .pipe(first())
                .toPromise();
        });
    }

    sendArticleSearchEventData(data, articleSearchMode?: string) {
        setTimeout(() => {
            const eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
            let request: any;
            switch (data.searchType) {
                case LIB_VEHICLE_ARTICLE_NUMBER_SEARCH:
                    request = {
                        artSearchType: ARTICLE_SEARCH_TYPE.ARTICLE_NUMBER,
                        artSearchTermEntered: data.search,
                        artNumberOfResult: get(data, 'response.totalElements', 0).toPrecision()
                    };
                    break;
                case LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH:
                    request = {
                        artSearchType: ARTICLE_SEARCH_TYPE.ARTICLE_DESC,
                        artSearchTermEntered: data.search,
                        artNumberOfResult: get(data, 'response.totalElements', 0).toPrecision()
                    };
                    break;
            }
            const extras = articleSearchMode && { articleSearchMode } || null;
            const eventRequest = this.analyticService.createFullTextSearchArticleEventData(request, extras);
            if (eventRequest) {
                // GA4 Search event
                const sourceId = eventRequest.basket_item_source_id;
                const sourceDesc = eventRequest.basket_item_source_desc;
                const searchTerm = eventRequest.art_search_term_entered || '';
                this.gaService.search('', searchTerm, sourceId, sourceDesc);
    
                // Json event
                this.analyticService.postEventFulltextSearch(eventRequest, eventType)
                    .pipe(first())
                    .toPromise();
            }
        });
    }

    sendFulltextSearchEventData(data, extras?: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.FULL_TEXT_SEARCH_EVENT;

            const request = this.analyticService.createFullTextSearchEventData(data, extras || {});
            if (request) {
                // GA4 Search event
                const sourceId = request.basket_item_source_id;
                const sourceDesc = request.basket_item_source_desc;
                const searchTerm = request.fts_search_term_entered || '';
                this.gaService.search('', searchTerm, sourceId, sourceDesc);
    
                // Json event
                this.analyticService.postEventFulltextSearch(request, eventType)
                    .pipe(first())
                    .toPromise();
            }
        });
    }

    private getEventSource(article: ArticleModel) {
        switch (article && article.source) {
            case ARTICLE_EVENT_SOURCE.EVENT_SOURCE_GT:
                return ShoppingBasketEventType.GTMOTIVE;
            case ARTICLE_EVENT_SOURCE.EVENT_SOURCE_HP:
                return ShoppingBasketEventType.HAYNESPRO;
            case ARTICLE_EVENT_SOURCE.EVENT_SOURCE_DVSE:
                return ShoppingBasketEventType.DVSE;
            case ARTICLE_EVENT_SOURCE.EVENT_SOURCE_WSP:
                return ShoppingBasketEventType.WSP;
            default:
                return ShoppingBasketEventType.ARTICLE_LIST;
        }
    }
}
