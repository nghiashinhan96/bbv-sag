import { OnInit, Component, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SagTableControl, SagTableColumn, TablePage } from 'sag-table';
import { ERROR_NOT_FOUND } from 'src/app/core/conts/app.constant';
import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { AffiliateInfoModel } from '../../models/affiliate-info.model';


@Component({
    selector: 'backoffice-affiliate-results',
    templateUrl: './affiliate-results.component.html',
})
export class AffiliateResultsComponent implements OnInit, SagTableControl {
    public affShortName: string;
    public affiliates: Array<AffiliateInfoModel>;

    columns = [];
    page = new TablePage();
    searchModel = {};
    tableCallback: any;
    tableRequest: any;

    notFoundText = '';

    readonly spinnerSelector = '.affiliates-table';

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private affService: AffiliateService
    ) { }

    ngOnInit(): void {
        this.getInputData();
        this.buildAffiliateColumn();
    }

    buildAffiliateColumn() {
        this.columns = [
            {
                id: 'name',
                i18n: 'HOME.LABEL.AFFILIATE_SEARCH.NAME',
                filterable: false,
                sortable: false,
                width: '20%',
            },
            {
                id: 'shortName',
                i18n: 'HOME.LABEL.AFFILIATE_SEARCH.SHORT_NAME',
                filterable: false,
                sortable: false,
                width: '20%',
            },
            {
                id: 'orgCode',
                i18n: 'HOME.LABEL.AFFILIATE_SEARCH.ORG_CODE',
                filterable: false,
                sortable: false,
                width: '20%',
            },
            {
                id: 'description',
                i18n: 'HOME.LABEL.AFFILIATE_SEARCH.DESCRIPTION',
                filterable: false,
                sortable: false,
                width: '30%',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                width: '10%',
                cellTemplate: this.colActions,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    searchTableData({ request, callback }): void {
        // SpinnerService.start(this.spinnerSelector);
        this.tableRequest = request;
        this.tableCallback = callback;
        return;
    }

    edit(aff) {
        this.router.navigate(['detail'], {
            queryParams: { affShortName: aff.shortName },
            relativeTo: this.route,
        });
    }

    private getInputData() {
        this.route.queryParams.subscribe(({ affShortName }) => {
            this.affShortName = affShortName;
            this.affService.getInfos(this.affShortName).subscribe(
                (data) => {
                    const res: any = data;
                    this.affiliates = res.map(
                        (item) => {
                            return new AffiliateInfoModel(item);
                        });

                    if (this.tableCallback) {
                        this.tableCallback({
                            rows: this.affiliates,
                            totalItems: this.affiliates.length,
                            itemsPerPage: this.affiliates.length,
                        });
                    }
                },
                (err) => {
                    if (this.tableCallback) {
                        this.tableCallback({
                            rows: [],
                            totalItems: 0,
                        });
                    }
                    this.notFoundText =
                        err.error.code === ERROR_NOT_FOUND
                            ? 'MESSAGES.NO_RESULTS_FOUND'
                            : 'MESSAGES.GENERAL_ERROR';
                }
            );
        });
    }
}
