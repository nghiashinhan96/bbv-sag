import { Constant } from 'src/app/core/conts/app.constant';
import { OFFER_ITEM_TYPE } from '../enums/offers.enum';
import { LabourTime } from '../../dms/models/labour-time.model';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { CustomPrice } from 'sag-custom-pricing';

export class OfferPosition {
    id?: number;
    actionType?: string;
    actionValue?: number;
    articleDescription?: string;
    articleNumber?: string;
    calculated?: string;
    context?: string;
    createdDate?: string;
    createdUserId?: number;
    currencyId?: number;
    deliveryDate?: number;
    deliveryTypeId?: number;
    grossPrice?: number;
    // originalGrossPrice - additional field to keep default value of grossPrice
    originalGrossPrice?: number;
    makeId?: number;
    modelId?: number;
    modifiedDate?: string;
    modifiedUserId?: number;
    netPrice?: number;
    offerId?: number;
    quantity?: number;
    remark?: string;
    sequence?: number;
    shopArticle?: any;
    shopArticleId?: string;
    tecstate?: string;
    totalGrossPrice?: number;
    type?: string;
    umsartId?: string;
    uomIso?: string;
    vehicleBomDescription?: string;
    vehicleDescription?: string;
    vehicleId?: string;
    version?: number;
    connectVehicleId?: string;
    pimId?: string;
    awNumber?: string;
    index: number;
    displayedPrice: CustomPrice;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.actionType = data.actionType;
            this.actionValue = data.actionValue;
            this.articleDescription = data.articleDescription;
            this.articleNumber = data.articleNumber;
            this.calculated = data.calculated;
            this.context = data.context;
            this.createdDate = data.createdDate;
            this.createdUserId = data.createdUserId;
            this.currencyId = data.currencyId;
            this.deliveryDate = data.deliveryDate;
            this.deliveryTypeId = data.deliveryTypeId;
            this.grossPrice = data.grossPrice;
            this.originalGrossPrice = data.originalGrossPrice;
            this.makeId = data.makeId;
            this.modelId = data.modelId;
            this.modifiedDate = data.modifiedDate;
            this.modifiedUserId = data.modifiedUserId;
            this.netPrice = data.netPrice;
            this.offerId = data.offerId;
            this.quantity = data.quantity;
            this.remark = data.remark;
            this.sequence = data.sequence;
            this.shopArticle = data.shopArticle;
            this.shopArticleId = data.shopArticleId;
            this.tecstate = data.tecstate;
            this.totalGrossPrice = data.totalGrossPrice;
            this.type = data.type;
            this.umsartId = data.umsartId;
            this.uomIso = data.uomIso;
            this.vehicleBomDescription = data.vehicleBomDescription;
            this.vehicleDescription = data.vehicleDescription;
            this.vehicleId = data.vehicleId;
            this.version = data.version;
            this.connectVehicleId = data.connectVehicleId;
            this.pimId = data.pimId;
            this.awNumber = data.awNumber;
            this.index = data.index;
            this.displayedPrice = data.displayedPrice;
            this.basketItemSourceId = data.basketItemSourceId;
            this.basketItemSourceDesc = data.basketItemSourceDesc;
        }
    }

    static convertFromOfferOwnArticleOrWork(ownArticle: any): OfferPosition {
        const offerPosition = new OfferPosition();
        offerPosition.shopArticleId = ownArticle.id;
        offerPosition.quantity = ownArticle.amount;
        offerPosition.articleNumber = ownArticle.articleNumber;
        offerPosition.createdDate = ownArticle.createdDate;
        offerPosition.currencyId = ownArticle.currency;
        // own artcle and work dont show vehicle, category description
        offerPosition.vehicleDescription = null;
        offerPosition.vehicleBomDescription = null;
        offerPosition.modifiedDate = ownArticle.modifiedDate;
        offerPosition.modifiedUserId = ownArticle.modifiedUserId;
        offerPosition.articleDescription = ownArticle.name;
        offerPosition.grossPrice = ownArticle.price;
        offerPosition.tecstate = ownArticle.tecstate;
        offerPosition.version = ownArticle.version;
        offerPosition.totalGrossPrice = +offerPosition.quantity * +offerPosition.grossPrice;
        if (ownArticle.type === Constant.TYPE_ARTICLE) {
            offerPosition.type = OFFER_ITEM_TYPE.CLIENT_ARTICLE.toString();
        } else if (ownArticle.type === Constant.TYPE_WORK) {
            offerPosition.type = OFFER_ITEM_TYPE.CLIENT_WORK.toString();
        }
        offerPosition.displayedPrice = ownArticle.displayedPrice;
        return offerPosition;
    }

    static convertFromLabourTime(labourTime: LabourTime, vehicleId: string): OfferPosition {
        const offerPosition = new OfferPosition();
        offerPosition.awNumber = labourTime.awNumber;
        offerPosition.articleDescription = labourTime.name;
        offerPosition.quantity = labourTime.time;
        offerPosition.grossPrice = labourTime.labourRate;
        offerPosition.totalGrossPrice = offerPosition.quantity * offerPosition.grossPrice;
        offerPosition.type = OFFER_ITEM_TYPE.HAYNESPRO_PROVIDER_WORK.toString();
        offerPosition.connectVehicleId = vehicleId;
        return offerPosition;
    }

    static convertFromBasketItem(basketItem: ShoppingBasketItemModel, translateService: any): OfferPosition {
        const offerPosition = new OfferPosition();
        const article = basketItem.articleItem;
        offerPosition.umsartId = article.umsartId;
        offerPosition.pimId = article.pimId;
        offerPosition.articleNumber = article.artnrDisplay;
        offerPosition.articleDescription = basketItem.getProductText() ? translateService.instant(basketItem.getProductText()) : '';
        const vehicle = basketItem.vehicle;
        if (vehicle && vehicle.id) {
            offerPosition.connectVehicleId = vehicle.vehid;
            offerPosition.vehicleDescription = vehicle.vehicleInfo;
            offerPosition.makeId = vehicle.id_make;
            offerPosition.modelId = vehicle.id_model;
            offerPosition.type = OFFER_ITEM_TYPE.VENDOR_ARTICLE.toString();
            const productText = basketItem.getProductText();
            offerPosition.vehicleBomDescription = productText && translateService.instant(productText) || '';
            const criteria = article.criteria.filter(item => item.cid === '100');
            if (criteria && criteria.length > 0) {
                offerPosition.vehicleBomDescription += '/' + criteria[0].cvp;
            }
        } else {
            offerPosition.type = OFFER_ITEM_TYPE.VENDOR_ARTICLE_WITHOUT_VEHICLE.toString();
        }
        offerPosition.quantity = basketItem.quantity;
        offerPosition.grossPrice = basketItem.grossPrice;
        offerPosition.netPrice = basketItem.netPrice;
        offerPosition.totalGrossPrice = basketItem.totalGrossPrice;
        offerPosition.version = 1;
        offerPosition.displayedPrice = article.displayedPrice;
        offerPosition.basketItemSourceId = basketItem.basketItemSourceId;
        offerPosition.basketItemSourceDesc = basketItem.basketItemSourceDesc;
        return offerPosition;
    }
}

export class OfferPositionT extends OfferPosition {
    discountDescription: string = null;
    discountValue: string = null;
}
