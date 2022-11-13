import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuBarComponent } from './menu-bar.component';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { PromotionModalComponent } from './dialogs/promotion-modal/promotion-modal.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ShopInfoModalComponent } from './dialogs/shop-info-modal/shop-info-modal.component';
import { VideoPlayerComponent } from './dialogs/video-player/video-player.component';
import { ConnectCommonModule } from 'src/app/shared/connect-common/connect-common.module';


@NgModule({
    declarations: [
        MenuBarComponent,
        PromotionModalComponent,
        ShopInfoModalComponent,
        VideoPlayerComponent
    ],
    imports: [
        CommonModule,
        RouterModule,
        TranslateModule,
        ConnectCommonModule,
        ModalModule.forRoot()
    ],
    entryComponents: [
        PromotionModalComponent,
        ShopInfoModalComponent,
        VideoPlayerComponent
    ],
    exports: [
        MenuBarComponent,
        PromotionModalComponent,
        ShopInfoModalComponent,
        VideoPlayerComponent
    ]
})
export class MenuBarModule { }
