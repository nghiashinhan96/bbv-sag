export class ArticleSearchTranslatedText {
    articleNumber: string;
    articleDescription: string;
    searchButtonLabel: string;

    constructor(data?: any) {
        if (data) {
            this.articleNumber = data.articleNumber;
            this.articleDescription = data.articleDescription;
            this.searchButtonLabel = data.searchButtonLabel;
        }
    }
}
