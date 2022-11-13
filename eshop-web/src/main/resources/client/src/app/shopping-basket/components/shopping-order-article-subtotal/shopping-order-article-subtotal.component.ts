import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BasketPriceSummaryModel } from './../../models/basket-price-summary-model';
import { AffiliateUtil, AffiliateEnum } from 'sag-common';
import { environment } from 'src/environments/environment';
import { LibUserSetting } from 'sag-article-detail';
@Component({
  selector: 'connect-shopping-order-article-subtotal',
  templateUrl: './shopping-order-article-subtotal.component.html',
  styleUrls: ['./shopping-order-article-subtotal.component.scss']
})
export class ShoppingOrderArticleSubtotalComponent implements OnInit {
  @Input() userSetting: LibUserSetting;
  @Input() basketPriceSummary: BasketPriceSummaryModel;
  @Input() isCartStepMode: boolean;
  @Input() showVatIcon: boolean;
  @Input() currentStateVatConfirm: boolean;

  @Output() toggleVATEmit = new EventEmitter<any>();

  vatTypeDisplayConvert: any;
  cz = AffiliateEnum.CZ;
  sb = AffiliateEnum.SB;
  isFinalCustomer = false;
  isPDP: boolean;

  constructor() { }

  ngOnInit() {
    this.isPDP = AffiliateUtil.isAffiliateApplyPDP(environment.affiliate);
    this.isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
    this.vatTypeDisplayConvert = this.getVATSetting();
  }

  getVATSetting() {
    if (this.userSetting) {
      return this.userSetting.vatTypeDisplayConvert;
    }

    return null;
  }

  toggleVAT() {
    this.toggleVATEmit.emit();
  }
}
