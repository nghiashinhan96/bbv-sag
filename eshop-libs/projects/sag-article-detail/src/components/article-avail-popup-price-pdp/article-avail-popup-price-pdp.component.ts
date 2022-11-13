import { Component } from '@angular/core';
import { SagArticleDetailAvailPopupShowPriceComponent } from '../article-avail-popup-show-price/article-avail-popup-show-price.component';
import { AffiliateUtil } from 'sag-common';
@Component({
  selector: 'sag-article-avail-popup-price-pdp',
  templateUrl: './article-avail-popup-price-pdp.component.html',
  styleUrls: ['./article-avail-popup-price-pdp.component.scss']
})
export class SagArticleDetailAvailPopupPricePdpComponent extends SagArticleDetailAvailPopupShowPriceComponent {
  initialPriceVat() {
    this.promotionInPercent = this.article.getPromotionDiscount();
    this.net1Price = this.article.getNet1Price();
    this.oepPriceVat = this.article.getOepPrice(true);
    this.uvpePriceVat = this.article.getUvpePrice(true);
    this.uvpPriceVat = this.article.getUvpPrice(true);
    this.kbPriceVat = this.article.getKbPrice(true);
    this.finalCustomerNetPriceVat = this.article.getFinalCustomerNetPrice(this.userSetting, true, AffiliateUtil.isFinalCustomerAffiliate(this.affiliateCode));
    this.netPriceVat = this.article.getNetPrice(true);
    this.net1PriceWithVat = this.article.getNet1Price(true);
    this.standardGrossPriceVat = this.initialStandardGrossPriceWithVat();

    if(AffiliateUtil.isAxCz(this.affiliateCode)) {
      this.dpcPriceWithVat = this.article.getDPC(true);
    }
  }

  initialStandardGrossPriceWithVat() {
    let standardGrossPrice = this.article.getGrossPrice(true);
    if (AffiliateUtil.isAffiliateCH(this.affiliateCode) || AffiliateUtil.isAxCz(this.affiliateCode)) {
      standardGrossPrice = this.article.getStandardGrossPrice(true) || this.article.getGrossPrice(true);
    }

    return standardGrossPrice;
  }

  initialShowPromotionDiscount() {
    if (!this.userSetting) {
      return false;
    }

    if (!this.article) {
      return;
    }

    return !!this.article.getPromotionDiscount() && this.article.netPriceLessThanNet1Price();
  }

  isShowBrutto() {
    return this.article.getBrutto() && this.userSetting.showGross && this.article.getBrutto() >= this.article.getNet1Price();
  }

  initialShowDiscount() {
    if (!this.userSetting) {
      return false;
    }

    if (!this.article) {
      return false;
    }
    // user setting and show 'Rabatt' as allowed by ERP
    const conditionOne = this.userSetting && this.userSetting.showGross && !!this.article.getDiscount();

    // don't have a Brutto, or when "Brutto < Netto1", then the discount is not shown.
    let conditionTwo = false;

    // net1PriceFound and nettto2 < netto1
    if(this.article.getNet1PriceFound() && this.article.netPriceLessThanNet1Price()) {
        conditionTwo = this.article.getBrutto() && this.article.getNet1Price() && this.article.getBrutto() > this.article.getNet1Price();
    }

    // net1PriceFound not found and nettto2 >= netto1
    if(!this.article.getNet1PriceFound() || this.article.netPriceGreaterThanOrEqualNet1Price()) {
        conditionTwo = this.article.getBrutto() && this.article.getNetPrice() && this.article.getBrutto() > this.article.getNetPrice();
    }

    return conditionOne && conditionTwo;
  }

  getNetPrice(){
    if(this.isLessThan){
      return this.isIncludeVAT ? this.net1PriceWithVat : this.net1Price;
    }
    return  this.isIncludeVAT ? this.netPriceVat : this.netPrice;
  }

  getPromotionNetPrice(){
    if(this.isLessThan){
      return this.isIncludeVAT ? this.netPrice : this.netPriceVat;
    }
    return  this.isIncludeVAT ? this.net1Price : this.net1PriceWithVat;
  }
}
