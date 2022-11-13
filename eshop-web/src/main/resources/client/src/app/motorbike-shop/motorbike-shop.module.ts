import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MotorbikeShopRoutingModule } from './motorbike-shop-routing.module';
import { MotorbikeShopComponent } from './motorbike-shop.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { HomeModule } from '../home/home.module';
import { ArticleSearchModule } from 'sag-article-search';
import { MotorbikeSearchComponent } from './components/motorbike-search/motorbike-search.component';
import { TranslateModule } from '@ngx-translate/core';


@NgModule({
  declarations: [
    MotorbikeShopComponent,
    MotorbikeSearchComponent,
  ],
  imports: [
    CommonModule,
    MotorbikeShopRoutingModule,
    ConnectCommonModule,
    HomeModule,
    ArticleSearchModule,
    TranslateModule
  ]
})
export class MotorbikeShopModule { }
