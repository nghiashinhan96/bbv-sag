
export class FeedbackTopic {
    title: string;
    topicCode: string;
    topic: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.topicCode = data.topicCode;
        this.topic = data.topic;
    }
}
