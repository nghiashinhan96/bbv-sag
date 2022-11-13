import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';

import { SagCommonModule } from 'sag-common';
import { SagGtmotiveModule } from 'sag-gtmotive';
import { SagArticleListModule, ArticleListConfigService } from 'sag-article-list';
import { SagVehicleListModule } from 'sag-vehicle-list';

import { SagInContextResolver } from './services/articles-in-context.resolver';
import { SagInContextVehicleInfoYearPipe } from './pages/vehicle-info-dialog/vehicle-info-year.pipe';
import { SagInContextArticleResultListComponent } from './pages/articles-in-context/articles-in-context.component';
import { SagInContextQuickClickComponent } from './pages/quick-click/quick-click.component';
import { SagInContextArticleListComponent } from './pages/articles-in-context-list/articles-in-context-list.component';
import { SagInContextVehicleInfoComponent } from './pages/vehicle-info/vehicle-info.component';
import { SagInContextVehicleInfoDialogComponent } from './pages/vehicle-info-dialog/vehicle-info-dialog.component';
import { SagInContextClassicCategoriesComponent } from './pages/classic-categories/classic-categories.component';
import { SagInContextVinPopupComponent } from './components/vin-popup/vin-popup.component';
import { SagInContextIntegrationService } from './services/articles-in-context-integration.service';
import { RouterModule } from '@angular/router';
import { ArticleInContextService } from './services/articles-in-context.service';
import { SagArticleListConfigServiceImpl } from './services/articles-list-config-impl.service';
import { SagInContextStorageService } from './services/articles-in-context-storage.service';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ModalModule } from 'ngx-bootstrap/modal';
import { SagArticleDetailModule } from 'sag-article-detail';

@NgModule({
    declarations: [
        SagInContextClassicCategoriesComponent,
        SagInContextArticleResultListComponent,
        SagInContextQuickClickComponent,
        SagInContextArticleListComponent,
        SagInContextVehicleInfoComponent,
        SagInContextVehicleInfoDialogComponent,
        SagInContextVehicleInfoYearPipe,
        SagInContextVinPopupComponent
    ],
    entryComponents: [
        SagInContextVehicleInfoDialogComponent,
        SagInContextVinPopupComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        RouterModule,
        ModalModule.forRoot(),
        ReactiveFormsModule,
        SagCommonModule,
        SagGtmotiveModule,
        SagArticleListModule,
        NgxWebstorageModule,
        SagArticleDetailModule,
        SagVehicleListModule,
    ],
    exports: [
        SagInContextArticleResultListComponent,
        SagInContextArticleListComponent,
        SagInContextClassicCategoriesComponent,
        SagInContextQuickClickComponent
    ]
})
export class SagInContextModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagInContextModule,
            providers: [
                SagInContextStorageService,
                SagInContextIntegrationService,
                SagArticleListConfigServiceImpl,
                { provide: ArticleListConfigService, useExisting: SagArticleListConfigServiceImpl },
                SagInContextResolver,
                ArticleInContextService
            ]
        };
    }
}

