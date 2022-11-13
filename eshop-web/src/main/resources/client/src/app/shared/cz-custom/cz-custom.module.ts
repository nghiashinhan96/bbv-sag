import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PopoverModule } from 'ngx-bootstrap/popover';
import {
    AvailPopoverContentComponent
} from './components/article-detail/article-detail-avail-popover-content/avail-popover-content.component';
import {
    ArticleDetailPriceComponent
} from './components/article-detail/article-detail-price/article-detail-price.component';
import {
    ArticleDetailTotalPriceComponent
} from './components/article-detail/article-detail-total-price/article-detail-total-price.component';
import { SagCurrencyModule } from 'sag-currency';
import { SingleArticleModalComponent } from './components/article-detail/single-article-modal/single-article-modal.component';
import { SagArticleDetailModule } from 'sag-article-detail';
import { ArticleDetailMemosComponent } from './components/article-detail/article-detail-memos/article-detail-memos.component';
import {
    ArticleDetailSpecialInfoComponent
} from './components/article-detail/article-detail-special-info/article-detail-special-info.component';

@NgModule({
    declarations: [
        SingleArticleModalComponent,
        AvailPopoverContentComponent,
        ArticleDetailPriceComponent,
        ArticleDetailTotalPriceComponent,
        ArticleDetailMemosComponent,
        ArticleDetailSpecialInfoComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        TranslateModule,
        PopoverModule.forRoot(),
        SagCurrencyModule,
        SagArticleDetailModule
    ],
    entryComponents: [
        SingleArticleModalComponent
    ],
    exports: [
        SingleArticleModalComponent,
        AvailPopoverContentComponent,
        ArticleDetailPriceComponent,
        ArticleDetailTotalPriceComponent,
        ArticleDetailMemosComponent,
        ArticleDetailSpecialInfoComponent
    ]
})
export class CzCustomModule { }
