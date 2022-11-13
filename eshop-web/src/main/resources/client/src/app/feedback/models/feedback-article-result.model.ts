import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackArticleAvailability } from './feedback-article-availability.model';
import { FeedbackArticleAvailabilityItem } from './feedback-article-availability-item.model';

export class FeedbackArticleResult {
    articleId: string;
    articleNr: string;
    articleName: string;
    brand: string;
    oepPrice: string;
    uvpePrice: string;
    netPrice: string;
    availability: FeedbackArticleAvailability;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.articleId = data.articleId;
        this.articleNr = data.articleNr;
        this.articleName = data.articleName;
        this.brand = data.brand;
        this.oepPrice = data.oepPrice;
        this.uvpePrice = data.uvpePrice;
        this.netPrice = data.netPrice;

        if (!data.availability || !data.availability.avaiItems) {
            return;
        }

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

    get content(): string {
        return [
            this.articleNr,
            this.articleName,
            this.brand,
            this.availability ? this.availability.content : NOT_AVAILABLE,
            this.oepPrice,
            this.uvpePrice,
            this.netPrice
        ].join('; ');
    }
}
