import { Constant } from 'src/app/core/conts/app.constant';

export class ArticleSearchCriteria {
    type: string;
    articleNumber: string;
    name: string;
    description: string;
    amount: string;
    price: string;
    sort?: {
        sortBy: string,
        direction: string
    };

    constructor(type, articleNumber, name, description, amount, price, sort?) {
        this.type = type;
        this.articleNumber = articleNumber;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
        if (sort) {
            this.sort = sort;
        }
    }

    public static getEmptyCriteria(type: string) {
        return new ArticleSearchCriteria(type, Constant.EMPTY_STRING,
            Constant.EMPTY_STRING, Constant.EMPTY_STRING, Constant.EMPTY_STRING, Constant.EMPTY_STRING);
    }

    public static getEmptyCriteriaWithSorting(sort: any, type: string) {
        return new ArticleSearchCriteria(type, Constant.EMPTY_STRING,
            Constant.EMPTY_STRING, Constant.EMPTY_STRING, Constant.EMPTY_STRING, Constant.EMPTY_STRING, sort);
    }
}
