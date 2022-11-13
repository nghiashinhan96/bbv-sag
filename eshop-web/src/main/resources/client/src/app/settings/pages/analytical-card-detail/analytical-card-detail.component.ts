import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { finalize } from "rxjs/operators";
import { SagTableColumn, SagTableRequestModel, SagTableResponseModel, TablePage } from "sag-table";
import { SpinnerService } from "src/app/core/utils/spinner";
import { ANALYTICAL_CARD_PAYMENT } from "../../enums/analytical-card/analytical-card.enum";
import { AnalyticalCardDetail } from "../../models/analytical-card/analytical-card-detail.model";
import { AnalyticalCardService } from "../../services/analytical-card.service";

@Component({
    selector: 'connect-analytical-card-detail',
    templateUrl: './analytical-card-detail.component.html',
    styleUrls: ['./analytical-card-detail.component.scss']
})
export class AnalyticalCardDetailComponent implements OnInit {
    paymentMethod: ANALYTICAL_CARD_PAYMENT;
    detail: AnalyticalCardDetail;
    columns: SagTableColumn[];
    PAYMENT = ANALYTICAL_CARD_PAYMENT;

    searchModel = {};
    sort = {};
    page = new TablePage();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private analyticalCardService: AnalyticalCardService
    ) { }

    ngOnInit() {
        const params = this.activatedRoute.snapshot.queryParams;
        this.paymentMethod = params.paymentMethod;
        this.columns = this.buildTableColumns();
    }

    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void; }): void {
        const params = this.activatedRoute.snapshot.queryParams;
        const req = {
            documentType: params.documentType,
            paymentMethod: params.paymentMethod,
            status: params.documentStatus,
            page: request.page - 1
        };
        this.analyticalCardService.getAnalyticalCardDetail(params.documentNr, req).subscribe((res: any) => {
            if (!res) {
                callback({
                    rows: [],
                    totalItems: 0
                });
                return;
            }
            this.detail = new AnalyticalCardDetail(res);
            callback({
                rows: this.detail.entryLines,
                totalItems: this.detail.entryLinesNo,
                itemsPerPage: 20,
                page: res.number + 1
            });
        });
    }

    backToList() {
        const params = this.activatedRoute.snapshot.queryParams;
        const queryParams = {
            paymentMethod: params.paymentMethod,
            status: params.orderStatus,
            dateFrom: params.dateFrom,
            dateTo: params.dateTo,
            page: params.page,
            sorting: params.sorting
        };
        this.router.navigate(['settings/analytical-card'], { queryParams });
    }

    private buildTableColumns() {
        let columns = [
            {
                id: 'type',
                sortKey: 'dateNumber',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.TYPE',
                filterable: false,
                sortable: false,
                width: '120px'
            },
            {
                id: 'nr',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.NUMBER',
                filterable: false,
                sortable: false,
                width: '180px'
            },
            {
                id: 'description',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.DESCRIPTION',
                filterable: false,
                sortable: false,
                width: '240px'
            },
            {
                id: 'uoM',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.UOM',
                filterable: false,
                sortable: false,
                width: '100px'
            },
            {
                id: 'quantity',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.AMOUNT',
                filterable: false,
                sortable: false,
                width: '80px',
                cellClass: "text-center",
                class: "text-center"
            },
            {
                id: 'unitPrice',
                i18n: 'SETTINGS.ANALYTICAL_CARD.LINE_HEADER.PRICE',
                filterable: false,
                sortable: false,
                width: '120px',
                type: 'currency'
            },
            {
                id: 'amountInclVAT',
                i18n: 'SETTINGS.ANALYTICAL_CARD.TOTAL_WITH_VAT',
                filterable: false,
                sortable: false,
                width: '120px',
                type: 'currency'
            }
        ] as SagTableColumn[];

        return columns;
    }
}