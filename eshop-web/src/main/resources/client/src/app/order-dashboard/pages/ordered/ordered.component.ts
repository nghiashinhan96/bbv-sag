import { Component, OnInit } from '@angular/core';
import { ORDER_STATUS } from '../../enums/order-status';

@Component({
  selector: 'connect-ordered',
  templateUrl: './ordered.component.html',
  styleUrls: ['./ordered.component.scss']
})
export class OrderedComponent implements OnInit {
  type = ORDER_STATUS.ORDERED;
  
  constructor() { }

  ngOnInit() {
  }

}
