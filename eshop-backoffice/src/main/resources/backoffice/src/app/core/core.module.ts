import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { SysNotificationComponent } from './components/sys-notification/sys-notification.component';
import { BackOfficeCommonModule } from '../shared/common/bo-common.module';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
    declarations: [
        SysNotificationComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        BackOfficeCommonModule,
        ModalModule.forRoot()
    ],
    providers: [
    ],
    entryComponents: [
        SysNotificationComponent
    ],
    exports: [
        SysNotificationComponent
    ]
})
export class CoreModule { }
