import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { DVSECatalogComponent } from './dvse-catalog.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([{
      path: '', component: DVSECatalogComponent
    }]),
    ConnectCommonModule
  ],
  declarations: [DVSECatalogComponent]
})

export class DVSECatalogModule { }
