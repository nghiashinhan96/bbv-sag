import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { AnalyticEventType } from '../enums/event-type.enums';
import { Constant } from 'src/app/core/conts/app.constant';
import { AnalyticLoggingService } from './analytic-logging.service';
import { GoogleAnalyticsService } from './google-analytics.service';
import { GA_SEARCH_CATEGORIES } from '../enums/ga-search-category.enums';

@Injectable({ providedIn: 'root' })
export class TyreAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService
    ) { }

    sendPkwGaData(data: any) {
        setTimeout(() => {
            const season = data.season ? data.season.trim() : Constant.EMPTY_STRING;
            const width = data.width ? data.width.toString().trim() : Constant.EMPTY_STRING;
            const height = data.height ? data.height.toString().trim() : Constant.EMPTY_STRING;
            const radius = data.radius ? data.radius.toString().trim() : Constant.EMPTY_STRING;
            const speedIdx = data.speedIndex ? data.speedIndex.trim() : Constant.EMPTY_STRING;
            let tyreDimensions = Constant.EMPTY_STRING;
            tyreDimensions += season ? `season=${season};` : Constant.EMPTY_STRING;
            tyreDimensions += width ? `width=${width};` : Constant.EMPTY_STRING;
            tyreDimensions += height ? `height=${height};` : Constant.EMPTY_STRING;
            tyreDimensions += radius ? `radius=${radius};` : Constant.EMPTY_STRING;
            tyreDimensions += speedIdx ? `speed_index=${speedIdx};` : Constant.EMPTY_STRING;
            const tyreSupplier = data.supplier;
        });
    }

    sendMotorEvent(request: any) {
        setTimeout(() => {
            this.sendTyreEvent(request, Constant.TYRES_EVENT_MOTORRAD);
        });
    }

    sendPkwEvent(request: any) {
        setTimeout(() => {
            this.sendTyreEvent(request, Constant.TYRES_EVENT_PKW);
        });
    }

    private sendTyreEvent(request, selectedTab) {
        const eventType = AnalyticEventType.TYRE_EVENT;

        const pkwTyreRequest = this.analyticService.createTyreEventData(
            {
                ...request
            },
            {
                selectedTab,
                matchCodeSearch: !!(request.matchCode || '').trim(),
                numberOfHits: request.totalElements
            }
        );
        this.analyticService.postEventFulltextSearch(pkwTyreRequest, eventType)
            .pipe(first())
            .toPromise();
    }
}
