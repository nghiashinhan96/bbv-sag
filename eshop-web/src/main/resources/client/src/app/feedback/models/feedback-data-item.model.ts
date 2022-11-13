import { Constant } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackDataItem {
    key: string;
    value: string;
    title: string;
    childs: Array<FeedbackDataItem>;
    isShortTechnicalData: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.key = data.key;
        this.value = data.value;
        this.title = data.title;
        this.childs = data.childs;
        this.isShortTechnicalData = data.isShortTechnicalData;
    }

    getValue(): string {
        return FeedbackUtils.getValue(this.value);
    }

    get content(): string {
        return `${this.title}: ${this.getValue()}`;
    }
}
