import { NONVEHICLE } from 'src/app/core/conts/app.constant';
import { AvailabilityUtil } from 'sag-article-detail';
import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';
import { get } from 'lodash';
import { BasketItemSource } from './basket-item-source.model';

export class ShoppingBasketItemEvent {
    basketItemArticleId: string;
    basketItemArticleName: string;
    basketItemQuantity: number;
    basketItemAvailabilityStatus?: string;
    basketItemAvailabilityDateTime?: string[] = [];
    basketItemNettoPriceDisplayed: boolean;
    basketItemBruttoPrice: number;
    basketItemNettoPrice: number;
    basketItemTotalPrice: number;
    basketItemVehcileId: string;
    basketItemPriceType: string;
    basketItemPriceVehBrand: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(data: any, isNettoPriceDisplay?: boolean, customBrand?: any, basketItemSource?: BasketItemSource) {
        const isCz = AffiliateUtil.isCz(environment.affiliate);
        if (data) {
            if (data.articleItem) {
                this.basketItemArticleId = data.articleItem.artid;
                this.basketItemArticleName = data.articleItem.name ? data.articleItem.name : [];
                if (isCz) {
                    this.basketItemAvailabilityStatus = `${get(data, 'articleItem.availabilities[0].availState')}`;
                } else {
                    this.basketItemAvailabilityStatus = AvailabilityUtil.getAvailInText(data.articleItem.availabilities);
                }
                this.basketItemAvailabilityDateTime = data.articleItem.availabilities ? data
                    .articleItem.availabilities.map(avail => {
                        if (avail && (avail.formattedCETArrivalDate && avail.formattedCETArrivalTime)) {
                            return `${avail.formattedCETArrivalDate} ${avail.formattedCETArrivalTime.replace('.', ':')}`;
                        }
                    }) : [];
                if (data.articleItem.displayedPrice) {
                    this.basketItemPriceType = data.articleItem.displayedPrice.type;
                    this.basketItemPriceVehBrand = data.articleItem.displayedPrice.brand;
                    this.basketItemBruttoPrice = data.articleItem.displayedPrice.price;
                } else {
                    const priceObj = data.articleItem.price && data.articleItem.price.price;
                    this.basketItemPriceType = priceObj && priceObj.type;
                    this.basketItemPriceVehBrand = data.vehicle && data.vehicle.vehicle_brand;
                    this.basketItemBruttoPrice = priceObj && priceObj.grossPrice || data.grossPrice;
                }
            } else {
                this.basketItemArticleId = data.pimId || data.artid;
                this.basketItemArticleName = data.articleDescription || data.name;
                if (isCz) {
                    this.basketItemAvailabilityStatus = `${get(data, 'availabilities[0].availState')}`;
                } else {
                    this.basketItemAvailabilityStatus = AvailabilityUtil.getAvailInText(data.availabilities || '');
                }
                this.basketItemAvailabilityDateTime = data.availabilities ?
                    data.availabilities.map(avail => {
                        if (avail && (avail.formattedCETArrivalDate && avail.formattedCETArrivalTime)) {
                            return `${avail.formattedCETArrivalDate} ${avail.formattedCETArrivalTime.replace('.', ':')}`;
                        }
                    }) : [];
                if (data.displayedPrice) {
                    this.basketItemPriceType = data.displayedPrice.type;
                    this.basketItemPriceVehBrand = data.displayedPrice.brand;
                    this.basketItemBruttoPrice = data.displayedPrice.price;
                } else {
                    const priceObj = data.price && data.price.price;
                    this.basketItemPriceType = priceObj && priceObj.type;
                    this.basketItemPriceVehBrand =  customBrand && customBrand.brand;
                    this.basketItemBruttoPrice = priceObj && priceObj.grossPrice || data.grossPrice;
                }
            }
            this.basketItemQuantity = data.quantity || data.amountNumber;
            this.basketItemNettoPriceDisplayed = isNettoPriceDisplay;
            this.basketItemNettoPrice = (data.price && data.price.price && data.price.price.netPrice) || data.netPrice;
            this.basketItemTotalPrice = this.getTotalPrice(this.basketItemNettoPriceDisplayed);
            this.basketItemVehcileId =
                (data.vehicle && data.vehicle.vehid && data.vehicle.vehid !== NONVEHICLE) ? data.vehicle.vehid : '';
            if (data.basketItemSourceId && data.basketItemSourceDesc) {
                this.basketItemSourceId = data.basketItemSourceId;
                this.basketItemSourceDesc = data.basketItemSourceDesc;
            } else if (basketItemSource) {
                this.basketItemSourceId = basketItemSource.basketItemSourceId;
                this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
            }
        }
    }

    getTotalPrice(isNetPrice: boolean) {
        if (isNetPrice) {
            return this.basketItemNettoPrice * this.basketItemQuantity;
        } else {
            return this.basketItemBruttoPrice * this.basketItemQuantity;
        }
    }

    toEventRequest() {
        return {
            basket_item_article_id: this.basketItemArticleId,
            basket_item_article_name: this.basketItemArticleName,
            basket_item_quantity: this.basketItemQuantity,
            basket_item_availability_status: (this.basketItemAvailabilityStatus || '').toLocaleLowerCase(),
            basket_item_availability_date_time: this.basketItemAvailabilityDateTime,
            basket_item_netto_price_displayed: this.basketItemNettoPriceDisplayed || false,
            basket_item_brutto_price: this.basketItemBruttoPrice,
            basket_item_netto_price: this.basketItemNettoPrice,
            basket_item_total_price: (+this.basketItemTotalPrice || 0).toFixed(2),
            basket_item_vehcile_id: this.basketItemVehcileId,
            basket_item_price_type: this.basketItemPriceType,
            basket_item_price_veh_brand: this.basketItemPriceVehBrand,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
