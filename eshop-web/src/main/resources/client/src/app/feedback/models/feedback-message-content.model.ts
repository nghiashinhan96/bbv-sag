
export class FeedbackMessageContent {
    title: string;
    content: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.content = data.content;
    }
}
