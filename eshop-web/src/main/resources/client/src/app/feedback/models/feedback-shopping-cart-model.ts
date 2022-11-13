import { FeedbackArticleAvailability } from './feedback-article-availability.model';
import { FeedbackArticleAvailabilityItem } from './feedback-article-availability-item.model';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackShoppingCartItem {
    vehicleId: string;
    vehicleDesciption: string;
    articleId: string;
    articleNr: string;
    brand: string;
    quantity: string;
    grossPrice: string;
    netPrice: string;
    addedTime: string;
    availability: FeedbackArticleAvailability;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.vehicleId = data.vehicleId;
        this.vehicleDesciption = data.vehicleDesciption;
        this.articleId = data.articleId;
        this.articleNr = data.articleNr;
        this.brand = data.brand;
        this.quantity = data.quantity;
        this.grossPrice = data.grossPrice;
        this.netPrice = data.netPrice;
        this.addedTime = data.addedTime;

        if (data.availability) {
            const availability = data.availability;
            let avaiItems = [];
            if (availability.avaiItems && availability.avaiItems.length) {
                avaiItems = availability.avaiItems.map(item =>
                    new FeedbackArticleAvailabilityItem({
                        quantity: item.quantity,
                        arrivalTime: item.arrivalTime,
                        tourName: item.tourName
                    })
                );
            }
            this.availability =
                new FeedbackArticleAvailability({
                    articleId: availability.articleId,
                    avaiItems
                });
        }
    }

    get vehicleIdValue() {
        return FeedbackUtils.getValue(this.vehicleId);
    }

    get vehicleDesciptionValue() {
        return FeedbackUtils.getValue(this.vehicleDesciption);
    }

    get articleIdValue() {
        return FeedbackUtils.getValue(this.articleId);
    }

    get articleNrValue() {
        return FeedbackUtils.getValue(this.articleNr);
    }

    get brandValue() {
        return FeedbackUtils.getValue(this.brand);
    }

    get quantityValue() {
        return FeedbackUtils.getValue(this.quantity);
    }

    get grossPriceValue() {
        return FeedbackUtils.getValue(this.grossPrice);
    }

    get netPriceValue() {
        return FeedbackUtils.getValue(this.netPrice);
    }

    get addedTimeValue() {
        return FeedbackUtils.getValue(this.addedTime);
    }

    get availabilityValue() {
        return this.availability ? this.availability.content : NOT_AVAILABLE;
    }

    get content(): string {
        return [
            this.vehicleIdValue,
            this.vehicleDesciptionValue,
            this.articleIdValue,
            this.articleNrValue,
            this.brandValue,
            this.quantityValue,
            this.availabilityValue,
            this.grossPriceValue,
            this.netPriceValue,
            this.addedTimeValue
        ].join('; ');
    }
}
