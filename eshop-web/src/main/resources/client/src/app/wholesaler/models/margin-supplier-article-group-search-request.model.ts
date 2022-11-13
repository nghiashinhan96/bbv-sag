import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';

export class MarginSupplierArticleGroupSearchRequestModel {
    articleGroup: string = null;
    articleGroupDesc: string = null;
    page: number = 0;
    size: number = APP_DEFAULT_PAGE_SIZE;

    constructor (data = null) {
        if (data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });
        }
    }

    searchBody() {
        const { articleGroup, articleGroupDesc } = this;

        return {
            articleGroup: articleGroup.trim(),
            articleGroupDesc: articleGroupDesc.trim()
        };
    }

    searchParams() {
        const { page, size } = this;
        return { page, size };
    }
}