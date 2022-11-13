import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { AnalyticLoggingService } from './analytic-logging.service';
import { AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class OilAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendAdsEventData() {
        this.analyticService.sendAdsEvent(AnalyticEventType.OIL_EVENT);
    }

    sendEventData(request: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.OIL_EVENT;

            const oilRequest = this.analyticService.createOilEventData(request);
            this.analyticService.postEventFulltextSearch(oilRequest, eventType)
                .pipe(first())
                .toPromise();
        });
    }
}
