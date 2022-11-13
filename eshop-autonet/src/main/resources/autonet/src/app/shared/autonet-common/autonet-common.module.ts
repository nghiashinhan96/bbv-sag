import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertiseComponent } from './components/advertise/advertise.component';
import { AutonetFrameComponent } from './components/autonet-frame/autonet-frame.component';
import { AutonetFrameDialogComponent } from './dialogs/autonet-frame-dialog/autonet-frame-dialog.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AppMessageDialogComponent } from './dialogs/app-message-dialog/app-message-dialog.component';
import { TranslateModule } from '@ngx-translate/core';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { ArticleFreeTextSearchComponent } from 'src/app/passenger-car/component/article-free-text-search/article-free-text-search.component';
import { ArticleSearchModule } from 'sag-article-search';
import { ReactiveFormsModule } from '@angular/forms';
import { ArticleReplaceModalComponent } from './components/article-replace-modal/article-replace-modal.component';
import { SagArticleListModule } from 'sag-article-list';
import { AccessoryListModalComponent } from './components/article-accessories-modal/article-accessories-modal.component';
import { PartsListModalComponent } from './components/article-parts-list-modal/article-parts-list-modal.component';
import { ArticleCrossReferenceModalComponent } from './components/article-cross-reference-modal/article-cross-reference-modal.component';
import { SagCommonModule } from 'sag-common';
@NgModule({
    declarations: [
        AdvertiseComponent,
        AutonetFrameComponent,
        AutonetFrameDialogComponent,
        AppMessageDialogComponent,
        ArticleFreeTextSearchComponent,
        SidebarComponent,
        ArticleReplaceModalComponent,
        AccessoryListModalComponent,
        PartsListModalComponent,
        ArticleCrossReferenceModalComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        ModalModule.forRoot(),
        ReactiveFormsModule,
        ArticleSearchModule,
        SagArticleListModule,
        SagCommonModule
    ],
    entryComponents: [
        AutonetFrameDialogComponent,
        AppMessageDialogComponent,
        ArticleReplaceModalComponent,
        AccessoryListModalComponent,
        PartsListModalComponent,
        ArticleCrossReferenceModalComponent
    ],
    exports: [
        AdvertiseComponent,
        AutonetFrameComponent,
        AutonetFrameDialogComponent,
        AppMessageDialogComponent,
        SidebarComponent
    ]
})
export class AutonetCommonModule { }
