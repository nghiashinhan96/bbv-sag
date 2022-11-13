import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ArticleAvailLocationItemModel } from '../../models/article-avail-location-item.model';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';

@Component({
  selector: 'sag-article-warning-avail-popup',
  templateUrl: './article-warning-avail-popup.component.html',
  styleUrls: ['./article-warning-avail-popup.component.scss']
})
export class SagArticleWarningAvailPopupComponent implements OnInit {
  @Input() locations: ArticleAvailLocationItemModel[] = [];
  @Input() userSetting: LibUserSetting;
  
  message = {
    message: 'SHOPPING_BASKET.REQUEST_QUANTITY_NOT_AVAIL_AT_PRIMARY_LOCATION',
    type: 'WARNING'
  };

  constructor(
    public bsModalRef: BsModalRef
  ) { }

  ngOnInit() {
  }

}
