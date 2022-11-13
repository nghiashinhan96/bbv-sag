import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';

@Component({
    selector: 'sag-article-list-non-merkmale-filter',
    templateUrl: 'non-merkmale-filter.component.html',
    styleUrls: ['non-merkmale-filter.component.scss']
})
export class SagArticleListNonMerkmaleFilterComponent implements OnInit {

    @Input() attributes: any;
    @Input() filterData: any;

    @Output() additionalFilterEmitter = new EventEmitter();
    @Output() resetEmitter = new EventEmitter();

    isReset$ = new BehaviorSubject<boolean>(false);
    // Default collapse filtering
    TYRE_SEGMENT = 'tyre_segment';
    ngOnInit() { }

    resetFilter() {
        this.isReset$.next(true);
        this.resetEmitter.emit();
    }

    filterArticle(filters: any) {
        this.additionalFilterEmitter.emit(filters);
    }
}
