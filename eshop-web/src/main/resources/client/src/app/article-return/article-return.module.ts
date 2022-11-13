import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArticleReturnRoutingModule } from './article-return-routing.module';

import { SagTableModule } from 'sag-table';
import { ArticleReturnListComponent } from './pages/article-return-list/article-return-list.component';
import { ArticleReturnContainerComponent } from './pages/article-return-container/article-return-container.component';
import { ArticleReturnConfirmationComponent } from './pages/article-return-confirmation/article-return-confirmation.component';

import { ReturnOrderInformationModalComponent } from './components/return-order-information-modal/return-order-information-modal.component';

import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { SagCurrencyModule } from 'sag-currency';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { ArticleReturnService } from './services/article-return.service';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
    declarations: [ArticleReturnListComponent, ArticleReturnContainerComponent, ArticleReturnConfirmationComponent, ReturnOrderInformationModalComponent],
    imports: [
        CommonModule,
        FormsModule,
        ArticleReturnRoutingModule,
        TranslateModule,
        ConnectCommonModule,
        NgSelectModule,
        SagCurrencyModule,
        SagTableModule,
        ModalModule.forRoot(),
    ],
    providers: [ArticleReturnService],
    entryComponents: [ReturnOrderInformationModalComponent]
})
export class ArticleReturnModule { }
