import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { AnalyticLoggingService } from './analytic-logging.service';
import { AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class BulbAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendAdsEventData() {
        this.analyticService.sendAdsEvent(AnalyticEventType.BULB_EVENT);
    }

    sendEventData(request: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.BULB_EVENT;

            const bulbsRequest = this.analyticService.createBulbEventData(
                request,
                { totalItems: request.totalElements }
            );
            this.analyticService.postEventFulltextSearch(bulbsRequest, eventType)
                .pipe(first())
                .toPromise();
        });
    }
}
