import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SagArticleShowPriceComponent } from '../article-show-price/article-show-price.component';

@Component({
    selector: 'sag-article-cart-net-price',
    templateUrl: './article-cart-net-price.component.html',
    styleUrls: ['./article-cart-net-price.component.scss']
})
export class SagArticleCartNetPriceComponent extends SagArticleShowPriceComponent implements OnChanges {
    @Input() cartItem: any;
    @Input() netPrice: number;
    discount: number;

    ngOnChanges(changes: SimpleChanges): void {
        super.ngOnChanges(changes);

        if (changes.cartItem && !changes.cartItem.firstChange) {
            this.initData();
        }
    }

    initialItem() {
        return this.cartItem;
    }

    compareNetPriceAndNetPriceExlPromotion() {
        this.netPriceGreaterThanOrEqualNet1Price = this.cartItem.netPriceGreaterThanOrEqualNet1Price();
        this.netPriceLessThanNet1Price = this.cartItem.netPriceLessThanNet1Price();
    }

    initData() {
        if (!this.isPDP) {
            if(this.isFinalCustomer) {
                if(!this.isShowVat) {
                    this.cartItem.setFCNetPrice(this.userSetting, this.currentStateVatConfirm);
                }
            } else {
                this.netPrice = this.cartItem.getNetPrice(this.currentStateVatConfirm);
            }
        } else {
            this.compareNetPriceAndNetPriceExlPromotion();
            this.isShownDiscount = this.getShownDiscount();
            this.isShowPromotionDiscount = this.getShownPromotionDiscount();

            if(this.isShowVat) {
                this.netPrice = this.getNetPrice();
            } else {
                this.netPrice = this.getNetPrice(this.currentStateVatConfirm);
            }
        }
    }

    getNetPrice(includeVat = false) {
        return this.cartItem.getNetPrice(includeVat);
    }
}