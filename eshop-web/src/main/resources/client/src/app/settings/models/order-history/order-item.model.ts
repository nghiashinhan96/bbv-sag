import { OrderItemCategory } from './order-item-category.model';
import { OrderItemVehicle } from './order-item-vehicle.model';
import { ArticleModel } from 'sag-article-detail';
import { Constant } from 'src/app/core/conts/app.constant';

export class OrderItem {
    article: ArticleModel;
    vehicle: OrderItemVehicle;
    category: OrderItemCategory;
    itemDesc: string;
    recycle: boolean;
    depot: boolean;
    vin: boolean;
    recycleArticleId: string;
    depotArticleId: string;
    vocArticleId: string;
    vrgArticleId: string;
    additionalText: string;

    vehicleInfo: string;
    grossPrice: string;
    priceType: string;
    quantity: number;
    deliveryInformation: any;
    showPriceType?: boolean;
    sendMethodDesc?: string;

    constructor(data?: any, sendMethodDesc?) {
        if (data) {
            this.article = new ArticleModel(data.article);
            this.vehicle = new OrderItemVehicle(data.vehicle);
            this.category = new OrderItemCategory(data.category);
            this.itemDesc = data.itemDesc;
            this.recycle = data.recycle;
            this.depot = data.depot;
            this.vin = data.vin;
            this.recycleArticleId = data.recycleArticleId;
            this.depotArticleId = data.depotArticleId;
            this.vocArticleId = data.vocArticleId;
            this.vrgArticleId = data.vrgArticleId;

            this.additionalText = data.additionalText;
            this.vehicleInfo = this.vehicle && this.vehicle.vehicleInfo;
            this.grossPrice = this.article && this.article.price && this.article.price.price.grossPrice || 0;
            if (this.article && this.article.displayedPrice) {
                this.priceType = `${this.article.displayedPrice.type} ${this.article.displayedPrice.brand}`;
            } else {
                if (this.article.price && this.article.price.price
                    && this.article.price.price.type && this.article.price.price.type !== Constant.GROSS) {
                    this.priceType = this.article.price.price.type;
                }
            }

            this.quantity = this.article && this.article.amountNumber;
            this.sendMethodDesc = sendMethodDesc;
        }
    }
}