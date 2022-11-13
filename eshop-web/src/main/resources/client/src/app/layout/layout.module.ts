import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SaleComponent } from './components/sale/sale.component';
import { TranslateModule } from '@ngx-translate/core';
import { HeaderModule } from './components/header/header.module';
import { MenuBarModule } from './components/menu-bar/menu-bar.module';
import { ScrollerComponent } from './components/scroller/scroller.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { FeedbackModule } from '../feedback/feedback.module';
import { LayoutResolver } from './layout.resolver';

@NgModule({
  declarations: [
    LayoutComponent,
    SaleComponent,
    ScrollerComponent
  ],
  imports: [
    CommonModule,
    TranslateModule,
    LayoutRoutingModule,
    HeaderModule,
    MenuBarModule,
    ConnectCommonModule,
    FeedbackModule
  ],
  providers: [
    LayoutResolver
  ]
})
export class LayoutModule { }
