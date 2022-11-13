import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { SaleComponent } from './components/sale/sale.component';

import { TabsModule } from 'ngx-bootstrap/tabs';
import { TranslateModule } from '@ngx-translate/core';
import { CateloguesComponent } from './pages/catelogues/catelogues.component';
import { AdministrationComponent } from './pages/administration/administration.component';
import { ShoppingBasketComponent } from './pages/shopping-basket/shopping-basket.component';
import { AutonetCommonModule } from '../shared/autonet-common/autonet-common.module';
import { PriceOfferComponent } from './pages/price-offer/price-offer.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ScrollerComponent } from './components/scroller/scroller.component';
import { ShowpageComponent } from './pages/showpage/showpage.component';
@NgModule({
  declarations: [
    LayoutComponent,
    HeaderComponent,
    FooterComponent,
    SaleComponent,
    CateloguesComponent,
    AdministrationComponent,
    ShoppingBasketComponent,
    PriceOfferComponent,
    ShowpageComponent,
    ScrollerComponent,
  ],
  imports: [
    CommonModule,
    LayoutRoutingModule,
    TabsModule.forRoot(),
    TranslateModule,
    AutonetCommonModule,
    BsDropdownModule.forRoot()
  ]
})
export class LayoutModule { }
