import { FeedbackTopic } from './feedback-topic.model';
import { FeedbackCustomerContact } from './feedback-customer-contact.model';
import { FeedbackSource } from './feedback-source.model';
import { FeedbackMessageContent } from './feedback-message-content.model';
import { FeedbackData } from './feedback-data.model';

export class FeedbackSavingRequest {
    lang: string;
    topic: FeedbackTopic;
    affiliateStore: string;
    customerContact: FeedbackCustomerContact;
    source: FeedbackSource;
    message: FeedbackMessageContent;
    salesInfo: FeedbackData;
    userData: FeedbackData;
    technicalData: FeedbackData;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.lang = data.lang;
        this.topic = data.topic;
        this.affiliateStore = data.affiliateStore;
        this.customerContact = data.customerContact;
        this.source = data.source;
        this.message = data.message;
        this.salesInfo = data.salesInfo;
        this.userData = data.userData;
        this.technicalData = data.technicalData;
    }
}
