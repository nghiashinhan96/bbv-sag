import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UnicatCatalogComponent } from './unicat-catalog.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { UnicatCatalogRoutingModule } from './unicat-catalog-routing.module';

@NgModule({
  imports: [
    CommonModule,
    UnicatCatalogRoutingModule,
    ConnectCommonModule
  ],
  declarations: [UnicatCatalogComponent]
})

export class UnicatCatalogModule { }
