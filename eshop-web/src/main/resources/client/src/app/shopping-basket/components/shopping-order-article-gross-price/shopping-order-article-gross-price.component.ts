import { Component, Input, OnInit } from '@angular/core';
import { ArticleModel, LibUserSetting } from 'sag-article-detail';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { get } from 'lodash';

@Component({
  selector: 'connect-shopping-order-article-gross-price',
  templateUrl: './shopping-order-article-gross-price.component.html',
  styleUrls: ['./shopping-order-article-gross-price.component.scss']
})
export class ShoppingOrderArticleGrossPriceComponent implements OnInit {
  @Input() editable: boolean;
  @Input() article: ArticleModel;
  @Input() priceType: string;
  @Input() grossPrice: number;
  @Input() userSetting: LibUserSetting;
  @Input() currentStateVatConfirm: boolean;

  isPDP: boolean;
  isShowVat: boolean;
  isFinalCustomer: boolean;

  constructor () { }

  ngOnInit() {
    this.isPDP = AffiliateUtil.isAffiliateApplyPDP(environment.affiliate);
    this.isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);

    this.isShowVat = this.userSetting && get(this.userSetting, 'vatTypeDisplayConvert.list');
  }

}
