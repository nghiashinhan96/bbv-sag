import { AffiliateUtil } from 'sag-common';
import { ArticleModel, LibUserSetting, AvailabilityUtil } from 'sag-article-detail';
import { environment } from 'src/environments/environment';
import { ArticleUtil } from '../utils/article.util';

const isEhCh = AffiliateUtil.isEhCh(environment.affiliate)
const isEhCz = AffiliateUtil.isEhCz(environment.affiliate)
export class ShoppingBasketItemModel {
    quantity: number;
    articleItem: ArticleModel;
    additionalTextDoc: string;
    depotArticleId: string;
    recycleArticleId: string;
    vehicle: {
        id: string,
        id_make: number,
        id_model: number,
        vehid: string,
        vehicleInfo: string
    }; // VehicleDoc;
    totalGrossPrice: number;
    totalNetPriceWithVat: number;
    totalGrossPriceWithVat: number;
    netPrice: number;
    netPriceWithVat: number;
    grossPrice: number;
    grossPriceWithVat: number;
    totalNetPrice: number;
    discountPriceWithVat: number;
    vehicleId: string;
    vehicleInfo: string;
    info: any;
    cartKey: string;
    recycle: boolean;
    depot: boolean;
    pfand: boolean;
    voc: boolean;
    vrg: boolean;
    vin: boolean;
    attachedCartItem: boolean;
    amountNumber: number;
    attachedCartItems: ShoppingBasketItemModel[];
    itemType: string;
    addedTime: string;
    curGenArtDescription: string;
    itemDesc: string;
    refText: string;
    isUpdatedAvail: boolean;
    productTextWithArt?: string;
    productTextWithoutArt?: string;
    source: string;

    totalFinalCustomerNetPrice: number;
    totalFinalCustomerNetPriceWithVat: number;
    finalCustomerNetPrice: number;
    finalCustomerNetPriceWithVat: number;
    displayFCNetPrice: number;

    net1Price: number;
    net1PriceWithVat: number;
    totalNet1Price: number;
    totalNet1PriceWithVat: number;
    discountInPercent: number;
    discountPrice: number;
    promotionInPercent: number;
    net1PriceFound: boolean;
    standardGrossPrice: number;
    standardGrossPriceWithVat: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(data: ShoppingBasketItemModel | any) {
        if (data) {
            this.quantity = data.quantity;

            this.net1Price = data.net1Price || 0;
            this.net1PriceWithVat = data.net1PriceWithVat || 0;
            this.totalNet1Price = data.totalNet1Price || 0;
            this.totalNet1PriceWithVat = data.totalNet1PriceWithVat || 0;
            this.discountInPercent = data.discountInPercent;
            this.discountPrice = data.discountPrice;
            this.promotionInPercent = data.promotionInPercent;
            this.net1PriceFound = data.net1PriceFound;
            this.standardGrossPrice = data.standardGrossPrice || 0;
            this.standardGrossPriceWithVat = data.standardGrossPriceWithVat || 0;

            this.articleItem = this.getArticleItem(data);
            this.additionalTextDoc = data.additionalTextDoc;
            this.depotArticleId = data.depotArticleId;
            this.recycleArticleId = data.recycleArticleId;
            this.vehicle = data.vehicle || {};
            this.grossPrice = data.grossPrice;
            this.grossPriceWithVat = data.grossPriceWithVat;
            this.totalGrossPrice = data.totalGrossPrice;
            this.totalGrossPriceWithVat = data.totalGrossPriceWithVat;
            this.netPrice = data.netPrice || 0;
            this.netPriceWithVat = data.netPriceWithVat || 0;
            this.totalNetPrice = data.totalNetPrice;
            this.totalNetPriceWithVat = data.totalNetPriceWithVat;
            this.discountPriceWithVat = data.discountPriceWithVat;
            this.vehicleId = data.vehicleId;
            this.vehicleInfo = data.vehicleInfo;
            this.info = data.info;
            this.cartKey = data.cartKey;
            this.recycle = data.recycle;
            this.depot = data.depot;
            this.pfand = data.pfand;
            this.voc = data.voc;
            this.vrg = data.vrg;
            this.vin = data.vin;
            this.attachedCartItem = data.attachedCartItem;
            this.amountNumber = data.amountNumber;
            this.addedTime = data.addedTime;
            this.attachedCartItems = (data.attachedCartItems || []).map(attachedCartItem => new ShoppingBasketItemModel(attachedCartItem));
            this.curGenArtDescription = data.curGenArtDescription;
            this.itemType = data.itemType;
            this.itemDesc = data.itemDesc;
            this.refText = data.refText;
            this.isUpdatedAvail = data.isUpdatedAvail;
            this.productTextWithArt = this.getProductText();
            this.productTextWithoutArt = this.getProductText(false);
            this.articleItem.itemType = this.itemType;
            this.source = data.source || '';

            this.finalCustomerNetPrice = data.finalCustomerNetPrice;
            this.finalCustomerNetPriceWithVat = data.finalCustomerNetPriceWithVat;
            this.totalFinalCustomerNetPrice = data.totalFinalCustomerNetPrice;
            this.totalFinalCustomerNetPriceWithVat = data.totalFinalCustomerNetPriceWithVat;
            this.basketItemSourceId = data.basketItemSourceId;
            this.basketItemSourceDesc = data.basketItemSourceDesc;
        }
    }

    getArticleItem(data): ArticleModel {
        const priceObj = data.articleItem && data.articleItem.price || {};
        const articleItem = new ArticleModel({
            ...data.articleItem,
            finalCustomerNetPriceWithVat: data.finalCustomerNetPriceWithVat,
            totalFinalCustomerNetPriceWithVat: data.totalFinalCustomerNetPriceWithVat,
            price: {
                ...priceObj,
                price: {
                    ...priceObj.price,
                    netPriceWithVat: data.netPriceWithVat,
                    grossPriceWithVat: data.grossPriceWithVat,
                    totalGrossPriceWithVat: data.totalGrossPriceWithVat,
                    totalNetPriceWithVat: data.totalNetPriceWithVat,
                    discountPriceWithVat: data.discountPriceWithVat,
                    totalGrossPrice: data.totalGrossPrice,
                    totalNetPrice: data.totalNetPrice
                }
            }
        });

        return articleItem;
    }

    getProductText(isShownArtText = true) {
        if (this.vin) {
            return this.itemDesc;
        }
        const data = {
            depot: this.depot,
            pfand: this.pfand,
            recycle: this.recycle,
            voc: this.voc,
            vrg: this.vrg
        };
        return ArticleUtil.getProductText(this.articleItem, data, isShownArtText);
    }

    getPrice(userPrice: LibUserSetting) {
        if (isEhCh || isEhCz) {
            if (userPrice.fcUserCanViewNetPrice) {
                if (userPrice.currentStateVatConfirm) {
                    return this.totalFinalCustomerNetPriceWithVat;
                } else {
                    return this.totalFinalCustomerNetPrice;
                }
            } else {
                if (userPrice.currentStateVatConfirm) {
                    return this.totalGrossPriceWithVat;
                } else {
                    return this.totalGrossPrice;
                }
            }
        }

        if (userPrice.currentStateNetPriceView && userPrice.currentStateVatConfirm) {
            return this.totalNetPriceWithVat;
        }
        if (userPrice.currentStateNetPriceView) {
            return this.totalNetPrice;
        }
        if (userPrice.currentStateVatConfirm) {
            return this.totalGrossPriceWithVat;
        }
        return this.totalGrossPrice;
    }

    checkDeliverable(availPermission: boolean, affiliateCode: string) {
        if(availPermission) {
            return AffiliateUtil.isEhCz(affiliateCode) ? AvailabilityUtil.isArticle24hCz(this.articleItem && this.articleItem.availabilities || []) : AvailabilityUtil.isArticle24h(this.articleItem && this.articleItem.availabilities || []);
        }
        if (availPermission && AffiliateUtil.isEhCz(affiliateCode)
            && AvailabilityUtil.isArticle24hCz(this.articleItem && this.articleItem.availabilities || [])) {
            return true;
        }

        return false;
    }

    getNet1PriceFound () {
        if(this.articleItem) {
            if (!this.articleItem.price || !this.articleItem.price.price) {
                return null;
            }

            let { net1PriceFound } = this.articleItem.price.price;

            return net1PriceFound || false;
        }

        return this.net1PriceFound;
    }

    getBrutto(currentStateVatConfirm = false) {
        if(this.articleItem) {
            if (!this.articleItem.price || !this.articleItem.price.price) {
                return null;
            }

            let { standardGrossPrice, standardGrossPriceWithVat } = this.articleItem.price.price;

            return currentStateVatConfirm ? (standardGrossPriceWithVat || 0) : (standardGrossPrice || 0);
        }

        return currentStateVatConfirm ? this.standardGrossPriceWithVat : this.standardGrossPrice;
    }

    getGrossPrice(currentStateVatConfirm = false) {
        return currentStateVatConfirm ? this.grossPriceWithVat : this.grossPrice;
    }

    getNetPrice(currentStateVatConfirm = false) {
        return currentStateVatConfirm ? this.netPriceWithVat : this.netPrice;
    }

    getArticlePrice(currentStateNetPriceView, currentStateVatConfirm = false) {
        if (currentStateNetPriceView && currentStateVatConfirm) {
            return this.totalNetPriceWithVat;
        }
        if (currentStateNetPriceView) {
            return this.totalNetPrice;
        }
        if (currentStateVatConfirm) {
            return this.totalGrossPriceWithVat;
        }
        return this.totalGrossPrice;
    }

    setFCNetPrice(priceSetting: LibUserSetting, currentStateVatConfirm) {
        if (!priceSetting) {
            return;
        }
        if (priceSetting.fcUserCanViewNetPrice) {
            if (currentStateVatConfirm) {
                this.displayFCNetPrice = this.finalCustomerNetPriceWithVat;
                return;
            }
            this.displayFCNetPrice = this.finalCustomerNetPrice;
        } else {
            this.displayFCNetPrice = currentStateVatConfirm ? this.netPriceWithVat : this.netPrice;
        }
    }

    getVehicleId() {
        return this.vehicle && this.vehicle.id || '';
    }

    getDiscount() {
        if(this.articleItem) {
            if (!this.articleItem.price || !this.articleItem.price.price) {
                return null;
            }

            let { discountInPercent } = this.articleItem.price.price;

            return discountInPercent;
        }

        return this.discountInPercent;
    }

    getNet1Price(inclVAT = false) {
        return inclVAT ? this.net1PriceWithVat : this.net1Price;
    }

    getTotalNet1Price(inclVAT = false) {
        return inclVAT ? this.totalNet1PriceWithVat : this.totalNet1Price;
    }

    getPromotionDiscount () {
        if(this.articleItem) {
            if (!this.articleItem.price || !this.articleItem.price.price) {
                return null;
            }

            let { promotionInPercent } = this.articleItem.price.price;

            return promotionInPercent;
        }

        return this.promotionInPercent;
    }

    netPriceGreaterThanOrEqualNet1Price() {
        if (this.getNetPrice() >= this.getNet1Price()) {
            return true;
        } else {
            return false;
        }
    }

    netPriceLessThanNet1Price() {
        if (this.getNetPrice() < this.getNet1Price()) {
            return true;
        } else {
            return false;
        }
    }
}
