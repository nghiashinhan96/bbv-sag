import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { LibUserSetting } from 'sag-article-detail';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'connect-shopping-order-part-header',
  templateUrl: './shopping-order-part-header.component.html',
  styleUrls: ['./shopping-order-part-header.component.scss']
})
export class ShoppingOrderPartHeaderComponent implements OnInit, OnChanges {
  @Input() userSetting: LibUserSetting;
  @Input() affiliateCode: string;
  @Input() articleListType: string;

  @Output() currentNetPriceChange = new EventEmitter();

  isShowNetPrice: boolean;
  canViewNetPrice: boolean;

  cz = AffiliateEnum.CZ;
  ehcz = AffiliateEnum.EH_CZ;
  ehaxcz = AffiliateEnum.EH_AX_CZ;
  isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
  sb = AffiliateEnum.SB;
  isSB = AffiliateUtil.isSb(environment.affiliate);

  constructor() { }

  ngOnInit() {
    this.isShowNetPrice = this.userSetting.currentStateNetPriceView;
    this.canViewNetPrice = this.userSetting.canViewNetPrice;
  }

  onOffNetPrice() {
    if (this.userSetting && this.userSetting.netPriceView) {
      this.currentNetPriceChange.emit();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.userSetting && !changes.userSetting.firstChange) {
      this.isShowNetPrice = this.userSetting.currentStateNetPriceView;
    }
  }

}
