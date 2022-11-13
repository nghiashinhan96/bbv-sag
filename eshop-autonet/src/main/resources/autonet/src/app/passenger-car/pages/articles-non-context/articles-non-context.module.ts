import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticlesNonContextRoutingModule } from './articles-non-context-routing.module';
import { ArticlesNonContextComponent } from './articles-non-context.component';
import { ArticleInfoComponent } from './components/article-info/article-info.component';
import { TranslateModule } from '@ngx-translate/core';
import { AutonetArticleListConfigService } from 'src/app/core/services/autonet-article-list-config.service';
import { AutonetCommonModule } from 'src/app/shared/autonet-common/autonet-common.module';
import { ArticleGroupSortService, ArticleListConfigService, SagArticleListModule } from 'sag-article-list';
import { AutonetSortingService } from '../articles-in-context/services/autonet-sorting.service';

@NgModule({
    declarations: [ArticlesNonContextComponent, ArticleInfoComponent],
    providers: [
        { provide: ArticleListConfigService, useClass: AutonetArticleListConfigService },
        { provide: ArticleGroupSortService, useClass: AutonetSortingService }
    ],
    imports: [
        CommonModule,
        ArticlesNonContextRoutingModule,
        TranslateModule,
        SagArticleListModule,
        AutonetCommonModule
    ]
})
export class ArticlesNonContextModule { }
