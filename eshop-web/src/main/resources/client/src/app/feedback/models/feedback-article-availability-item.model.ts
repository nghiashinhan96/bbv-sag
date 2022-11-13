import { Constant } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackArticleAvailabilityItem {
    quantity: number;
    tourName: string;
    arrivalTime: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.quantity = data.quantity;
        this.tourName = data.tourName;
        this.arrivalTime = data.arrivalTime;
    }

    get content(): string {
        return `${this.quantity} - ${FeedbackUtils.getValue(this.tourName)} - ${FeedbackUtils.getValue(this.arrivalTime)}`;
    }
}
