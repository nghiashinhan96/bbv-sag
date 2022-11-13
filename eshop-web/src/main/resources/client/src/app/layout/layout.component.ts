import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { get } from 'lodash';
import { UserService } from '../core/services/user.service';
import { FeedbackRecordingService } from '../feedback/services/feedback-recording.service';
import { UserDetail } from '../core/models/user-detail.model';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { GoogleAnalyticsService } from '../analytic-logging/services/google-analytics.service';
import { SEARCH_MODE } from 'sag-article-list';
@Component({
    selector: 'connect-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, OnDestroy {
    private subs = new SubSink();

    isShown = true;
    isSalesAuthedUser;
    ignoreCustomerInfo = false;
    isShownMemo = false;
    isShownCustomerInfo = false;

    constructor(
        private router: Router,
        private activateRoute: ActivatedRoute,
        private fbRecordingService: FeedbackRecordingService,
        private gaService: GoogleAnalyticsService,
        public userService: UserService,
    ) {
        this.router.events.subscribe(evt => {
            if (evt instanceof NavigationEnd) {
                const url = location.href;
                let params = new URL(url);

                // hide this from shopping basket component
                this.isShown = url.indexOf('/shopping-basket') === -1;
                this.ignoreCustomerInfo = url.indexOf('/home') > -1 ||
                    url.indexOf('/shopping-basket') > -1 || url.indexOf('/return') > -1 || url.indexOf('/dvse') > -1;
                this.isShownMemo = url.indexOf('/article-list') > -1;
                this.isShownCustomerInfo = get(this.userService, 'userDetail.isSalesOnBeHalf') && !this.ignoreCustomerInfo;

                const searchType = this.getSearchType(params);
                if (searchType) {
                    this.gaService.setGAListName(searchType);
                }
            }
        });
    }

    ngOnInit(): void {
        this.subs.add(this.userService.userDetail$.subscribe((user: UserDetail) => {
            if (!user) {
                return;
            }
            this.isSalesAuthedUser = user.salesUser || user.isSalesOnBeHalf;
            this.fbRecordingService.reloadData(user);
        }));
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    get enableFeedback() {
        return !AffiliateUtil.isEhCh(environment.affiliate) && !AffiliateUtil.isEhCz(environment.affiliate);
    }

    private getSearchType(params: URL) {
        let searchType = params.searchParams.get('type') || params.searchParams.get('searchMode');
        if (searchType) {
            return searchType;
        }
        if (location.href.indexOf('wsp') >= 0) {
            return SEARCH_MODE.WSP_SEARCH;
        }
        if (location.href.indexOf('article-list') >= 0) {
            return SEARCH_MODE.SHOPPING_LIST_RESULTS;
        }
        if (location.href.indexOf('shopping-basket') >= 0) {
            return SEARCH_MODE.SHOPPING_BASKET;
        }
        if (location.href.indexOf('vehicle') >= 0) {
            return SEARCH_MODE.VEHICLE_DESC;
        }
        if (location.href.indexOf('article') >= 0) {
            return SEARCH_MODE.ARTICLE_NUMBER;
        }
        return '';
    }
}
