export class FeedbackSource {
    title: string;
    source: string;
    code: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.source = data.source;
        this.code = data.code;
    }
}
