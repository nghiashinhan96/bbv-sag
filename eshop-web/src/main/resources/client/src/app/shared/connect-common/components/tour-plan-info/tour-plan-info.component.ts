import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AffiliateUtil } from 'sag-common';
import { DELIVERY_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { orderBy } from 'lodash';
import { TranslateService } from '@ngx-translate/core';
import { TourTime } from 'src/app/core/models/tour-time.model';
import { AppContextService } from 'src/app/core/services/app-context.service';

@Component({
  selector: 'connect-tour-plan-info',
  templateUrl: './tour-plan-info.component.html',
  styleUrls: ['./tour-plan-info.component.scss']
})
export class TourPlanInfoComponent implements OnInit, OnDestroy {
  @Input() isHideTourPlan = true;
  @Input() containerClass = 'tour-time-popover';

  DELIVERY_TYPE = DELIVERY_TYPE;
  isSalesOnBeHalf = false;
  salesUser = false;
  affiliateShowPlanTour = false;
  subs = new Subscription();

  constructor(
    private userService: UserService,
    private appContextService: AppContextService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.subs.add(
      this.userService.userDetail$
        .subscribe(userDetail => {
          if (userDetail) {
            this.isSalesOnBeHalf = userDetail && userDetail.isSalesOnBeHalf;
            this.salesUser = userDetail && userDetail.salesUser;
          }
        })
    );

    this.affiliateShowPlanTour = AffiliateUtil.isAffiliateShowPlanTour(environment.affiliate);
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  get shoppingBasketContext() {
    return this.appContextService.shoppingBasketContext;
  }

  get currentDeliveryType() {
    if (!this.affiliateShowPlanTour || (this.salesUser && !this.isSalesOnBeHalf)) {
      return null;
    }

    return this.shoppingBasketContext && this.shoppingBasketContext.deliveryType && this.shoppingBasketContext.deliveryType.descCode  || null;
  }

  get tourTimes() {
    const tours = this.shoppingBasketContext && this.shoppingBasketContext.tourTimes || [];

    if (tours.length > 0) {
      const data = orderBy(tours, ['tourName'], ['asc']);
      return data.map((dta: TourTime) => {
        const days = (dta.tourDaysConvert || []).map(day => this.translateService.instant(`MY_DATE_RANGE_PICKER.DAYS.${day}`));
        dta.tourDaysDisplay = days.join(', ');

        return dta;
      });
    }

    return tours;
  }
}
