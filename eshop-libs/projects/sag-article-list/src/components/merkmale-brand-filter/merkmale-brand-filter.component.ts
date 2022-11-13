import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'sag-article-list-merkmale-brand-filter',
    templateUrl: './merkmale-brand-filter.component.html',
    styleUrls: ['./merkmale-brand-filter.component.scss']
})
export class SagArticleListMerkmaleBrandFilterComponent implements OnInit {

    @Input() filterTitle: string;
    @Input() set filterData(vals: any[]) {
        this.filterItems = vals ? vals.map(val => ({
            id: val.id,
            name: val.description,
            isChecked: false
        })) : [];
    }

    @Output() additionalFilterEmitter = new EventEmitter();

    filterItems: any[] = [];
    isCollapsed = false;

    constructor() { }

    ngOnInit() {
    }

    changeFilterValue(event: any, selectedFilter: any) {
        const isChecked = event.currentTarget.checked;
        this.additionalFilterEmitter.emit({ isChecked, filterId: selectedFilter.id, type: this.filterTitle });
    }

}
