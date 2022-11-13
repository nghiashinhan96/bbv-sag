import { AffiliateUtil } from 'sag-common';
import { Component, Input, OnInit } from '@angular/core';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { ArticleModel } from '../../models/article.model';
@Component({
  selector: 'sag-article-avail-popup-price',
  templateUrl: './article-avail-popup-price.component.html',
  styleUrls: ['./article-avail-popup-price.component.scss']
})
export class SagArticleDetailAvailPopupPriceComponent implements OnInit {
  @Input() isPDP: boolean;
  @Input() userSetting: LibUserSetting;
  @Input() article: ArticleModel;
  @Input() currency: string;
  @Input() basketType: any;
  @Input() affiliateCode: string;
  @Input() currentStateVatConfirm: boolean;
  

  isFinalCustomer: boolean;

  ngOnInit() {
    this.isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(this.affiliateCode);
  }
}
