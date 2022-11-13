import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { STGInfoComponent } from './stg-info.component';
import { STGInfoRoutingModule } from './stg-info-routing.module';

@NgModule({
  imports: [
    CommonModule,
    STGInfoRoutingModule,
    ConnectCommonModule
  ],
  declarations: [STGInfoComponent]
})

export class STGInfoModule { }
