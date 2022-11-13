import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SagCurrencyModule } from 'sag-currency';
import {
    SagInContextModule,
} from 'sag-in-context';
import { ArticlesInContextRoutingModule } from './articles-in-context-routing.module';
import { ArticleInContextResultListComponent } from './articles-in-context.component';
import { ArticlesInContextIntegrationService } from './services/articles-in-context-integration.service';
import { ArticlesInContextClassicCategoriesComponent } from './pages/classic-categories/classic-categories.component';
import { ArticlesInContextQuickClickComponent } from './pages/quick-click/quick-click.component';
import { ArtilesInContextListComponent } from './pages/articles-in-context-list/article-in-context-list.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { TranslateModule } from '@ngx-translate/core';
import { CzCustomModule } from '../shared/cz-custom/cz-custom.module';

@NgModule({
    declarations: [
        ArticleInContextResultListComponent,
        ArticlesInContextClassicCategoriesComponent,
        ArticlesInContextQuickClickComponent,
        ArtilesInContextListComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        PopoverModule.forRoot(),
        ArticlesInContextRoutingModule,
        SagInContextModule,
        SagCurrencyModule,
        CzCustomModule
    ],
    providers: [
        ArticlesInContextIntegrationService
    ]
})
export class ArticlesInContextModule { }
