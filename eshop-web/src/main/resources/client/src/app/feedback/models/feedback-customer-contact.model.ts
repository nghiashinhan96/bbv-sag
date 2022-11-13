export class FeedbackCustomerContact {
    title: string;
    contact: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.contact = data.contact;
    }
}
