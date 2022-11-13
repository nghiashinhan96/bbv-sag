import { Component, OnInit, OnDestroy } from '@angular/core';

import { switchMap } from 'rxjs/operators';
import { Subscription, of } from 'rxjs';

import { UserService } from 'src/app/core/services/user.service';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { WholesalerTabModel } from '../../models/wholesaler-tab.model';

@Component({
    selector: 'connect-wholesaler',
    templateUrl: './wholesaler.component.html',
    styleUrls: ['./wholesaler.component.scss']
})
export class WholesalerComponent implements OnInit, OnDestroy {

    tabs: WholesalerTabModel[];
    private sub: Subscription;

    constructor(private userService: UserService) { }

    ngOnInit() {
        this.sub = this.userService.userDetail$.pipe(
            switchMap((userDetail) => {
                if (userDetail) {
                    this.tabs = this.getTabList(userDetail);
                }
                return of(null);
            }),
        ).subscribe();
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    getTabList(userDetail: UserDetail) {
        return [
            {
                routerLink: 'wholesaler',
                routerText: 'SETTINGS.MANAGE_CUSTOMERS_TAB',
                isShown: !!userDetail && userDetail.hasWholesalerPermission && !userDetail.isSalesOnBeHalf
            },
            {
                routerLink: 'opening-day',
                routerText: 'OPENING_DAY.OPENING_DAY_TITLE',
                isShown: true
            },
            {
                routerLink: 'opening-time',
                routerText: 'BRANCHES.BRANCH_LIST_MENU_TITLE',
                isShown: true
            },
            {
                routerLink: 'tour-management',
                routerText: 'DELIVERY_PROFILE.TOURS',
                isShown: true
            },
            {
                routerLink: 'delivery-profile',
                routerText: 'DELIVERY_PROFILE.DELIVERY_PROFILE',
                isShown: true
            },
            {
                routerLink: 'margin-management',
                routerText: 'WSS.DISCOUNT_MANAGE',
                isShown: true
            }
        ];
    }
}
