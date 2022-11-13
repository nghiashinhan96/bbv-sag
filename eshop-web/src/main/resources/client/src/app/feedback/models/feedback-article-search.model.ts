import { Constant } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackArticleSearch {
    private articleNr: string;
    private articleName: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.articleNr = data.articleNr;
        this.articleName = data.articleName;
    }

    get articleNrValue() {
        return FeedbackUtils.getValue(this.articleNr);
    }

    get articleNameValue() {
        return FeedbackUtils.getValue(this.articleName);
    }

    get content(): string {
        return `${this.articleNrValue} - ${this.articleNameValue}`;
    }
}
