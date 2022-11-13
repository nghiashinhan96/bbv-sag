export class ArticleDescriptionSearchRequest {
    options: string[] = [];
    keyword: string;
    fullrequest = false;
    page = 0;

    constructor(data?: any) {
        if (data) {
            this.options = ['articles'];
            this.keyword = data.articleDescription.trim();
        }
    }
}
