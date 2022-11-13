import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { SagArticleDetailModule } from 'sag-article-detail';
import { AutonetArticleListConfigService } from 'src/app/core/services/autonet-article-list-config.service';
import { SingleArticleModalComponent } from './single-article-modal/single-article-modal.component';
import { ArticleListConfigService } from 'sag-article-list';

@NgModule({
    declarations: [
        SingleArticleModalComponent
    ],
    imports: [
        CommonModule,
        SagArticleDetailModule,
        TranslateModule
    ],
    entryComponents: [
        SingleArticleModalComponent
    ],
    exports: [
        SingleArticleModalComponent
    ],
    providers: [
        { provide: ArticleListConfigService, useClass: AutonetArticleListConfigService }
    ]
})
export class SingleArticleModule { }
