import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { uniq } from 'lodash';
import { AnalyticLoggingService } from './analytic-logging.service';
import { AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class BatteryAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendAdsEventData() {
        this.analyticService.sendAdsEvent(AnalyticEventType.BATTERIES_EVENT);
    }

    sendEventData(request: any, articles: any[]) {
        setTimeout(() => {
            const eventType = AnalyticEventType.BATTERIES_EVENT;
            const articleIds = uniq(articles.map(data => data.artnr_display));
            const articleNames = uniq(articles.map(data => data.name));
            const batteryRequest = this.analyticService.createBatteryEventData(
                {
                    ...request,
                    articleIdResult: articleIds,
                    articleNameResult: articleNames
                },
                { totalItems: request.totalElements }
            );
            this.analyticService.postEventFulltextSearch(batteryRequest, eventType)
                .pipe(first())
                .toPromise();
        });
    }
}
