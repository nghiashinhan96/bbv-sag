import { Component, Input, OnInit } from '@angular/core';
import { LibUserSetting } from 'sag-article-detail';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SubSink } from 'subsink';
import { OrderAvailabilityModel } from '../../../models/sb/order-availability.model';

@Component({
  selector: 'connect-shopping-chekout-sb',
  templateUrl: './shopping-chekout-sb.component.html',
  styleUrls: ['./shopping-chekout-sb.component.scss']
})
export class ShoppingChekoutSBComponent implements OnInit {
  @Input() userSetting: LibUserSetting;

  private subs = new SubSink();
  public orders: any[] = [];

  msg = {
    message: 'ORDER.WARNING.TRANSFER_PARTIALLY_FAILED',
    type: 'WARNING'
  };

  constructor (
    private shoppingBasketService: ShoppingBasketService
  ) { }

  ngOnInit() {
    this.subs.sink = this.shoppingBasketService.responseOrders$
      .subscribe((orders: any) => {
        this.orders = orders || [];

        this.orders.forEach(order => {
          order.expanded = true;
          order.orderAvailabilities = (order.orderAvailabilities || []).map(item => new OrderAvailabilityModel(item));
        });
      });

  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  trackByIndex(index: number) {
    return index;
  }
}
