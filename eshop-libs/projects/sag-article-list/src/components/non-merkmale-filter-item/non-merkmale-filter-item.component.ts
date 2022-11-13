import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Component({
    selector: 'sag-article-list-non-merkmale-filter-item',
    templateUrl: './non-merkmale-filter-item.component.html',
    styleUrls: ['./non-merkmale-filter-item.component.scss']
})
export class SagArticleListNonMerkmaleFilterItemComponent implements OnInit {
    @Input() filterTitle: string;
    @Input() set filterData(vals: any[]) {
        this.filterItems = vals ? vals.map(val => ({
            id: val.id,
            name: val.description,
            isChecked: false
        })) : [];
    }
    @Input() resetAllValues: Observable<boolean>;

    @Output() additionalFilterEmitter = new EventEmitter();

    @Input() isCollapsed = false;

    filterItems: any[] = [];

    constructor() { }

    ngOnInit() {
        if (this.resetAllValues) {
            this.resetAllValues.subscribe(reset => {
                this.filterItems = this.filterItems.map(item => ({ id: item.id, name: item.name, isChecked: false }));
            });
        }
    }

    changeFilterValue(event: any, selectedFilter: any) {
        const isChecked = event.currentTarget.checked;
        this.additionalFilterEmitter.emit({ isChecked, filterId: selectedFilter.id, type: this.filterTitle });
    }
}
