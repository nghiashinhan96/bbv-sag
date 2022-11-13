import { Component } from '@angular/core';
import { SagArticleDetailAvailPopupShowPriceComponent } from '../article-avail-popup-show-price/article-avail-popup-show-price.component';
import { AffiliateUtil } from 'sag-common';

@Component({
  selector: 'sag-article-avail-popup-price-final',
  templateUrl: './article-avail-popup-price-final.component.html',
  styleUrls: ['./article-avail-popup-price-final.component.scss']
})
export class SagArticleDetailAvailPopupPriceFinalComponent extends SagArticleDetailAvailPopupShowPriceComponent {
  initialShowNetPrice() {
    return this.userSetting.fcUserCanViewNetPrice;
  }

  initialStandardGrossPrice() {
    return this.article.getStandardGrossPrice() || this.article.grossPrice;
  }

  initialPriceVat() {
    this.finalCustomerNetPriceVat = this.article.getFinalCustomerNetPrice(this.userSetting, true, AffiliateUtil.isFinalCustomerAffiliate(this.affiliateCode));
  }
}
