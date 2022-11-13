import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { GtmotiveComponent } from './gtmotive.component';
import { GtmotiveRoutingModule } from './gtmotive-routing.module';
import { ArticleInContextListConfigService } from '../article-in-context-result-list/services/article-in-context-list-config.service';
import { SagGtmotiveModule, SagGtmotiveConfigService } from 'sag-gtmotive';
import { SagArticleListModule } from 'sag-article-list';
import { GtmotiveConfigService } from './services/gtmotive-config.service';

@NgModule({
    declarations: [
        GtmotiveComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        GtmotiveRoutingModule,
        TranslateModule,
        NgSelectModule,
        NgxWebstorageModule,
        ConnectCommonModule,
        SagGtmotiveModule,
        SagArticleListModule
    ],
    providers: [
        { provide: SagGtmotiveConfigService, useClass: GtmotiveConfigService }
    ]
})
export class GtmotiveModule { }
