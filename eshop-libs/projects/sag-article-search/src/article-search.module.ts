import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SagSearchVehicleComponent } from './components/vehicle-search/vehicle-search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ArticleSearchConfigService } from './services/article-search-config.service';
import { TranslateModule } from '@ngx-translate/core';
import { SagSearchArticleComponent } from './components/article-search/article-search.component';
import { ArticleSearchService } from './services/article-search.service';
import { SagSearchMakeModelTypeComponent } from './components/make-model-type-search/make-model-type-search.component';
import { SagSearchVehicleDialogComponent } from './dialogs/all-vehicle-dialog/all-vehicle-dialog.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { RouterModule } from '@angular/router';
import { SagCommonModule } from 'sag-common';
import { SagSearchVehicleHistoryComponent } from './components/vehicle-history-search/vehicle-history-search.component';
import { SagSearchArticleHistoryComponent } from './components/article-history-search/article-history-search.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { VehicleSearchWrapperComponent } from './components/vehicle-search-wrapper/vehicle-search-wrapper.component';
import { MakeModelSearchComponent } from './components/make-model-search/make-model-search.component';
import { ArticleSearchStorageService } from './services/article-search-storage.service';
import { SagAdvanceSearchVehicleComponent } from './components/advance-vehicle-search/advance-search-vehicle.component'; 

@NgModule({
    declarations: [
        SagSearchVehicleComponent,
        SagSearchVehicleHistoryComponent,
        SagSearchArticleComponent,
        SagSearchVehicleDialogComponent,
        SagSearchMakeModelTypeComponent,
        SagSearchArticleHistoryComponent,
        VehicleSearchWrapperComponent,
        MakeModelSearchComponent,
        SagAdvanceSearchVehicleComponent,
    ],
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        TooltipModule.forRoot(),
        PopoverModule.forRoot(),
        ModalModule.forRoot(),
        SagCommonModule,
        TranslateModule,
        NgSelectModule,
        TabsModule.forRoot(),
    ],
    entryComponents: [SagSearchVehicleDialogComponent],
    exports: [
        SagSearchVehicleComponent,
        SagSearchVehicleHistoryComponent,
        SagSearchArticleComponent,
        SagSearchMakeModelTypeComponent,
        SagSearchArticleHistoryComponent,
        VehicleSearchWrapperComponent,
        MakeModelSearchComponent,
        SagAdvanceSearchVehicleComponent,
    ]
})
export class ArticleSearchModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: ArticleSearchModule,
            providers: [
                ArticleSearchConfigService,
                ArticleSearchService,
                ArticleSearchStorageService
            ]
        };
    }
}
