import { Injectable } from '@angular/core';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { ArticleBasketModel } from 'sag-article-detail';

@Injectable()
export class ArticleListSearchService {
    constructor(
        private shoppingBasketService: ShoppingBasketService,
        private articleBasketService: ArticleShoppingBasketService
    ) { }

    async isItemAddedToCart(data: ArticleBasketModel) {
        if (this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
            return await this.articleBasketService.isAddedInCart(data);
        } else {
            const basket = this.shoppingBasketService.currentSubBasket;

            if (!basket || !basket.items || basket.items.length === 0) {
                return false;
            }
            const cartItem = basket.items.find(item => item.articleItem.pimId === data.pimId);
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
}
