import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
@Component({
  selector: 'sag-article-avail-popup-deposit-article',
  templateUrl: './article-avail-popup-deposit-article.component.html',
  styleUrls: ['./article-avail-popup-deposit-article.component.scss']
})
export class SagArticleAvailPopupDepositArticleComponent implements OnInit, OnChanges {
  @Input() article: ArticleModel;
  @Input() userSetting: LibUserSetting;
  @Input() showInclVatCol: boolean;
  @Input() includeVAT: boolean = false;

  isShowVat: boolean;

  depositPrice: number;
  depositPriceWithVat: number;
  vocPrice: number;
  vocPriceWithVat: number;
  vrgPrice: number;
  vrgPriceWithVat: number;
  pfandPrice: number;
  pfandPriceWithVat: number;

  isShowDeposit: boolean;
  isShowVOC: boolean;
  isShowVRG: boolean;
  isShowPFAND: boolean;
  isShowDepositArticle: boolean;

  constructor() { }

  ngOnInit() {
    this.initSettings();
    this.initDepositPrices();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.article && !changes.article.firstChange) {
      this.initSettings();
      this.initDepositPrices();
    }
  }

  private initDepositPrices() {
    this.depositPrice = this.isShowDeposit && this.article.depositArticle.getDepositGrossPrice();
    this.depositPriceWithVat = this.isShowDeposit && this.article.depositArticle.getDepositGrossPrice(true);

    this.vocPrice = this.isShowVOC && this.article.vocArticle.getDepositGrossPrice();
    this.vocPriceWithVat = this.isShowVOC && this.article.vocArticle.getDepositGrossPrice(true);

    this.vrgPrice = this.isShowVRG && this.article.vrgArticle.getDepositGrossPrice();
    this.vrgPriceWithVat = this.isShowVRG && this.article.vrgArticle.getDepositGrossPrice(true);

    this.pfandPrice = this.isShowPFAND && this.article.pfandArticle.getDepositGrossPrice();
    this.pfandPriceWithVat = this.isShowPFAND && this.article.pfandArticle.getDepositGrossPrice(true);
  }


  private initSettings() {
    this.isShowDeposit = !!this.article.depositArticle;
    this.isShowVOC = !!this.article.vocArticle;
    this.isShowVRG = !!this.article.vrgArticle;
    this.isShowPFAND = !!this.article.pfandArticle;

    this.isShowDepositArticle = this.isShowDeposit || this.isShowVOC || this.isShowPFAND || this.isShowVRG;
    this.isShowVat = this.initialShowVat();
  }

  private initialShowVat() {
    return this.userSetting && this.userSetting.vatTypeDisplayConvert && this.userSetting.vatTypeDisplayConvert.detail;
  }
}
