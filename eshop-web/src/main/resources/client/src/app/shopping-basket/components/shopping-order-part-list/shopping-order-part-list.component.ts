import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LibUserSetting } from 'sag-article-detail';

@Component({
  selector: 'connect-shopping-order-part-list',
  templateUrl: './shopping-order-part-list.component.html',
  styleUrls: ['./shopping-order-part-list.component.scss']
})
export class ShoppingOrderPartListComponent implements OnInit {
  @Input() data: any;
  @Input() userSetting: LibUserSetting;
  @Input() affiliateCode: string;

  @Output() currentNetPriceChange = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onCurrentNetPriceChange() {
    this.currentNetPriceChange.emit();
  }

}
