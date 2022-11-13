import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { ArticleShoppingBasketService } from '../core/services/article-shopping-basket.service';
import { ArticleListSearchRoutingModule } from './article-list-search-routing.module';
import { ArticleListSearchComponent } from './article-list-search.component';
import { ArticleListItemComponent } from './components/article-list-item/article-list-item.component';
import { ImportArticlesModalComponent } from './components/import-articles-modal/import-articles-modal.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ArticleListSearchService } from './services/article-list-search.service';
import { SagCurrencyModule } from 'sag-currency';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SagArticleListModule } from 'sag-article-list';
import { CzCustomModule } from '../shared/cz-custom/cz-custom.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ModalModule.forRoot(),
        TranslateModule,
        ArticleListSearchRoutingModule,
        SagCurrencyModule,
        SagArticleListModule,
        ConnectCommonModule,
        CzCustomModule
    ],
    declarations: [
        ArticleListSearchComponent,
        ArticleListItemComponent,
        ImportArticlesModalComponent
    ],
    entryComponents: [
        ImportArticlesModalComponent
    ],
    providers: [
        ArticleShoppingBasketService,
        ArticleListSearchService
    ],
    exports: [
        ArticleListSearchComponent
    ]
})
export class ArticleListSearchModule { }
