import { Injectable } from '@angular/core';
import { PercentPipe } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { DiscountType, DiscountItemType } from '../enums/discount.enums';
import { OfferPosition } from '../models/offer-position.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { SagCurrencyPipe } from 'sag-currency';

const PERCENT_DIGITS = '1.2';
const AMOUNT_PREFIX = '1x ';
const DEFAULT_AMOUNT_ST = '0';
const DEFAULT_AMOUNT = 0;
const DEFAULT_LOCALE_ID = 'en-US';

@Injectable()
export class OfferDiscountService {
    public couponTypes: Array<any>;

    constructor(
        private translateService: TranslateService,
        private currencyPipe: SagCurrencyPipe
    ) {
        this.couponTypes = [
            {
                value: DiscountType.DISCOUNT_PERCENT,
                label: 'OFFERS.DISCOUNT.DISCOUNT_IN_PERCENT'
            },
            {
                value: DiscountType.DISCOUNT_AMOUNT,
                label: 'OFFERS.DISCOUNT.DISCOUNT_AS_AMOUNT'
            },
            {
                value: DiscountType.DISCOUNT_PERCENT_WORK,
                label: 'OFFERS.DISCOUNT.DISCOUNT_IN_PERCENT_ON_WORKS'
            },
            {
                value: DiscountType.DISCOUNT_AMOUNT_WORK,
                label: 'OFFERS.DISCOUNT.DISCOUNT_AS_AN_AMOUNT_ON_WORKS'
            },
            {
                value: DiscountType.DISCOUNT_PERCENT_ARTICLES,
                label: 'OFFERS.DISCOUNT.DISCOUNT_IN_PERCENT_ON_ARTICLES'
            },
            {
                value: DiscountType.DISCOUNT_AMOUNT_ARTICLES,
                label: 'OFFERS.DISCOUNT.DISCOUNT_AS_AN_AMOUNT_ON_ARTICLES'
            },
            {
                value: DiscountType.ADDITION_PERCENT,
                label: 'OFFERS.DISCOUNT.ADDITION_IN_PERCENT'
            },
            {
                value: DiscountType.ADDITION_AMOUNT,
                label: 'OFFERS.DISCOUNT.ADDITION_AS_AMOUNT'
            },
            {
                value: DiscountType.ADDITION_PERCENT_WORK,
                label: 'OFFERS.DISCOUNT.ADDITION_IN_PERCENT_ON_WORKS'
            },
            {
                value: DiscountType.ADDITION_AMOUNT_WORK,
                label: 'OFFERS.DISCOUNT.ADDITION_AS_AN_AMOUNT_ON_WORKS'
            },
            {
                value: DiscountType.ADDITION_PERCENT_ARTICLES,
                label: 'OFFERS.DISCOUNT.ADDITION_IN_PERCENT_ON_ARTICLES'
            },
            {
                value: DiscountType.ADDITION_AMOUNT_ARTICLES,
                label: 'OFFERS.DISCOUNT.ADDITION_AS_AN_AMOUNT_ON_ARTICLES'
            },
        ];
    }

    public isDiscountOrSurChargeByPercent(type: DiscountType) {
        return (
            this.isDiscountByPercent(type) ||
            this.isSurChargeByPercent(type)
        );
    }

    private isDiscountByPercent(type: DiscountType) {
        return (
            type === DiscountType.DISCOUNT_PERCENT ||
            type === DiscountType.DISCOUNT_PERCENT_WORK ||
            type === DiscountType.DISCOUNT_PERCENT_ARTICLES
        );
    }

    private isSurChargeByPercent(type: DiscountType) {
        return (
            type === DiscountType.ADDITION_PERCENT ||
            type === DiscountType.ADDITION_PERCENT_WORK ||
            type === DiscountType.ADDITION_PERCENT_ARTICLES
        );
    }

    public isDiscountOrSurChargeAsAmount(type: DiscountType) {
        return (
            this.isDiscountAsAmount(type) ||
            this.isSurChargeAsAmount(type)
        );
    }

    private isDiscountAsAmount(type: DiscountType) {
        return (
            type === DiscountType.DISCOUNT_AMOUNT ||
            type === DiscountType.DISCOUNT_AMOUNT_WORK ||
            type === DiscountType.DISCOUNT_AMOUNT_ARTICLES
        );
    }

    private isSurChargeAsAmount(type: DiscountType) {
        return (
            type === DiscountType.ADDITION_AMOUNT ||
            type === DiscountType.ADDITION_AMOUNT_WORK ||
            type === DiscountType.ADDITION_AMOUNT_ARTICLES
        );
    }

    public isDiscount(type: DiscountType) {
        return (
            this.isDiscountByPercent(type) ||
            this.isDiscountAsAmount(type)
        );
    }

    public buildDiscountDescriptionAsString(item: OfferPosition): string {
        let description: string;
        if (item.remark) {
            description = item.remark.concat('(');
        } else {
            description = '(';
        }
        switch (item.actionType) {
            case DiscountType[DiscountType.DISCOUNT_PERCENT]:
            case DiscountType[DiscountType.ADDITION_PERCENT]:
                description = description.concat(this.convertToPercent(item.actionValue));
                break;
            case DiscountType[DiscountType.DISCOUNT_AMOUNT]:
            case DiscountType[DiscountType.ADDITION_AMOUNT]:
                description = description
                    .concat(AMOUNT_PREFIX)
                    .concat(this.currencyPipe.transform(item.actionValue));
                break;
            case DiscountType[DiscountType.DISCOUNT_PERCENT_ARTICLES]:
            case DiscountType[DiscountType.ADDITION_PERCENT_ARTICLES]:
                description = description
                    .concat(this.convertToPercent(item.actionValue))
                    .concat(Constant.SPACE)
                    .concat(this.translateService.instant('OFFERS.DISCOUNT.ON_ALL_OWN_ARTICLE'));
                break;
            case DiscountType[DiscountType.DISCOUNT_PERCENT_WORK]:
            case DiscountType[DiscountType.ADDITION_PERCENT_WORK]:
                description = description
                    .concat(this.convertToPercent(item.actionValue))
                    .concat(Constant.SPACE)
                    .concat(this.translateService.instant('OFFERS.DISCOUNT.ON_ALL_OWN_WORK'));
                break;
            case DiscountType[DiscountType.DISCOUNT_AMOUNT_ARTICLES]:
            case DiscountType[DiscountType.ADDITION_AMOUNT_ARTICLES]:
                description = description
                    .concat(AMOUNT_PREFIX)
                    .concat(this.currencyPipe.transform(item.actionValue))
                    .concat(Constant.SPACE)
                    .concat(this.translateService.instant('OFFERS.DISCOUNT.ON_ALL_OWN_ARTICLE'));
                break;
            case DiscountType[DiscountType.DISCOUNT_AMOUNT_WORK]:
            case DiscountType[DiscountType.ADDITION_AMOUNT_WORK]:
                description = description
                    .concat(AMOUNT_PREFIX)
                    .concat(this.currencyPipe.transform(item.actionValue))
                    .concat(Constant.SPACE)
                    .concat(this.translateService.instant('OFFERS.DISCOUNT.ON_ALL_OWN_WORK'));
                break;
            default:
                break;
        }
        description += ')';
        return description;
    }

    public getDiscountTypes(discountType: DiscountItemType, totalArticle?: number, totalOwnArticle?: number, totalOwnWork?: number) {
        switch (discountType) {
            case DiscountItemType.OFFER:
                if ((totalArticle !== 0) && totalOwnArticle === 0 && totalOwnWork === 0) {
                    return this.getDiscountTypesForOfferExceptWorkAndArticle();
                }
                if (totalOwnArticle === 0) {
                    return this.getDiscountTypesForOfferExceptArticle();
                }
                if (totalOwnWork === 0) {
                    return this.getDiscountTypesForOfferExceptWork();
                }
                return this.getDiscountTypesForOffer();

            case DiscountItemType.ITEM:
                return this.getDiscountTypesForItem();
            default:
                return null;
        }
    }

    private getDiscountTypesForItem(): Array<any> {
        return this.couponTypes.filter(item => {
            const type = Number(item.value) as DiscountType;
            return (
                type === DiscountType.DISCOUNT_PERCENT ||
                type === DiscountType.DISCOUNT_AMOUNT ||
                type === DiscountType.ADDITION_PERCENT ||
                type === DiscountType.ADDITION_AMOUNT
            );
        });
    }

    private getDiscountTypesForOfferExceptArticle(): Array<any> {
        return this.couponTypes.filter(item => {
            const type = Number(item.value) as DiscountType;
            return (
                type === DiscountType.DISCOUNT_PERCENT ||
                type === DiscountType.DISCOUNT_AMOUNT ||
                type === DiscountType.DISCOUNT_PERCENT_WORK ||
                type === DiscountType.DISCOUNT_AMOUNT_WORK ||
                type === DiscountType.ADDITION_PERCENT ||
                type === DiscountType.ADDITION_AMOUNT ||
                type === DiscountType.ADDITION_PERCENT_WORK ||
                type === DiscountType.ADDITION_AMOUNT_WORK
            );
        });
    }

    private getDiscountTypesForOffer(): Array<any> {
        return this.couponTypes;
    }

    private getDiscountTypesForOfferExceptWork(): Array<any> {
        return this.couponTypes.filter(item => {
            const type = Number(item.value) as DiscountType;
            return (
                type === DiscountType.DISCOUNT_PERCENT ||
                type === DiscountType.DISCOUNT_AMOUNT ||
                type === DiscountType.DISCOUNT_PERCENT_ARTICLES ||
                type === DiscountType.DISCOUNT_AMOUNT_ARTICLES ||
                type === DiscountType.ADDITION_PERCENT ||
                type === DiscountType.ADDITION_AMOUNT ||
                type === DiscountType.ADDITION_PERCENT_ARTICLES ||
                type === DiscountType.ADDITION_AMOUNT_ARTICLES
            );
        });
    }

    public isDiscountOrSurChargeForOwnAllArticleByPercent(type: DiscountType) {
        return (
            type === DiscountType.DISCOUNT_PERCENT_ARTICLES ||
            type === DiscountType.ADDITION_PERCENT_ARTICLES
        );
    }

    public isDiscountOrSurChargeForOwnAllWorkByPercent(type: DiscountType) {
        return (
            type === DiscountType.DISCOUNT_PERCENT_WORK ||
            type === DiscountType.ADDITION_PERCENT_WORK
        );
    }

    private getDiscountTypesForOfferExceptWorkAndArticle(): Array<any> {
        return this.couponTypes.filter(item => {
            const type = Number(item.value) as DiscountType;
            return (
                type === DiscountType.DISCOUNT_PERCENT ||
                type === DiscountType.DISCOUNT_AMOUNT ||
                type === DiscountType.ADDITION_PERCENT ||
                type === DiscountType.ADDITION_AMOUNT
            );
        });
    }

    private convertToPercent(actionValue) {
        return PercentPipe.prototype.transform(actionValue, PERCENT_DIGITS, DEFAULT_LOCALE_ID);
    }

    public buildDiscountAmountWithSignAsString(item: OfferPosition): string {
        if (this.isDiscount(DiscountType[item.actionType])) {
            return '-'.concat(this.buildDiscountAmountAsString(item));
        }
        return this.buildDiscountAmountAsString(item);
    }

    private buildDiscountAmountAsString(item: OfferPosition) {
        let value = DEFAULT_AMOUNT_ST;
        if (this.isDiscountOrSurChargeByPercent(DiscountType[item.actionType])) {
            value = this.currencyPipe.transform(item.totalGrossPrice * item.actionValue);
        } else if (this.isDiscountOrSurChargeAsAmount(DiscountType[item.actionType])) {
            value = this.currencyPipe.transform(item.actionValue);
        }
        return value;
    }
}
