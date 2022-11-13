import { Component, OnInit } from '@angular/core';
import { ORDER_STATUS } from '../../enums/order-status';

@Component({
  selector: 'connect-my-customer-order',
  templateUrl: './my-customer-order.component.html',
  styleUrls: ['./my-customer-order.component.scss']
})
export class MyCustomerOrderComponent implements OnInit {

  type = ORDER_STATUS.MY_CUSTOMER_ORDERS;
  constructor() { }

  ngOnInit() {
  }

}
