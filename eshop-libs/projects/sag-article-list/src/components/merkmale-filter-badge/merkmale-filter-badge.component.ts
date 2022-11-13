import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MultiLevelSelectedFilter } from '../../models/multi-level-selected-filter.model';
import { MultiLevelCategoryTreeService } from '../../services/multi-level-category-tree.service';

@Component({
    selector: 'sag-article-list-merkmale-filter-badge',
    templateUrl: './merkmale-filter-badge.component.html',
    styleUrls: ['./merkmale-filter-badge.component.scss']
})
export class SagArticleListMerkmaleFilterBadgeComponent implements OnInit {
    @Input() filterDataOnBadge: MultiLevelSelectedFilter;

    @Output() deselectValueEmitter = new EventEmitter<any>();

    constructor(private merkmaleService: MultiLevelCategoryTreeService) { }

    ngOnInit() {
    }

    deselectValue(val) {
        this.filterDataOnBadge.criteriaValueIds.delete(val.key);
        if (!this.filterDataOnBadge.criteriaValueIds.size) {
            this.filterDataOnBadge.categoryName = null;
        }
        this.deselectValueEmitter.emit(val);
    }

    clearAllFilterValues() {
        this.merkmaleService.setClearAllMerkmaleValues(this.filterDataOnBadge);
        this.filterDataOnBadge.criteriaValueIds = new Map();
    }

}
