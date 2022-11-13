import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { uniq } from 'lodash';
import { AnalyticLoggingService } from './analytic-logging.service';
import { AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class WspAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendEventData(request: any, isCameFromFavoriteSearch?: boolean, isCameFromGenartBreadcrumbClick?: boolean) {
        setTimeout(() => {
            const eventType = AnalyticEventType.WSP_CATALOG_EVENT;
            const wspRequest = this.analyticService.createWspCatalogEventData(request, isCameFromFavoriteSearch, isCameFromGenartBreadcrumbClick);
            this.analyticService.postEventFulltextSearch(wspRequest, eventType)
                .pipe(first())
                .toPromise();
        });
    }
}
