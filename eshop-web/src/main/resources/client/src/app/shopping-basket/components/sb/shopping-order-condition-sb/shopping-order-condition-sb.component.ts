import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

import { UserDetail } from 'src/app/core/models/user-detail.model';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { DELIVERY_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { SB_LOCATION_TYPES } from 'sag-article-detail';
@Component({
  selector: 'connect-shopping-order-condition-sb',
  templateUrl: './shopping-order-condition-sb.component.html',
  styleUrls: ['./shopping-order-condition-sb.component.scss']
})
export class ShoppingOrderConditionSBComponent implements OnInit {
  @Input() basketContext: ShoppingBasketContextModel;

  @Input() userDetail: UserDetail;
  @Input() finalOrder: OrderDashboardListItemModel = null;
  @Input() isVinOnly: boolean;

  @Output() updateShoppingBasketContext = new EventEmitter<{
    body: ShoppingBasketContextModel;
    reload: boolean;
    done: () => void;
  }>();

  COURIER = DELIVERY_TYPE.COURIER;
  PICKUP = DELIVERY_TYPE.PICKUP;

  PRIMARY_LOCATION = SB_LOCATION_TYPES.PRIMARY;

  constructor () { }

  ngOnInit() {
  }

  updateReferenceTextByLocation(event, index) {
    const value = event.target.value || '';
    this.basketContext.eshopBasketContextByLocation[index].referenceTextByLocation = value;
    this.updateContext(this.basketContext);
  }

  updateMsgBranch(event, index) {
    const value = event.target.value || '';
    this.basketContext.eshopBasketContextByLocation[index].messageToBranch = value;
    this.updateContext(this.basketContext);
  }

  private updateContext(body: ShoppingBasketContextModel, reload = false, done?: () => void) {
    this.updateShoppingBasketContext.emit({
      body,
      reload,
      done
    });
  }
}
