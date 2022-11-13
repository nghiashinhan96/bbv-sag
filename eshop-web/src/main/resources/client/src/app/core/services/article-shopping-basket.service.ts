import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';
import { get } from 'lodash';
import { ShoppingBasketService } from './shopping-basket.service';
import { ShoppingBasketModel } from '../models/shopping-basket.model';
import { map } from 'rxjs/operators';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { BroadcastService } from 'sag-common';
import { ArticlesService, ArticleBasketModel, ArticleModel, ArticleBroadcastKey } from 'sag-article-detail';
import { ShoppingBasketItemModel } from '../models/shopping-basket-item.model';
import { GrossPriceType } from 'sag-custom-pricing';
import { NONVEHICLE } from '../conts/app.constant';
import { SHOPPING_BASKET_ENUM } from '../enums/shopping-basket.enum';
import { AppStorageService } from './app-storage.service';

@Injectable({ providedIn: 'root' })
export class ArticleShoppingBasketService {
    private removeSubscription: Subscription;
    private pendingTask = [];
    private isLoadingBasket = false;
    private updateSubscription: Subscription;

    constructor(
        private fbRecordingService: FeedbackRecordingService,
        private articlesAnalyticService: ArticlesAnalyticService,
        private gaService: GoogleAnalyticsService,
        private articleService: ArticlesService,
        private shoppingBasket: ShoppingBasketService,
        private articleListBroadcastService: BroadcastService,
        private appStorage: AppStorageService
    ) { }

    observeArticleRemove(callback?) {
        if (this.removeSubscription) {
            this.removeSubscription.unsubscribe();
        }
        this.removeSubscription = this.shoppingBasket.removedArticles$
            .subscribe((cartKeys: string[]) => {
                (cartKeys || []).forEach(cartKey => {
                    this.articleListBroadcastService.broadcast(cartKey);
                    if (callback) {
                        callback(cartKey);
                    }
                });
            });
    }

    observeArticleUpdate(callback?) {
        if (this.updateSubscription) {
            this.updateSubscription.unsubscribe();
        }
        this.updateSubscription = this.shoppingBasket.updateArticles$
            .subscribe((shoppingBasketItems: ShoppingBasketItemModel[]) => {
                this.articleListBroadcastService.broadcast(ArticleBroadcastKey.UPDATE_ARTICLE_BY_CART_KEYS, shoppingBasketItems);
                if (callback) {
                    callback(shoppingBasketItems);
                }
            });
    }

    loadMiniBasket() {
        if (!this.shoppingBasket.miniBasket) {
            if (this.isLoadingBasket) {
                const promise = new Promise(resolve => {
                    this.pendingTask.push(resolve);
                });
                return promise;
            } else {
                this.isLoadingBasket = true;
                return new Promise(resolve => {
                    this.shoppingBasket.loadMiniBasket((shoppingBasket) => {
                        this.isLoadingBasket = false;
                        resolve(shoppingBasket);
                        while (this.pendingTask.length > 0) {
                            const task = this.pendingTask.pop();
                            task(shoppingBasket);
                        }
                    });
                });
            }
        } else {
            return Promise.resolve(this.shoppingBasket.miniBasket);
        }
    }

    isAddedInCart(data: ArticleBasketModel) {
        return this.loadMiniBasket().then((basket: ShoppingBasketModel) => {
            if (basket) {
                const cartItem = basket.items.find(item => this.isSameVehicleArticle(data, item));
                if (!!cartItem && data.callback) {
                    data.callback({
                        amount: cartItem.quantity,
                        cartKey: cartItem.cartKey,
                        price: {
                            netPriceWithVat: cartItem.netPriceWithVat,
                            grossPriceWithVat: cartItem.grossPriceWithVat,
                            totalGrossPriceWithVat: cartItem.totalGrossPriceWithVat,
                            totalNetPriceWithVat: cartItem.totalNetPriceWithVat,
                            discountPriceWithVat: cartItem.discountPriceWithVat,
                            totalGrossPrice: cartItem.totalGrossPrice,
                            totalNetPrice: cartItem.totalNetPrice
                        },
                        displayedPrice: cartItem.articleItem.displayedPrice
                    });
                }
                return !!cartItem;
            }
            return false;
        });
    }

    async isItemAddedToCart(data: ArticleBasketModel) {
        if (this.shoppingBasket.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
            return await this.isAddedInCart(data);
        } else {
            const basket = this.shoppingBasket.currentSubBasket;

            if (!basket || !basket.items || basket.items.length === 0) {
                return false;
            }
            const cartItem = basket.items.find(item => this.isSameVehicleArticle(data, item));
            if (cartItem && data.callback) {
                data.callback({
                    amount: cartItem.quantity,
                    cartKey: cartItem.cartKey,
                    price: {
                        grossPrice: cartItem.grossPrice,
                        totalGrossPrice: cartItem.totalGrossPrice,
                        totalNetPrice: cartItem.totalNetPrice,
                        netPriceWithVat: cartItem.netPriceWithVat,
                        grossPriceWithVat: cartItem.grossPriceWithVat,
                        totalGrossPriceWithVat: cartItem.totalGrossPriceWithVat,
                        totalNetPriceWithVat: cartItem.totalNetPriceWithVat,
                        discountPriceWithVat: cartItem.discountPriceWithVat
                    },
                    displayedPrice: cartItem.articleItem.displayedPrice
                });
            }

            return !!cartItem;
        }
    }

    private isSameVehicleArticle(articleBasketModel: ArticleBasketModel, shoppingBasketItemModel: ShoppingBasketItemModel): boolean {
        return articleBasketModel.pimId === shoppingBasketItemModel.articleItem.pimId
            && this.getArticleBasketModelVehicleId(articleBasketModel) === shoppingBasketItemModel.getVehicleId();
    }

    private getArticleBasketModelVehicleId(articleBasketModel: ArticleBasketModel) {
        return articleBasketModel.vehicle && articleBasketModel.vehicle.id || '';
    }

    addItemToCart(data: ArticleBasketModel, callback?: any) {
        const isGetRawValue = true;
        this.articleService.syncArticle({ amount: data.amount, pimId: data.pimId }, isGetRawValue)
            .pipe(map((res: any) => {
                if (res && !res.notFoundInAx) {
                    this.fbRecordingService.recordAvailability(res.artid, res.availabilities, true);
                }
                return res;
            }))
            .subscribe((article: any) => {
                if (!article || article.notFoundInAx) {
                    if (data.callback) {
                        data.callback(article);
                    }
                    return;
                }
                article.displayedPrice = data.article.displayedPrice;
                article.oilArticle = data.article.oilArticle;
                article.oilProduct = data.article.oilProduct;
                article.glassOrBody = data.article.glassOrBody;
                article.replacementForArtId = data.article.replacementForArtId;
                const category = data.category;
                const vehicle = data.vehicle;
                const body = {
                    category: {
                        gaId: category && category.belongedGaIds,
                        gaDesc: category && category.description || '',
                        rootDesc: category && category.rootDescription || ''
                    },
                    article,
                    vehicle,
                    quantity: data.amount,
                    basketItemSourceId: this.appStorage.basketItemSource.basketItemSourceId,
                    basketItemSourceDesc: this.appStorage.basketItemSource.basketItemSourceDesc
                };
                setTimeout(() => {
                    const gaArticle = {
                        ...data.article,
                        availabilities: article.availabilities,
                        index: data.index
                    };
                    const googleAnalyticsArgs = this.gaService.prepareProductFieldObjectArgs(gaArticle, data.amount);
                    this.gaService.updateBasketProduct(googleAnalyticsArgs, data.rootModalName);
                });
                this.shoppingBasket.addItemToCart(body).subscribe((basket: ShoppingBasketModel) => {
                    if (callback) {
                        callback(basket);
                    }
                    this.articlesAnalyticService.sendAddToBasketEventData(
                        {
                            ...data.article,
                            vehicle: data.vehicle,
                            amountNumber: data.amount
                        },
                        basket,
                        {
                            type: GrossPriceType.UVPE.toString(),
                            brand: vehicle && vehicle.vehicle_brand
                        }
                    );
                    const cartItem = basket.items.find(item =>
                        item.articleItem.pimId === data.pimId && (!vehicle || vehicle.vehid === NONVEHICLE || item.vehicleId === vehicle.id));

                    if (cartItem && data.callback) {
                        const newArticle = new ArticleModel(article);
                        newArticle.updatePrice({
                            netPriceWithVat: cartItem.netPriceWithVat,
                            grossPriceWithVat: cartItem.grossPriceWithVat,
                            totalGrossPriceWithVat: cartItem.totalGrossPriceWithVat,
                            totalNetPriceWithVat: cartItem.totalNetPriceWithVat,
                            discountPriceWithVat: cartItem.discountPriceWithVat,
                            totalGrossPrice: cartItem.totalGrossPrice,
                            totalNetPrice: cartItem.totalNetPrice
                        });
                        data.callback({
                            article,
                            cartKey: cartItem.cartKey,
                            amount: cartItem.quantity,
                            displayedPrice: cartItem.articleItem.displayedPrice
                        });
                    }
                });
            });
    }

    updateBasketItem(data: ArticleBasketModel, callback?: any) {
        const body = {
            pimId: data.pimId,
            amount: data.amount,
            vehicleId: get(data, 'vehicle.id', NONVEHICLE)
        };
        this.shoppingBasket.updateCartItem(body).subscribe((basket: ShoppingBasketModel) => {
            const cartItem = basket.items.find(item => item.cartKey === data.cartKey);
            if (callback) {
                callback(cartItem, basket);
            }
            if (data.callback) {
                data.callback({
                    article: cartItem.articleItem,
                    cartKey: cartItem.cartKey,
                    amount: cartItem.quantity,
                    displayedPrice: cartItem.articleItem.displayedPrice
                });
            }
        }, error => {

        });
    }

    updateTheInBasketArticle(articles, vehicleId) {
        this.loadMiniBasket().then((basket: ShoppingBasketModel) => {
            (articles || []).forEach(art => {
                const cartItem = basket.items.find(
                    item => item.articleItem.pimId === art.pimId && (!vehicleId || item.vehicleId === vehicleId));
                if (!!cartItem) {
                    this.articleListBroadcastService.broadcast(cartItem.articleItem.pimId, {
                        action: 'SYNC',
                        amount: cartItem.quantity,
                        article: cartItem.articleItem,
                        cartKey: cartItem.cartKey
                    });
                }
            });
        });
    }

    getArticleListAreAddedToCart(data: ArticleBasketModel[]) {
        return this.loadMiniBasket().then((basket: ShoppingBasketModel) => {
            if (basket) {
                const articles = (data || []).reduce((previousValue, currentValue) => {
                    const cartItem = basket.items.find(item => this.isSameVehicleArticle(currentValue, item));
                    if (!!cartItem) {
                        return [...previousValue, { pimId: currentValue.pimId, quantity: cartItem.quantity }];
                    }
                    return previousValue;
                }, []);
                return articles;
            }
            return [];
        });
    }
}
