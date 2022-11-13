import { NONVEHICLE } from 'src/app/core/conts/app.constant';

export class OrderBasketItemEvent {
    articleNr: string;
    pimId: string;
    genarttxt: string;
    productBrand: string;
    sagProductGroup: string;
    sagProductGroup2: string;
    sagProductGroup3: string;
    sagProductGroup4: string;
    quantity: number;
    vehicleId: string;
    brutto: number;
    netto: number;
    total: number;
    basketItemPriceType: 'UVPE' | 'OEP';
    basketItemPriceVehBrand: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(data: any) {
        if (data) {
            this.articleNr = data.articleItem.artnr;
            this.pimId = data.articleItem.pimId;
            this.genarttxt = data.articleItem.genArtTxts && data.articleItem.genArtTxts.length ?
                data.articleItem.genArtTxts[0].gatxtdech : '';
            this.productBrand = data.articleItem.idProductBrand;
            this.sagProductGroup = data.articleItem.sagProductGroup;
            this.sagProductGroup2 = data.articleItem.sagProductGroup2;
            this.sagProductGroup3 = data.articleItem.sagProductGroup3;
            this.sagProductGroup4 = data.articleItem.sagProductGroup4;
            this.quantity = data.quantity || data.amountNumber;
            this.brutto = (data.price && data.price.price && data.price.price.grossPrice) || data.grossPrice;
            this.netto = (data.price && data.price.price && data.price.price.netPrice) || data.netPrice;
            this.vehicleId = (data.vehicle && data.vehicle.vehid && data.vehicle.vehid !== NONVEHICLE) ? data.vehicle.vehid : '';
            if (data.articleItem.displayedPrice) {
                this.basketItemPriceType = data.articleItem.displayedPrice.type;
                this.basketItemPriceVehBrand = data.articleItem.displayedPrice.brand;
            } else {
                const priceObj = data.articleItem.price && data.articleItem.price.price;
                this.basketItemPriceType = priceObj && priceObj.type;
                this.basketItemPriceVehBrand = data.vehicle && data.vehicle.vehicle_brand;
            }
            if (data.basketItemSourceId && data.basketItemSourceDesc) {
                this.basketItemSourceId = data.basketItemSourceId;
                this.basketItemSourceDesc = data.basketItemSourceDesc;
            }
        }
    }

    buildTotalPriceEventData(isNettoPriceDisplay: boolean) {
        if (isNettoPriceDisplay) {
            this.total = this.netto * this.quantity;
        } else {
            this.total = this.brutto * this.quantity;
        }
        return this;
    }

    toEventRequest() {
        return {
            ArticleNr: this.articleNr,
            Id_pim: this.pimId,
            Genarttxt: this.genarttxt,
            Brand: this.productBrand,
            SAG_product_group: this.sagProductGroup,
            SAG_product_group_2: this.sagProductGroup2,
            SAG_product_group_3: this.sagProductGroup3,
            SAG_product_group_4: this.sagProductGroup4,
            quantity: this.quantity,
            vehicle_id: this.vehicleId,
            brutto: this.brutto,
            netto: this.netto,
            total: this.total,
            basket_item_price_type: this.basketItemPriceType,
            basket_item_price_veh_brand: this.basketItemPriceVehBrand,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
