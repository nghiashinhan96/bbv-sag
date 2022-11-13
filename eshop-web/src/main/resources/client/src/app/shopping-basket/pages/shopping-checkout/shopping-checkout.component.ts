import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { SagMessageData } from 'sag-common';

import { UserService } from 'src/app/core/services/user.service';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { DELIVERY_TYPE, ORDER_TYPE } from '../../../core/enums/shopping-basket.enum';
import { ParamUtil } from 'src/app/core/utils/params.utils';
import { ORDER_WARNING } from '../../enums/shopping-basket.enums';
import { AffiliateEnum } from 'sag-common';

@Component({
  selector: 'connect-shopping-checkout',
  templateUrl: './shopping-checkout.component.html',
  styleUrls: ['./shopping-checkout.component.scss']
})
export class ShoppingCheckoutComponent implements OnInit {
  isOrderRequest: boolean;
  orderNumber: string;
  orderTransaction: string;
  additionalMesgs: string[];
  counterPage: string;
  workIds: string[];
  error: SagMessageData;
  warnings: SagMessageData[] = [];
  public PREFIX_BARCODE_ID = 'barcode-';
  cz = AffiliateEnum.CZ;
  errorTimeout: boolean;
  articleName: string;
  sb = AffiliateEnum.SB;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public userService: UserService
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.counterPage = params.counterPage;

      if (ParamUtil.toBoolean(params.error)) {
        this.error = {
          type: 'WARNING',
          message: 'ORDER.WARNING.TRANSFER_PARTIALLY_FAILED'
        };
      }

      if (ParamUtil.toBoolean(params.errorTimeout)) {
        this.errorTimeout = true;
        this.articleName = params.articleName || '';
      }

      if (params.warning) {
        const warnings = JSON.parse(params.warning) || [];
        warnings.forEach(item => {
          const msg = this.getWarningMessage(item);

          if (msg) {
            this.warnings.push({
              type: 'WARNING',
              message: msg
            })
          }
        })
      }

      const orderNrs = params.orderNumber ? JSON.parse(params.orderNumber).join(' - ') : NOT_AVAILABLE;

      if (this.counterPage === ORDER_TYPE.COUNTER) {
        this.workIds = params.workIds ? JSON.parse(params.workIds) : [];
        this.orderNumber = orderNrs;
      } else {
        if (params[Constant.IS_ORDER_REQUEST] === 'true') {
          this.isOrderRequest = true;
          this.orderNumber = orderNrs;
          this.orderTransaction = params.transactionNr || NOT_AVAILABLE;
          const orderTypes = params.orderType && JSON.parse(params.orderType);
          const deliveryType = params.deliveryType;

          this.additionalMesgs = orderTypes.map(item => this.getAdditionalMessageByOrderType(item, deliveryType)).filter(msg => msg);

        } else {
          this.isOrderRequest = false;

        }
      }
    });
  }

  backToHomePage() {
    this.router.navigateByUrl(Constant.HOME_PAGE);
  }

  private getAdditionalMessageByOrderType(orderType, deliveryType): string {
    const isPickupDelivery = deliveryType && deliveryType === DELIVERY_TYPE[DELIVERY_TYPE.PICKUP];
    if (!isPickupDelivery) {
      return '';
    }
    switch (orderType) {
      case ORDER_TYPE.STD:
        return this.userService.userDetail.isSalesOnBeHalf ? 'SHOPPING_ORDER.CONFIRMATION.ADDITIONAL_MESG.STD' : '';
      case ORDER_TYPE.ABS:
        return 'SHOPPING_ORDER.CONFIRMATION.ADDITIONAL_MESG.ABS';
      default:
        return '';
    }
  }

  private getWarningMessage(code: string) {
    switch (code) {
      case ORDER_WARNING.INVALID_COUPON:
        return 'ORDER.WARNING.INVALID_COUPON';
      default:
        return '';
    }
  }
}
