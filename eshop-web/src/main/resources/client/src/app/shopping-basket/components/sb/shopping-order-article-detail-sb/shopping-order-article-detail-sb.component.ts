import { Component, Input, OnInit } from '@angular/core';
import { SAG_COMMON_DATETIME_FORMAT } from 'sag-common';

@Component({
  selector: 'connect-shopping-order-article-detail-sb',
  templateUrl: './shopping-order-article-detail-sb.component.html',
  styleUrls: ['./shopping-order-article-detail-sb.component.scss']
})
export class ShoppingOrderArticleDetailSbComponent implements OnInit {
  @Input() orderItem: any;
  dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;

  constructor () { }

  ngOnInit() {
  }

}
