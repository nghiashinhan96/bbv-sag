import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { LibUserSetting } from 'sag-article-detail';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'connect-shopping-order-article-total-price',
  templateUrl: './shopping-order-article-total-price.component.html',
  styleUrls: ['./shopping-order-article-total-price.component.scss']
})
export class ShoppingOrderArticleTotalPriceComponent implements OnInit, OnChanges {
  @Input() articlePrice: number;
  @Input() cartItem: ShoppingBasketItemModel;
  @Input() userSetting: LibUserSetting;
  @Input() currentStateVatConfirm: boolean;

  isPDP = AffiliateUtil.isAffiliateApplyPDP(environment.affiliate);
  isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
  isShowVat: boolean;

  totalPrice: number;
  totalPriceVat: number;

  netPriceGreaterThanOrEqualNet1Price = false;
  netPriceLessThanNet1Price = false;

  constructor() { }

  ngOnInit() {
    this.initialShowVat();
    this.initTotalCartPrice();
    this.compareNetPriceAndNetPriceExlPromotion();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.userSetting && !changes.userSetting.firstChange) {
      this.initTotalCartPrice();
      this.compareNetPriceAndNetPriceExlPromotion();
    }
  }

  private initialShowVat() {
    const inclVATSettings = this.userSetting && this.userSetting.vatTypeDisplayConvert;
    this.isShowVat = inclVATSettings && inclVATSettings.list;
  }

  private initTotalCartPrice() {
    if (this.isPDP) {
      if (this.isShowVat) {
        this.totalPrice = this.cartItem.getArticlePrice(this.userSetting.currentStateNetPriceView, false);
        this.totalPriceVat = this.cartItem.getArticlePrice(this.userSetting.currentStateNetPriceView, true);
      }
    } else {
      if (this.isFinalCustomer) {
        if (this.isShowVat) {
          this.totalPrice = this.cartItem.articleItem.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView);
          this.totalPriceVat = this.cartItem.articleItem.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView, true);
        } else {
          this.totalPrice = this.cartItem.articleItem.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView, this.currentStateVatConfirm);
        }
      }
    }
  }

  private compareNetPriceAndNetPriceExlPromotion() {
    this.netPriceGreaterThanOrEqualNet1Price = this.cartItem.netPriceGreaterThanOrEqualNet1Price();
    this.netPriceLessThanNet1Price = this.cartItem.netPriceLessThanNet1Price();
  }
}
