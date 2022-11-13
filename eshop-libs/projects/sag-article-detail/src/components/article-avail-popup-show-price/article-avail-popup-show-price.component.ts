import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { get } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import { SagArticleDetailStorageService } from '../../services/article-detail-storage.service';

@Component({
  selector: 'sag-article-avail-popup-show-price',
  template: ''
})
export class SagArticleDetailAvailPopupShowPriceComponent implements OnInit, OnChanges {
  @Input() userSetting: LibUserSetting;
  @Input() article: ArticleModel;
  @Input() currency: string;
  @Input() basketType: any;
  @Input() affiliateCode: string;
  @Input() currentStateVatConfirm: boolean;

  isShowNetPrice: boolean;
  isShowDiscount: boolean;
  isShowPromotionDiscount: boolean;
  isShowVat: boolean;
  isLessThan: boolean;
  isAffiliateApplyFgasAndDeposit: boolean;
  isFinalCustomerAffiliate: boolean;

  standardGrossPrice: number;
  standardGrossPriceVat: number;
  oepPrice: number;
  oepPriceVat: number;
  uvpePrice: number;
  uvpePriceVat: number;
  uvpPrice: number;
  uvpPriceVat: number;
  kbPrice: number;
  kbPriceVat: number;
  discountPrice: number;
  discountInPercent: number;
  netPrice: number;
  netPriceVat: number;
  finalCustomerNetPrice: number;
  finalCustomerNetPriceVat: number;
  promotionInPercent: number;
  net1Price: number;
  net1PriceWithVat: number;
  dpcPrice: number;
  dpcPriceWithVat: number;
  isIncludeVAT: boolean;

  constructor(
    protected appStorage: SagArticleDetailStorageService
  ) { }

  ngOnInit(): void {
    this.isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(this.affiliateCode);
    this.iniialData();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.userSetting && !changes.userSetting.firstChange) {
      this.iniialData();
    }

    if (changes.article && !changes.article.firstChange) {
      this.iniialData();
    }
  }

  private iniialData() {
    if (this.userSetting && this.article) {
      this.initialSettings();
      this.initialPrices();
      this.initialPriceVat();
    }
  }

  isFinalBasket() {
    return this.basketType === 'SUB_FINAL_SHOPPING_CART';
  }

  initialSettings() {
    this.isShowVat = this.initialShowVat();
    this.isShowNetPrice = this.initialShowNetPrice();
    this.isShowDiscount = this.initialShowDiscount();
    this.isShowPromotionDiscount = this.initialShowPromotionDiscount();
    this.isLessThan = this.article.netPriceLessThanNet1Price();
    this.isFinalCustomerAffiliate = AffiliateUtil.isFinalCustomerAffiliate(this.affiliateCode);
    this.isIncludeVAT = !this.isShowVat && this.currentStateVatConfirm;
  }

  initialShowVat() {
    return this.userSetting && this.userSetting.vatTypeDisplayConvert && this.userSetting.vatTypeDisplayConvert.detail;
  }

  isShowBrutto() {
    return this.standardGrossPrice && this.userSetting.showGross;
  }

  initialShowNetPrice() {
    return this.userSetting.currentStateNetPriceView;
  }

  initialShowPromotionDiscount() {
    return false;
  }

  initialShowDiscount() {
    return this.article.isShownDiscount(this.userSetting); // not pdp
  }

  initialStandardGrossPrice() {
    let standardGrossPrice = this.article.grossPrice;
    if (AffiliateUtil.isAffiliateCH(this.affiliateCode) || AffiliateUtil.isAxCz(this.affiliateCode)) {
      standardGrossPrice = this.article.getStandardGrossPrice(this.isIncludeVAT) || this.article.grossPrice;
    }

    return standardGrossPrice;
  }

  initialPrices() {
    this.standardGrossPrice = this.initialStandardGrossPrice();

    this.oepPrice = get(this.article, 'displayedPrice.price') || this.article.getOepPrice(this.isIncludeVAT);
    this.uvpePrice = this.article.getUvpePrice(this.isIncludeVAT);
    this.uvpPrice = this.article.getUvpPrice(this.isIncludeVAT);
    this.kbPrice = this.article.getKbPrice(this.isIncludeVAT);
    this.discountPrice = this.article.getDiscountPrice();
    this.discountInPercent = this.article.getDiscount();
    this.finalCustomerNetPrice = this.article.getFinalCustomerNetPrice(this.userSetting, this.isIncludeVAT, this.isFinalCustomerAffiliate);
    this.netPrice = this.article.getNetPrice(this.isIncludeVAT);

    if(AffiliateUtil.isAxCz(this.affiliateCode)) {
      this.dpcPrice = this.article.getDPC(this.isIncludeVAT);
    }
  }

  initialPriceVat() { }
}
