import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
    selector: 'sag-article-list-non-merkmale-filter-list',
    templateUrl: 'non-merkmale-filter-list.component.html',
    styleUrls: ['non-merkmale-filter-list.component.scss']
})
export class SagArticleListNonMerkmaleListComponent implements OnInit {

    @Input() filterData: any;

    @Output() selectedFilterEmitter = new EventEmitter();

    ngOnInit() { }

    handleAdditionalFilter(additionalFilter: any) {
        this.selectedFilterEmitter.emit(additionalFilter);
    }
}
