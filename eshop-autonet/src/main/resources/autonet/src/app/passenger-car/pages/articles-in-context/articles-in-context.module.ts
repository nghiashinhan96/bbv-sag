import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArticlesInContextRoutingModule } from './articles-in-context-routing.module';
import { ArticleInContextComponent } from './articles-in-context.component';
import { QuickClickComponent } from './pages/quick-click/quick-click.component';
import { ArticleInContextListComponent } from './pages/article-in-context-list/article-in-context-list.component';
import { VehicleInfoComponent } from './pages/vehicle-info/vehicle-info.component';
import { VehicleInfoDialogComponent } from './pages/vehicle-info-dialog/vehicle-info-dialog.component';
import { VehicleInfoYearPipe } from './pages/vehicle-info-dialog/vehicle-info-year.pipe';
import { TranslateModule } from '@ngx-translate/core';
import { AutonetArticleListConfigService } from 'src/app/core/services/autonet-article-list-config.service';
import { AutonetSortingService } from './services/autonet-sorting.service';
import { AutonetCommonModule } from 'src/app/shared/autonet-common/autonet-common.module';
import { ArticleGroupSortService, ArticleListConfigService, SagArticleListModule } from 'sag-article-list';
import { PopoverModule } from 'ngx-bootstrap/popover';

@NgModule({
    declarations: [
        ArticleInContextComponent,
        QuickClickComponent,
        ArticleInContextListComponent,
        VehicleInfoComponent,
        VehicleInfoDialogComponent,
        VehicleInfoYearPipe
    ],
    entryComponents: [VehicleInfoDialogComponent],
    providers: [
        AutonetSortingService,
        { provide: ArticleListConfigService, useClass: AutonetArticleListConfigService },
        { provide: ArticleGroupSortService, useClass: AutonetSortingService },
    ],
    imports: [
        CommonModule,
        ArticlesInContextRoutingModule,
        TranslateModule,
        SagArticleListModule,
        AutonetCommonModule,
        PopoverModule.forRoot()
    ]
})
export class ArticlesInContextModule { }
