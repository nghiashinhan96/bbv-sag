import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { TreeModule } from 'angular-tree-component';

import { BroadcastService, SagCommonModule } from 'sag-common';
import { SagArticleDetailModule, ArticleDetailConfigService } from 'sag-article-detail';

import { QuickClickService } from './services/quick-click.service';
import { CategoryTreeService } from './services/category-tree.service';
import { ArticleGroupSortService } from './services/article-group-sort.service';
import { MultiLevelCategoryTreeService } from './services/multi-level-category-tree.service';
import { ArticleListStorageService } from './services/article-list-storage.service';
import { ClassicCategoryService } from './services/classic-category.service';
import { SagArticleListClassicCategoryItemsPipe } from './pipes/classic-category-items.pipe';
import { SagArticleListClassicCategoryLeafPipe } from './pipes/classic-category-leaf.pipe';
import { SagArticleListClassicCategoryColumnPipe } from './pipes/classic-category-column.pipe';

import { SagArticleListNonMerkmaleAttributesComponent } from './components/non-merkmale-attributes/non-merkmale-attributes.component';
import { SagArticleListNonMerkmaleFilterItemComponent } from './components/non-merkmale-filter-item/non-merkmale-filter-item.component';
import { SagArticleListNonMerkmaleFilterComponent } from './pages/non-merkmale-filter/non-merkmale-filter.component';
import { SagArticleListNonMerkmaleListComponent } from './components/non-merkmale-filter-list/non-merkmale-filter-list.component';
import { SagArticleListMerkmaleFilterComponent } from './pages/merkmale-filter/merkmale-filter.component';
import { SagArticleListClassicCategoryComponent } from './pages/classic-category/classic-category.component';
import { SagArticleListClassicCategoryItemsComponent } from './components/classic-category-items/classic-category-items.component';
import { SagArticleListShoppingGroupComponent } from './components/shopping-group/shopping-group.component';
import { SagArticleListGaidGroupComponent } from './components/article-gaid-group/article-gaid-group.component';
import { SagArticleListCategoryTreeComponent } from './pages/category-tree/category-tree.component';
import { SagArticleListCategoryTreeSearchComponent } from './pages/category-tree-search/category-tree-search.component';
import { SagArticleListCategoryTreeItemComponent } from './components/category-tree-item/category-tree-item.component';
import { SagArticleListComponent } from './pages/article-list/article-list.component';
import { SagArticleListGroupComponent } from './components/article-group/article-group.component';
import { SagArticleListOlyslagerPopupComponent } from './components/article-olyslager-popup/article-olyslager-popup.component';
import { SagArticleListHeaderComponent } from './components/article-list-header/article-list-header.component';
import { SagArticleListNonGroupComponent } from './components/article-non-group/article-non-group.component';
import { SagArticleListMerkmaleFilterBadgeComponent } from './components/merkmale-filter-badge/merkmale-filter-badge.component';
import { SagArticleListQuickClickComponent } from './pages/quick-click/quick-click.component';
import { HaynesProIntegrationService, SagHaynesProModule } from 'sag-haynespro';
import { ArticleHaynesProIntegrationImpl } from './services/article-haynespro-integration-impl.service';
import { ArticleDetailConfigServiceImpl } from './services/article-detail-config-impl.service';
import { VehicleSearchService } from './services/vehicle-search.service';
import { OilService } from './services/oil.service';
import { SagArticleListCateOilOptionComponent } from './components/article-olyslager-popup/cate-oil-option/cate-oil-option.component';
import { SagCustomPricingModule } from 'sag-custom-pricing';
import { BrandPriorityAvailService } from './services/brand-priority-avail.service';
import { SagArticleListBrandFilterPipe } from './pipes/brand-filter.pipe';
import { SagArticleListBrandFilterComponent } from './components/article-brand-filter/article-brand-filter.component';
import { SagShowHideNonAvailPipe } from './pipes/show-hide-non-available.pipe';
import { SagCustomFavoriteListComponent } from './components/custom-favorite-list/custom-favorite-list.component';
import { SagArticleListMerkmaleBrandFilterComponent } from './components/merkmale-brand-filter/merkmale-brand-filter.component';
import { SagArticleListIntegrationService } from './services/article-list-integration.service';

@NgModule({
    declarations: [
        SagArticleListCategoryTreeComponent,
        SagArticleListCategoryTreeSearchComponent,
        SagArticleListCategoryTreeItemComponent,
        SagArticleListComponent,
        SagArticleListGroupComponent,
        SagArticleListOlyslagerPopupComponent,
        SagArticleListCateOilOptionComponent,
        SagArticleListHeaderComponent,
        SagArticleListNonGroupComponent,
        SagArticleListMerkmaleFilterComponent,
        SagArticleListMerkmaleFilterBadgeComponent,
        SagArticleListQuickClickComponent,
        SagArticleListNonMerkmaleAttributesComponent,
        SagArticleListNonMerkmaleFilterItemComponent,
        SagArticleListNonMerkmaleListComponent,
        SagArticleListNonMerkmaleFilterComponent,
        SagArticleListClassicCategoryComponent,
        SagArticleListClassicCategoryItemsComponent,
        SagArticleListClassicCategoryColumnPipe,
        SagArticleListClassicCategoryItemsPipe,
        SagArticleListClassicCategoryLeafPipe,
        SagArticleListShoppingGroupComponent,
        SagArticleListGaidGroupComponent,
        SagArticleListBrandFilterComponent,
        SagArticleListBrandFilterPipe,
        SagShowHideNonAvailPipe,
        SagCustomFavoriteListComponent,
        SagArticleListMerkmaleBrandFilterComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        TreeModule.forRoot(),
        PopoverModule.forRoot(),
        NgSelectModule,
        FormsModule,
        ReactiveFormsModule,
        CollapseModule.forRoot(),
        AngularMyDatePickerModule,
        SagHaynesProModule,
        SagArticleDetailModule,
        SagCommonModule,
        SagCustomPricingModule
    ],
    entryComponents: [
        SagArticleListOlyslagerPopupComponent
    ],
    exports: [
        SagArticleListCategoryTreeComponent,
        SagArticleListComponent,
        SagArticleListMerkmaleFilterComponent,
        SagArticleListQuickClickComponent,
        SagArticleListCategoryTreeSearchComponent,
        SagArticleListNonMerkmaleFilterComponent,
        SagArticleListClassicCategoryComponent,
        SagArticleListHeaderComponent,
        SagArticleListNonGroupComponent,
        SagHaynesProModule,
        SagShowHideNonAvailPipe,
        SagCustomFavoriteListComponent
    ]
})
export class SagArticleListModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagArticleListModule,
            providers: [
                OilService,
                MultiLevelCategoryTreeService,
                ArticleListStorageService,
                BroadcastService,
                QuickClickService,
                CategoryTreeService,
                ArticleGroupSortService,
                ClassicCategoryService,
                VehicleSearchService,
                ArticleHaynesProIntegrationImpl,
                ArticleDetailConfigServiceImpl,
                { provide: ArticleDetailConfigService, useClass: ArticleDetailConfigServiceImpl },
                { provide: HaynesProIntegrationService, useClass: ArticleHaynesProIntegrationImpl },
                BrandPriorityAvailService,
                SagArticleListIntegrationService
            ]
        };
    }
}
