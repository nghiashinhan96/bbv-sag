import { Component, OnInit } from '@angular/core';
import { ORDER_STATUS } from '../../enums/order-status';

@Component({
  selector: 'connect-new-order',
  templateUrl: './new-order.component.html',
  styleUrls: ['./new-order.component.scss']
})
export class NewOrderComponent implements OnInit {

  type = ORDER_STATUS.NEWS_ORDERS;
  constructor() { }

  ngOnInit() {
  }

}
