import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ReturnArticleItemEvent } from './return-article-item.event.model';

export class ReturnArticleEvent extends MetadataLogging {
    eventType = AnalyticEventType.FULL_TEXT_SEARCH_EVENT;
    ftsReturns: ReturnArticleItemEvent[] = [];

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any) {
        super(metadata, user);
        this.ftsReturns = (data || []).map(item => new ReturnArticleItemEvent(item));
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            fts_filter_selected: 'Retouren',
            fts_returns: this.ftsReturns.map(item => item.toEventRequest())
        };
    }
}
