import { FeedbackDataItem } from './feedback-data-item.model';

export class FeedbackData {
    title: string;
    items: FeedbackDataItem[];

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.items = data.items;
    }
}
