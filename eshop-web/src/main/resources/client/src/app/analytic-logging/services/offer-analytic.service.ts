import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { AnalyticLoggingService } from './analytic-logging.service';
import { ShoppingBasketEventType, AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class OfferAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendOfferEventData(offerItems: any) {
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                offerItems,
                {
                    source: ShoppingBasketEventType.OFFER
                }
            );
            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }
}
