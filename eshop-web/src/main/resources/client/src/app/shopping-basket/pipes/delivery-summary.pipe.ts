import { Pipe, PipeTransform } from '@angular/core';
import { ShoppingConditionHeaderModel } from '../models/shopping-condition-header.model';
import { TranslateService } from '@ngx-translate/core';
import { ArticleAvailabilityModel } from 'sag-article-detail';
import { AppCommonService } from 'src/app/core/services/app-common.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { UserService } from 'src/app/core/services/user.service';

const PICKUPTIME = 'pickupTime';
const TOURTIME = 'tourTime';

@Pipe({
    name: 'deliverySummary'
})
export class DeliverySummaryPipe implements PipeTransform {
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);

    constructor(
        private translateService: TranslateService,
        private appCommonService: AppCommonService,
        private userService: UserService
    ) { }

    transform(value: string, condition: ShoppingConditionHeaderModel, deliverySummary: {
        lastestAvail: ArticleAvailabilityModel,
        has24HoursArticle: boolean
    }): any {
        if (this.userService.userDetail.isFinalUserRole) {
            let timeInfo = '';
            let deliveryTime = this.getDeliveryTimeFC(deliverySummary);
            const atTheText = this.translateService.instant('CONDITION.AT_THE');
            const partialCollectionMessage = this.getPartial(condition.partialOrCollective);

            if (value === TOURTIME) {
                let tourText = 'CONDITION.FC_TOUR_TIME';
                timeInfo = this.translateService.instant(tourText, { param: partialCollectionMessage });
            }

            if (value === PICKUPTIME) {
                if (this.isSofort(deliverySummary)) {
                    deliveryTime = this.translateService.instant('ARTICLE.DELIVERY_IMMEDIATE');
                }
                let pickupText = 'CONDITION.FC_PICKUP_TIME';
                timeInfo = this.translateService.instant(pickupText, { param: partialCollectionMessage });
            }

            return `${timeInfo} ${atTheText}: ${deliveryTime}`;
        } else {
            let deliveryText = '';
            let deliveryTime = this.getDeliveryTime(deliverySummary);

            if (value === TOURTIME) {
                deliveryText = 'CONDITION.DELIVERY_TEXT_TOUR_WITHOUT_TIME';

                if (deliveryTime && deliveryTime.formattedCETArrivalDate) {
                    deliveryText = 'CONDITION.DELIVERY_TEXT_TOUR_WITH_TIME';
                }
            }

            if (value === PICKUPTIME) {
                if (this.isSofort(deliverySummary)) {
                    deliveryTime = this.translateService.instant('ARTICLE.DELIVERY_IMMEDIATE');
                    deliveryText = 'CONDITION.DELIVERY_TEXT_PICKUP_WITHOUT_TIME';
                } else {
                    if (deliveryTime && deliveryTime.formattedCETArrivalDate) {
                        deliveryText = 'CONDITION.DELIVERY_TEXT_PICKUP_WITH_TIME';
                    } else {
                        deliveryText = 'CONDITION.DELIVERY_TEXT_PICKUP_WITHOUT_TIME';
                    }
                }
            }

            if (deliveryText.indexOf('WITH_TIME') > -1) {
                return this.translateService.instant(deliveryText, {
                    arrivalDate: deliveryTime && deliveryTime.formattedCETArrivalDate || '',
                    arrivalTime: deliveryTime && deliveryTime.formattedCETArrivalTime || ''
                });
            } else {
                return this.translateService.instant(deliveryText, {
                    arrivalValue: deliveryTime || ''
                });
            }
        }
    }

    private getDeliveryTime(deliverySummary) {
        if (deliverySummary && deliverySummary.has24HoursArticle) {
            return this.appCommonService.getDeliveryTextApplyAffiliateSetting();
        }
        return deliverySummary && deliverySummary.lastestAvail || null;
    }

    private getDeliveryTimeFC(deliverySummary) {
        if (deliverySummary && deliverySummary.has24HoursArticle) {
            return this.appCommonService.getDeliveryTextApplyAffiliateSetting();
        }
        const lastestAvail = deliverySummary && deliverySummary.lastestAvail || {};
        const aroundText = this.translateService.instant('SETTINGS.MY_ORDER.DETAIL.AROUND');
        const atTheSuffixText = this.translateService.instant('CONDITION.AT_THE_SUFFIX');
        return `${lastestAvail.formattedCETArrivalDate || ''} ${aroundText} ${lastestAvail.formattedCETArrivalTime || ''}${atTheSuffixText}`;
    }

    private getPartial(partialOrCollective) {
        if (this.userService.userDetail.isFinalUserRole) {
            return this.translateService.instant('CONDITION.COLLECTIVE_DELIVERY_TITLE');
        }
        if (partialOrCollective === 'COLLECTIVE_DELIVERY1') {
            return this.translateService.instant('CONDITION.PARTIAL_DELIVERY_TITLE');
        } else {
            return this.translateService.instant('CONDITION.COLLECTIVE_DELIVERY_TITLE');
        }
    }

    private isSofort(deliverySummary) {
        const isExistingDelivery24Hours = deliverySummary && deliverySummary.has24HoursArticle;
        const latestAvali = deliverySummary && deliverySummary.lastestAvail || {};
        return latestAvali && latestAvali.sofort && !isExistingDelivery24Hours;
    }
}
