import { FeedbackArticleAvailabilityItem } from './feedback-article-availability-item.model';

export class FeedbackArticleAvailability {
    articleId: string;
    avaiItems: Array<FeedbackArticleAvailabilityItem>;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.articleId = data.articleId;
        this.avaiItems = data.avaiItems;
    }

    get content(): string {
        if (this.avaiItems && this.avaiItems.length) {
            return this.avaiItems.map(item => item.content).join(', ');
        }
        return '';
    }
}
