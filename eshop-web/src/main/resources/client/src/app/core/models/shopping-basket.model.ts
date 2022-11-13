import { ShoppingBasketItemModel } from './shopping-basket-item.model';

import { OfferPosition } from 'src/app/offers/models/offer-position.model';
import { ArticleSortUtil, ARTICLE_TYPE } from 'sag-article-list';
import { LibUserSetting, AvailabilityUtil, ArticleAvailabilityModel } from 'sag-article-detail';
import { CurrencyUtil } from 'sag-currency';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

const isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
const isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
const isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
export class ShoppingBasketInfo {
    lastestAvail: ArticleAvailabilityModel;
    has24HoursArticle: boolean;
    isAllSofort: boolean;
    isAllVailAvail: boolean;
    isVinOnly: boolean;
}
export class ShoppingBasketModel {
    items: ShoppingBasketItemModel[] = [];
    couponCode: string;
    discount: number;
    grossTotalExclVat: number;
    netTotalExclVat: number;
    newTotal: number;
    newTotalWithNetAndVat: number;
    newTotalWithVat: number;
    numberOfItems: number;
    numberOfOrderPos: number = 0;
    subTotal: number;
    subTotalWithNet: number;
    subTotalWithNetAndVat: number;
    subTotalWithVat: number;
    totalWithDiscount: number;
    vatTotal: number;
    vatTotalWithNet: number;
    vatValue: number;
    currentStateNetPriceView?: boolean;

    finalCustomerNetPrice?: number;
    totalFinalCustomerNetPrice?: number;
    finalCustomerNetTotalExclVat?: number;
    vatTotalWithFinalCustomerNet?: number;
    subTotalWithFinalCustomerNetAndVat?: number;    

    constructor(data?: ShoppingBasketModel | any) {
        if (data) {
            this.items = (data.items || []).map(item => new ShoppingBasketItemModel(item));
            this.items.sort((item1, item2) => {
                if (item1.addedTime < item2.addedTime) { return 1; }
                if (item1.addedTime > item2.addedTime) { return -1; }
                return 0;
            });
            this.couponCode = data.couponCode;
            this.discount = data.discount;
            this.grossTotalExclVat = data.grossTotalExclVat;
            this.netTotalExclVat = data.netTotalExclVat;
            this.newTotal = data.newTotal;
            this.newTotalWithNetAndVat = data.newTotalWithNetAndVat;
            this.newTotalWithVat = data.newTotalWithVat;
            this.numberOfItems = data.numberOfItems;
            this.numberOfOrderPos = data.numberOfOrderPos;
            this.subTotal = data.subTotal;
            this.subTotalWithNet = data.subTotalWithNet;
            this.subTotalWithNetAndVat = data.subTotalWithNetAndVat;
            this.subTotalWithVat = data.subTotalWithVat;
            this.totalWithDiscount = data.totalWithDiscount;
            this.vatTotal = data.vatTotal;
            this.vatTotalWithNet = data.vatTotalWithNet;
            this.vatValue = data.vatValue;
            this.finalCustomerNetPrice = data.finalCustomerNetPrice;
            this.totalFinalCustomerNetPrice = data.totalFinalCustomerNetPrice;
            this.finalCustomerNetTotalExclVat = data.finalCustomerNetTotalExclVat;
            this.vatTotalWithFinalCustomerNet = data.vatTotalWithFinalCustomerNet;
            this.subTotalWithFinalCustomerNetAndVat = data.subTotalWithFinalCustomerNetAndVat;
        }
    }

    vehicleGroup(shownNonVehicleGroupOnly?: boolean) {
        const filtered = (this.items || []).filter(item => item.itemType !== ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]);
        const group = ArticleSortUtil.groupBy(filtered,
            (item: ShoppingBasketItemModel) => [item.vehicleInfo]) as ShoppingBasketItemModel[][];
        return group.map(g => {
            g.sort((item1, item2) => {
                if (item1.addedTime < item2.addedTime) { return 1; }
                if (item1.addedTime > item2.addedTime) { return -1; }
                return 0;
            });
            const key = g[0].vehicleInfo;
            return {
                key,
                value: g,
                timestamp: new Date().getTime(),
                hidden: shownNonVehicleGroupOnly && !!key,
                vehicle: g[0].vehicle,
                vehicleId: g[0].vehicleId,
                gatxtdech: g[0].curGenArtDescription
            };
        }).sort((g1, g2) => {
            return g1.key < g2.key ? 1 : -1;
        });
    }

    devseGroup() {
        const filtered = (this.items || []).filter(item => item.itemType === ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]);
        const group = ArticleSortUtil.groupBy(filtered,
            (item: ShoppingBasketItemModel) => [item.vehicleInfo, item.curGenArtDescription]) as ShoppingBasketItemModel[][];
        return group.map(g => {
            return {
                key: g[0].vehicleInfo,
                value: g
            };
        }).sort((g1, g2) => {
            return g1.key < g2.key ? 1 : -1;
        });
    }

    getSummary(options): ShoppingBasketInfo {
        const filtered = (this.items || []).filter(item => item.itemType !== ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]);
        return {
            lastestAvail: this.getLastestAvail(filtered, options && options.defaultArrivalTime),
            has24HoursArticle: this.has24HoursArticle(filtered),
            isAllSofort: this.isAllSofort(filtered),
            isAllVailAvail: this.isAllVailAvail(),
            isVinOnly: this.isVinOnly()
        };
    }

    initSubTotal(priceSetting: LibUserSetting, currentStateVatConfirm = false) {
        if (isFinalCustomer) {
            if (priceSetting.fcUserCanViewNetPrice) {
                if (currentStateVatConfirm) {
                    return this.subTotalWithFinalCustomerNetAndVat;
                }
                return this.totalFinalCustomerNetPrice;
            } else {
                if (currentStateVatConfirm) {
                    return this.subTotalWithVat;
                }
                return this.subTotal;
            }
        }

        if (priceSetting.currentStateNetPriceView && currentStateVatConfirm) {
            return this.subTotalWithNetAndVat;
        }
        if (priceSetting.currentStateNetPriceView) {
            return this.subTotalWithNet;
        }
        if (currentStateVatConfirm) {
            return this.subTotalWithVat;
        }

        return this.subTotal;
    }

    initVatTotal(priceSetting: LibUserSetting) {
        if (isFinalCustomer) {
            if (priceSetting.fcUserCanViewNetPrice && priceSetting.currentStateNetPriceView) {
                return this.vatTotalWithFinalCustomerNet;
            } else {
                return this.vatTotal;
            }
        }

        if (priceSetting.currentStateNetPriceView) {
            return this.vatTotalWithNet;
        }
        return this.vatTotal;
    }

    initTotalExclVat(priceSetting: LibUserSetting) {
        if (isFinalCustomer) {
            if (priceSetting.fcUserCanViewNetPrice) {
                return this.finalCustomerNetTotalExclVat;
            } else {
                return this.grossTotalExclVat;
            }
        }

        if (priceSetting.currentStateNetPriceView) {
            return this.netTotalExclVat;
        }
        return this.grossTotalExclVat;
    }

    initTotalInclVat(priceSetting: LibUserSetting) {
        if (isFinalCustomer) {
            if (priceSetting.fcUserCanViewNetPrice && priceSetting.currentStateNetPriceView) {
                return CurrencyUtil.roundHalfEvenTo2digits(this.subTotalWithFinalCustomerNetAndVat);
            } else {
                return CurrencyUtil.roundHalfEvenTo2digits(this.newTotalWithVat);
            }
        }

        if (priceSetting.currentStateNetPriceView) {
            return CurrencyUtil.roundHalfEvenTo2digits(this.newTotalWithNetAndVat);
        }
        return CurrencyUtil.roundHalfEvenTo2digits(this.newTotalWithVat);
    }

    isKSOAUTOrder(): boolean {
        return (this.items || []).some(item => {
            const availabilities = item && item.articleItem && item.articleItem.availabilities || [];
            return availabilities.some(avail => !!avail.venExternalSource);
        });
    }

    isAllVailAvail() {
        const filtered = (this.items || []).filter(item => item.itemType !== ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]);

        return !(filtered || [])
            .some(item => {
                if (item.vin) {
                    return false;
                }
                const availabilities = item && item.articleItem && item.articleItem.availabilities || [];
                return availabilities.length === 0 || availabilities.find(avail => (avail.backOrder || avail.conExternalSource || avail.location && avail.location.allInPrioLocations));
            });
    }

    isVinOnly() {
        const filtered = this.items;

        return !(filtered || [])
            .some(item => {
                if (!item.vin) {
                    return true;
                }
            });
    }

    getArticlesNoReferencedMessage(item: ShoppingBasketItemModel, text = '') {
        if (!item.articleItem) {
            return '';
        }

        const articleNr = this.getArticleNr(item);
        const space = articleNr ? "  " : "";

        return `${item.articleItem.supplier}${space}${articleNr}  ${item.quantity}  ${text}`;
    }

    private getArticleNr(item: ShoppingBasketItemModel) {
        return item.articleItem.artnrDisplay || '';
    }

    private has24HoursArticle(items: ShoppingBasketItemModel[]) {
        // Ignore all Vin item when check availability
        const itemList = items.filter(item => !item.vin);
        if (isEhCz) {
            return (itemList || []).some(item => AvailabilityUtil.isArticle24hCz(item.articleItem && item.articleItem.availabilities));
        }
        return (itemList || [])
            .some(item => AvailabilityUtil.isArticle24h(item.articleItem && item.articleItem.availabilities));
    }

    private isAllSofort(items: ShoppingBasketItemModel[]) {
        return !(items || [])
            .some(item => {
                const availabilities = item && item.articleItem && item.articleItem.availabilities || [];
                return availabilities.length === 0 || !availabilities.find(avail => avail.sofort);
            });
    }

    private getLastestAvail(items: ShoppingBasketItemModel[], defaultArrivalTime: any) {
        return (items || [])
            .map(item => !item.vin && item.articleItem && item.articleItem.availabilities || [])
            .reduce((result: ArticleAvailabilityModel, avails) => {
                return (avails || []).reduce((lastest: ArticleAvailabilityModel, avail) => {
                    if (defaultArrivalTime === undefined) {
                        defaultArrivalTime = lastest && lastest.arrivalTime;
                    }
                    if (!lastest || new Date(lastest.arrivalTime || defaultArrivalTime).getTime() < new Date(avail.arrivalTime).getTime()) {
                        return avail;
                    }
                    return lastest;
                }, result);
            }, null);
    }

    getOfferPostions(translateService: any) {
        const offerPostions = [];
        this.items.forEach(item => {
            offerPostions.push(OfferPosition.convertFromBasketItem(item, translateService));
            if (item.attachedCartItems && item.attachedCartItems.length > 0) {
                item.attachedCartItems.forEach(attachedItem => {
                    offerPostions.push(OfferPosition.convertFromBasketItem(attachedItem, translateService));
                });
            }
        });
        return offerPostions;
    }

    compareOrderBasket(currentItems: Array<any> = [], ignoreDisplayedPrice = false): boolean {
        return this.isOrderSameOrigin(currentItems, ignoreDisplayedPrice);
    }

    private isOrderSameOrigin(currentItems: Array<any> = [], ignoreDisplayedPrice = false) {
        if (this.items.length !== currentItems.length) {
            return false;
        }
        return currentItems.every(i => {
            return this.isItemSameWithOrigin(i, ignoreDisplayedPrice);
        });
    }

    private isItemSameWithOrigin(currentItem: any, ignoreDisplayedPrice = false) {
        return this.items.some(i => {
            return this.isEqualItem(i, currentItem, ignoreDisplayedPrice);
        });
    }

    private isEqualItem(originalItem: any, currentItem: any, ignoreDisplayedPrice = false) {
        return originalItem.articleItem.artid === currentItem.articleItem.artid
            && originalItem.quantity === currentItem.quantity
            && originalItem.vehicleId === currentItem.vehicleId
            && this.isEqualDisplayedPrice(originalItem.articleItem.displayedPrice, currentItem.articleItem.displayedPrice,
                ignoreDisplayedPrice);
    }

    private isEqualDisplayedPrice(originPrice, currentPrice, ignoreDisplayedPrice = false): boolean {
        if (ignoreDisplayedPrice) {
            return true;
        }
        if (!originPrice && !currentPrice) {
            return true;
        }

        if (!originPrice || !currentPrice) {
            return false;
        }

        return (originPrice.type === currentPrice.type
            && originPrice.brand === currentPrice.brand
            && originPrice.brandId === currentPrice.brandId);
    }
}
