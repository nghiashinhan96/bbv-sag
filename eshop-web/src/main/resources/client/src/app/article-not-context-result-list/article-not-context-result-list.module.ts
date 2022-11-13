import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArticleNotContextResultListRoutingModule } from './article-not-context-result-list-routing.module';
import { ArticleResultContainerComponent } from './components/article-result-container/article-result-container.component';

import { TranslateModule } from '@ngx-translate/core';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { AnalyticLoggingModule } from '../analytic-logging/analytic-logging.module';
import { ArticleShoppingBasketService } from '../core/services/article-shopping-basket.service';
import { SagCurrencyModule } from 'sag-currency';
import { SagArticleListModule } from 'sag-article-list';
import { CzCustomModule } from '../shared/cz-custom/cz-custom.module';
@NgModule({
    declarations: [ArticleResultContainerComponent],
    providers: [
        ArticleShoppingBasketService
    ],
    imports: [
        CommonModule,
        ArticleNotContextResultListRoutingModule,
        TranslateModule,
        NgxWebstorageModule,
        SagCurrencyModule,
        SagArticleListModule,
        ModalModule.forRoot(),
        ConnectCommonModule,
        AnalyticLoggingModule,
        CzCustomModule
    ]
})
export class ArticleNotContextResultListModule { }
