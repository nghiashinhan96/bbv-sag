import { Component, EventEmitter, Input, Output } from "@angular/core";
import { sortBy, cloneDeep } from 'lodash';
import { BarFilter } from "sag-article-detail";

import { BrandFilterItem } from "../../models/brand-filter-item.model";
import { BrandFilterUtil } from "../../utils/brand-filter.util";

@Component({
    selector: 'sag-article-list-brand-filter',
    templateUrl: './article-brand-filter.component.html'
})
export class SagArticleListBrandFilterComponent {
    @Input() set brandsData(val: BrandFilterItem[]) {
        this.groupKey = val && val[0] && val[0].key;
        this.brands = sortBy(BrandFilterUtil.getUniqCombinedBrands(val), 'name').map(brand => ({ ...brand }));
    }

    @Input() isFilterByCaId = false;
    @Input() set barFilterOptionData(val: BarFilter[]) {
        this.barFilterOptions = [];
        (val || []).map((item, index) => {
            this.barFilterOptions.push(cloneDeep(item));
        });
    }

    @Output() brandsStateChange = new EventEmitter<BrandFilterItem>();
    @Output() barFilterStateChange = new EventEmitter<BarFilter[]>();
    @Output() close = new EventEmitter();

    isCollapsed = false;
    groupKey: string;
    brands: any[];

    barFilterOptions: BarFilter[];

    constructor() { }

    selectBrand(event: any, brand: any) {
        brand.checked = event.target.checked;
    }

    deselectAllBrands() {
        this.brands.forEach(brand => {
            brand.checked = false;
        });
    }

    onBrandsStateChange() {
        const data = new BrandFilterItem({
            key: this.groupKey,
            brands: this.brands
        });
        this.brandsStateChange.emit(data);
        if (this.isFilterByCaId) {
            this.barFilterStateChange.emit(this.barFilterOptions);
        }
        this.close.emit();
    }

    toggle() {
        this.isCollapsed = !this.isCollapsed;
    }

    onClose() {
        this.close.emit();
    }

    deselectAllFilter(index) {
        if (this.barFilterOptions[index]) {
            (this.barFilterOptions[index].options || []).forEach(op => op.checked = false);
        }
    }

    toggleFilterBar(index) {
        this.barFilterOptions[index].toggle = !this.barFilterOptions[index].toggle ;
    }

    selectBarOption(event: any, filter: any) {
        filter.checked = event.target.checked;
    }
}