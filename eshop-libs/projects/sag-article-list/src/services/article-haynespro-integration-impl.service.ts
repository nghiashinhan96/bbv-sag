import { Injectable } from '@angular/core';
import { HaynesProIntegration } from 'sag-haynespro';
import { ArticleListConfigService } from './article-list-config.service';
import { ArticleListStorageService } from './article-list-storage.service';
import { CategoryTreeService } from './category-tree.service';
import { CategoryModel } from 'sag-article-detail';
import { Observable } from 'rxjs';
import { VehicleSearchService } from './vehicle-search.service';

@Injectable()
export class ArticleHaynesProIntegrationImpl implements HaynesProIntegration {

    constructor(
        private articleListConfigService: ArticleListConfigService,
        private articleListStorageService: ArticleListStorageService,
        private vehicleSearchService: VehicleSearchService,
        private categoryTreeService: CategoryTreeService
    ) { }

    getVehiclesByVehId(vehicleId: any): Observable<any> {
        return this.vehicleSearchService.getVehiclesByVehId(vehicleId);
    }

    getCategoriesByGaids(gaid): CategoryModel[] {
        return this.categoryTreeService.getCategoriesByGaids(gaid);
    }

    checkOnCategoryTree(categoryIds, isChecked, emitSearchEvent) {
        return this.categoryTreeService.checkOnCategoryTree(categoryIds, isChecked, emitSearchEvent);
    }

    checkOilCate(cateIds, emitSearchEvent) {
        this.categoryTreeService.emitCheckOilCate(
            cateIds,
            emitSearchEvent,
            oils => {
                this.articleListStorageService.selectedOils = oils;
                this.categoryTreeService.checkOnCategoryTree(cateIds, true, false);
                if (emitSearchEvent) {
                    this.categoryTreeService.emitSearchRequest();
                }
            },
            oils => {
                this.articleListStorageService.removeSelectedOil(cateIds);
                this.categoryTreeService.checkOnCategoryTree(cateIds, false, false);
                if (emitSearchEvent) {
                    this.categoryTreeService.emitSearchRequest();
                }
            }
        );
    }

    get spinner() {
        return this.articleListConfigService.spinner;
    }

    set spinner(spinner) {
        this.spinner = spinner;
    }

    get baseUrl() {
        return this.articleListConfigService.baseUrl;
    }

    set baseUrl(baseUrl) {
        this.baseUrl = baseUrl;
    }

}
