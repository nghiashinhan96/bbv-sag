import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header.component';
import { TranslateModule } from '@ngx-translate/core';
import { BackOfficeCommonModule } from 'src/app/shared/common/bo-common.module';

@NgModule({
    declarations: [
        HeaderComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        BackOfficeCommonModule
    ],
    entryComponents: [
    ],
    exports: [
        HeaderComponent
    ]
})
export class HeaderModule { }
