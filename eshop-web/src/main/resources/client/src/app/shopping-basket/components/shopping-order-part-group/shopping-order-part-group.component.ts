import { Component, Input, OnInit } from '@angular/core';
import { LibUserSetting } from 'sag-article-detail';

@Component({
  selector: 'connect-shopping-order-part-group',
  templateUrl: './shopping-order-part-group.component.html',
  styleUrls: ['./shopping-order-part-group.component.scss']
})
export class ShoppingOrderPartGroupComponent implements OnInit {
  @Input() set group(value) {
    this.data = value;
  }
  @Input() userSetting: LibUserSetting;

  data: any[];

  constructor() { }

  ngOnInit() {
  }

  trackByGroupKey(index: number, group) {
    return group.key;
  }

  trackByCartKey(index: number, cartItem) {
    return cartItem.cartKey;
  }

}
