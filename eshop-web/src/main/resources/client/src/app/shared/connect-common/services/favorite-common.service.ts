import { Injectable } from '@angular/core';

import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { SEARCH_MODE, VEHICLE_SOURCE } from 'sag-article-list';
import { ARTICLE_SEARCH_TYPE } from 'src/app/analytic-logging/enums/article-search-type.enums';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { VehicleSearchService } from 'src/app/home/service/vehicle-search.service';

@Injectable({
    providedIn: 'root'
})
export class FavoriteCommonService {

    constructor(
        private router: Router,
        private vehicleSearchService: VehicleSearchService,
        private analyticService: AnalyticLoggingService,
    ) { }

    navigateToFavoriteVehicle(item) {
        if (item.vinId) {
            const data = {
                searchMode: SEARCH_MODE.VIN_CODE,
                searchTerm: item.vinId
            };
            this.vehicleSearchService.navigateToVin(data);
            return;
        }
        this.router.navigate(['vehicle', item.vehicleId, 'quick-click'], {
            queryParams: {
                source: VEHICLE_SOURCE.FAVORITE
            }
        });
        this.sendVehicleJsonEvent(item);
    }

    private sendVehicleJsonEvent(item) {
        const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
        const request = this.analyticService.createFavoriteVehicleEventData(
            {
                vehIdResult: item.vehicleId,
                vehNameResult: item.title,
                vehSearchNumberOfHits: 1
            }
        );
        if (request) {
            this.analyticService.postEventFulltextSearch(request, eventType)
                .pipe(first())
                .toPromise();
        }
    }

    private sendArticleJsonEvent(item) {
        const eventType = AnalyticEventType.FULL_TEXT_SEARCH_ARTICLE_EVENT;
        let request = {
            artSearchType: ARTICLE_SEARCH_TYPE.ARTICLE_NUMBER,
            artIdResult: item.articleId,
            artNameResult: item.title,
            artNumberOfResult: 1
        };
        const eventRequest = this.analyticService.createFavoriteArticleEventData(request);
        this.analyticService
            .postEventFulltextSearch(eventRequest, eventType)
            .pipe(first())
            .toPromise();
    }

    navigateToFavoriteArticle(item) {
        const queryParams: any = {
            type: SEARCH_MODE.ID_SAGSYS,
            articleId: item.articleId
        };
        const url = ['article', 'result'];
        this.router.navigate(url, { queryParams });
        this.sendArticleJsonEvent(item);
    }
}