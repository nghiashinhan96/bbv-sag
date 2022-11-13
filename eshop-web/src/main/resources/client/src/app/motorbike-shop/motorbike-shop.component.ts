import { Component, OnInit } from '@angular/core';
import { ADS_TARGET_NAME, AffiliateEnum, VEHICLE_CLASS } from 'sag-common';
import { UserService } from '../core/services/user.service';

@Component({
  selector: 'connect-motorbike-shop',
  templateUrl: './motorbike-shop.component.html',
  styleUrls: ['./motorbike-shop.component.scss']
})
export class MotorbikeShopComponent implements OnInit {
  MATIK_CH = AffiliateEnum.MATIK_CH;
  readonly adsTargetName = ADS_TARGET_NAME.MOTOBIKES;
  readonly vehicleClass = VEHICLE_CLASS.MB;
  constructor(
    public userService: UserService
  ) { }

  ngOnInit() {
  }

}
