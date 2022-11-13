export abstract class SagArticleDetailIntegrationInterface {
    constructor() { }

    // Article Analytics Service
    abstract get userDetail(): any;
    abstract sendArticleListEventData(data: any): void;
    abstract sendWishlistGaData(data: any, rootModalName?): void;
    abstract sendShoppingBasketEvent(article: any, customBrand?: any);
    abstract recordFeedbackAvailability(articleId, availabilities, isUpdateAvaiForArticleItemAndCartItem?);
}
