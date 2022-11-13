import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { FooterComponent } from './components/footer/footer.component';
import { TranslateModule } from '@ngx-translate/core';
import { HeaderModule } from './components/header/header.module';
import { SharedModules } from '../shared/components/shared-components.module';
import { HomeAuthResolver } from '../home/home-auth-resolver.service';

@NgModule({
    declarations: [
        LayoutComponent,
        FooterComponent,
    ],
    imports: [
        CommonModule,
        TranslateModule,
        LayoutRoutingModule,
        HeaderModule,
        SharedModules
    ],
    providers: [
        HomeAuthResolver
    ]
})
export class LayoutModule { }
