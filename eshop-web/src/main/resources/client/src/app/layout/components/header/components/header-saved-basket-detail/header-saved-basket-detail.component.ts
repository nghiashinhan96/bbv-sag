import { Component, OnInit, Input, ViewChild, TemplateRef, AfterViewInit, ElementRef, Inject } from '@angular/core';
import { SagTableColumn } from 'sag-table';
import { BasketHistoryArticleModel } from '../../models/basket-history-article.model';
import { PageScrollService } from 'ngx-page-scroll-core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'connect-header-saved-basket-detail',
    templateUrl: './header-saved-basket-detail.component.html',
    styleUrls: ['./header-saved-basket-detail.component.scss']
})
export class HeaderSavedBasketDetailComponent implements OnInit, AfterViewInit {
    @Input() data: BasketHistoryArticleModel[];
    columns: SagTableColumn[];
    @ViewChild('descRef', { static: true }) descRef: TemplateRef<any>;
    @ViewChild('manufacturerRef', { static: true }) manufacturerRef: TemplateRef<any>;

    manufacturerSuffix = 'm';

    constructor(
        private pageScrollService: PageScrollService,
        @Inject(DOCUMENT) private document: any,
        private el: ElementRef
    ) { }

    ngAfterViewInit(): void {
        this.pageScrollService.scroll({
            document: this.document,
            scrollTarget: this.el.nativeElement,
            scrollInView: true,
            scrollViews: [document.querySelector('.modal')],
            duration: 400
        });
    }

    ngOnInit() {
        this.columns = [
            {
                i18n: 'ARTICLE.NUMBER',
                filterable: false,
                id: 'amountNumber',
                sortable: true,
                cellClass: 'align-middle'
            },
            {
                id: 'articleNr',
                i18n: 'COMMON_LABEL.COLUMNS.ARTICLE_NR',
                sortable: true,
                filterable: false,
                cellClass: 'align-middle'
            },
            {
                id: 'info',
                i18n: 'COMMON_LABEL.COLUMNS.INFO',
                sortable: true,
                filterable: false,
                cellTemplate: this.descRef,
                width: '260px',
                cellClass: 'align-middle'
            },
            {
                id: 'manufacturer',
                i18n: 'BASKET_HISTORY.PLACE_HOLDERS.MANUFACTURER',
                sortable: true,
                filterable: false,
                cellTemplate: this.manufacturerRef,
                cellClass: 'align-middle article-detail-manufacturer'
            },
            {
                id: 'vehicle',
                i18n: 'COMMON_LABEL.COLUMNS.VEHICLE',
                sortable: true,
                filterable: false,
                cellClass: 'align-middle'
            }
        ];
    }


}
