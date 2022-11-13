import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BulbFilter } from '../../models/bulb-filter.model';
import { BulbsModel } from '../../models/bulbs.model';

@Component({
    selector: 'connect-bulb-filters',
    templateUrl: './bulb-filters.component.html',
    styleUrls: ['./bulb-filters.component.scss']
})
export class BulbFiltersComponent implements OnInit {

    @Input() data: BulbsModel;
    @Input() models: BulbFilter;
    @Input() loading: boolean;

    @Output() updateFilter = new EventEmitter<BulbFilter>();
    @Output() resetFilter = new EventEmitter();
    @Output() search = new EventEmitter<BulbFilter>();

    constructor() { }

    ngOnInit() {

    }

    onUpdateFilter() {
        this.updateFilter.emit(this.models);
    }

    onSearch() {
        this.search.emit(this.models);
    }

    onReset() {
        this.resetFilter.emit();
    }
}
