import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
export class MarginArticleGroupSearchRequestModel {
    sagArticleGroup: string = '';
    sagArticleGroupDesc: string = '';
    customArticleGroup: string = '';
    customArticleGroupDesc: string = '';
    page: number = 0;
    size: number = APP_DEFAULT_PAGE_SIZE;

    constructor (data = null) {
        if (data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });
        }
    }

    getRequestDto() {
        const { sagArticleGroup, sagArticleGroupDesc, customArticleGroup, customArticleGroupDesc } = this;

        return { sagArticleGroup, sagArticleGroupDesc, customArticleGroup, customArticleGroupDesc };
    }
}