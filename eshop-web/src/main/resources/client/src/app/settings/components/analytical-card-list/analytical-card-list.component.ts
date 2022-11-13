import { Component, OnInit, Input, ViewChild, TemplateRef, Output, EventEmitter, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';
import { SagTableColumn, SagTableControl, SagTableRequestModel, SagTableResponseModel, TablePage } from 'sag-table';

import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AnalyticalCardItem } from '../../models/analytical-card/analytical-card-item.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AnalyticalCardFilter } from '../../models/analytical-card/analytical-card-filter.model';
import { AnalyticalCardService } from '../../services/analytical-card.service';
import { ANALYTICAL_CARD_STATUS } from '../../enums/analytical-card/analytical-card.enum';
import { BehaviorSubject, Subject } from 'rxjs';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-analytical-card-list',
    templateUrl: './analytical-card-list.component.html',
    styleUrls: ['./analytical-card-list.component.scss']
})
export class AnalyticalCardListComponent implements OnInit, OnDestroy, SagTableControl {
    @ViewChild('viewDetailTemplate', { static: true }) viewDetailTemplate: TemplateRef<any>;
    @ViewChild('statusTemplate', { static: true }) statusTemplate: TemplateRef<any>;

    @Input() filterEvent: BehaviorSubject<AnalyticalCardFilter>;
    
    @Output() onCompleted = new EventEmitter();

    rows = [];
    columns: SagTableColumn[] = [];
    sort = {};
    page = new TablePage();
    searchModel;

    STATUS = ANALYTICAL_CARD_STATUS;

    subs = new SubSink();

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private analyticalCardService: AnalyticalCardService
    ) { }

    ngOnInit() {
        this.columns = this.buildTableColumns();
        this.subs.sink = this.filterEvent.subscribe(filter => {
            if (filter) {
                const request = filter.request();
                this.page.currentPage = request.page;
                this.searchModel = request;
            }
        })
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void; }): void {
        const filter = new AnalyticalCardFilter().sort({
            field: request.sort.field,
            direction: request.sort.direction
        });
        request.filter.page = request.page - 1;
        request.filter.sorting = filter.sorting;
        this.searchModel.sorting = filter.sorting;
        const spinner = SpinnerService.start(`connect-analytical-card div.${request.filter.paymentMethod}`);
        this.analyticalCardService.searchAnalyticalCard(request.filter).pipe(
            finalize(() => {
                this.onCompleted.emit();
                SpinnerService.stop(spinner);
            })
        ).subscribe((res: any) => {
            if (!res) {
                callback({
                    rows: [],
                    totalItems: 0
                });
                return;
            }
            this.rows = (res && res.content || []).map(item => new AnalyticalCardItem(item));
            callback({
                rows: this.rows,
                totalItems: res.totalElements,
                itemsPerPage: 20,
                page: res.number + 1
            });
        });
    }

    onViewCardDetail(item: AnalyticalCardItem) {
        const request = this.searchModel;
        this.router.navigate(['detail'], {
            queryParams: {
                documentNr: encodeURIComponent(item.documentNr),
                documentType: item.documentType,
                paymentMethod: item.paymentMethod,
                documentStatus: item.status,
                orderStatus: request.status,
                dateFrom: request.dateFrom,
                dateTo: request.dateTo,
                page: this.page.currentPage,
                sorting: request.sorting
            },
            relativeTo: this.route
        });
    }

    private buildTableColumns() {
        return [
            {
                id: 'documentType',
                sortKey: 'dateNumber',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.DOCUMENT_TYPE',
                filterable: false,
                sortable: false,
                width: '120px'
            },
            {
                id: 'documentNr',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.DOCUMENT_NO',
                filterable: false,
                sortable: false,
                width: '160px'
            },
            {
                id: 'webOrderNr',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.ORDER_NO',
                filterable: false,
                sortable: false,
                width: '160px'
            },
            {
                id: 'postingDate',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.POSTING_DATE',
                filterable: false,
                sortable: true,
                type: 'date',
                width: '100px'
            },
            {
                id: 'dueDate',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.DUE_DATE',
                filterable: false,
                sortable: true,
                type: 'date',
                width: '100px'
            },
            {
                id: 'paymentDeadlineNotification',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.PAYMENT_DEADLINE',
                filterable: false,
                sortable: false,
                width: '200px',
                cellClass: 'text-danger'
            },
            {
                id: 'status',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.STATUS',
                filterable: false,
                sortable: false,
                width: '50px',
                headerClass: 'text-center',
                cellClass: 'text-center',
                cellTemplate: this.statusTemplate
            },
            {
                id: 'remainingAmount',
                i18n: 'SETTINGS.ANALYTICAL_CARD.HEADER.REMAINING_AMOUNT',
                filterable: false,
                sortable: false,
                width: '120px',
                type: 'currency'
            }
        ] as SagTableColumn[];
    }
}
