export class OfferArticleModel {
    id: number;
    articleNumber = '';
    name = '';
    description = '';
    amount: number;
    price: number;
    type: string;

    constructor(data?) {
        if (!data) {
            this.amount = 1;
            this.price = 1;
            return;
        }

        this.id = data.id;
        this.articleNumber = data.articleNumber;
        this.name = data.name;
        this.description = data.description;
        this.amount = data.amount;
        this.price = data.price;
        this.type = data.type;
    }
}
