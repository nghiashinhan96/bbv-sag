import { Component, OnInit, ViewChild, TemplateRef, OnDestroy } from '@angular/core';
import { OffersResponse } from '../../models/offers-response.model';
import { OfferCriteria } from '../../models/offer-criteria.model';
import { OFFER_STATUS } from '../../enums/offers.enum';
import { NgOption } from '@ng-select/ng-select';
import { Constant } from 'src/app/core/conts/app.constant';
import { Offer } from '../../models/offer.model';
import { OffersService } from '../../services/offers.services';
import { finalize } from 'rxjs/operators';
import { SagTableColumn, SagTableControl } from 'sag-table';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SagConfirmationBoxComponent, GrossPriceKeyPipe } from 'sag-common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-offer-list',
    templateUrl: './offer-list.component.html',
    styleUrls: ['./offer-list.component.scss']
})
export class OfferListComponent implements OnInit, OnDestroy, SagTableControl {
    criteria = new OfferCriteria();

    offerStatusOptions: NgOption[] = [];
    selectedOfferStatus: string;

    oldColumns = [];
    columns = [];
    sortKeys = {
        offerNumber: 'orderDescByOfferNumber',
        customerName: 'orderDescByCustomerName',
        remark: 'orderDescByRemark',
        vehicleDesc: 'orderDescByVehicleDesc',
        offerDate: 'orderDescByOfferDate',
        price: 'orderDescByPrice',
        status: 'orderDescByStatus',
        username: 'orderDescByUsername'
    };

    data: OffersResponse = new OffersResponse();

    private currentPage = 0;
    private pageSize = Constant.DEFAULT_PAGE_SIZE;
    private callback: any;
    titleSub: Subscription;
    notFoundText = '';
    readonly spinnerSelector = '.offers-table';

    @ViewChild('colVehicleDescs', { static: true }) colVehicleDescsRef: TemplateRef<any>;
    @ViewChild('colStatus', { static: true }) colStatusRef: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActionsRef: TemplateRef<any>;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private modalService: BsModalService,
        private grossPriceKey: GrossPriceKeyPipe,
        private offersService: OffersService
    ) {
        this.titleSub = this.activatedRoute.data.subscribe(({ title }) => {
            this.offersService.title.next(title);
        });
    }

    ngOnInit() {
        this.buildOfferColumns();
    }

    ngOnDestroy() {
        this.titleSub.unsubscribe();
    }

    buildOfferColumns() {
        this.columns = [
            {
                id: 'offerNumber',
                i18n: 'COMMON_LABEL.NUMBER',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '105px'
            },
            {
                id: 'customerName',
                i18n: 'COMMON_LABEL.CUSTOMER',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '105px'
            },
            {
                id: 'remark',
                i18n: 'COMMON_LABEL.REMARKS',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '120px'
            },
            {
                id: 'vehicleDesc',
                i18n: 'COMMON_LABEL.VEHICLES',
                filterable: true,
                sortable: true,
                cellTemplate: this.colVehicleDescsRef,
                cellClass: 'align-middle',
                width: '160px'
            },
            {
                id: 'offerDate',
                type: 'date',
                i18n: 'COMMON_LABEL.DATE',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '90px'
            },
            {
                id: 'price',
                i18n: this.grossPriceKey.transform(environment.affiliate),
                filterable: true,
                sortable: true,
                width: '100px',
                type: 'currency'
            },
            {
                id: 'status',
                i18n: 'COMMON_LABEL.STATUS',
                type: 'select',
                selectSource: this.buildOfferStatusOptions(),
                selectValue: 'value',
                selectLabel: 'label',
                defaultValue: Constant.SPACE,
                filterable: true,
                sortable: true,
                cellTemplate: this.colStatusRef,
                cellClass: 'align-middle',
                width: '105px'
            },
            {
                id: 'username',
                i18n: 'COMMON_LABEL.USER',
                filterable: true,
                sortable: true,
                cellClass: 'align-middle',
                width: '90px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActionsRef,
                cellClass: 'align-middle'
            }
        ] as SagTableColumn[];
    }

    searchTableData(data) {
        this.currentPage = data.request.page - 1;
        this.criteria = new OfferCriteria(data.request.filter);
        this.callback = data.callback;
        if (data.request.sort && data.request.sort.field) {
            this.sort(this.sortKeys[data.request.sort.field], data.request.sort.direction === 'asc');
        }

        this.getOffers();
    }

    getOffers() {
        return this.offersService.getOffers(this.currentPage, this.pageSize, this.criteria)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe((res) => {
                if (!res) {
                    return;
                }
                if (this.callback) {
                    this.callback({
                        rows: res.offers,
                        totalItems: res.totalElements,
                        itemsPerPage: this.pageSize
                    });
                }
            }, (err) => {
                if (this.callback) {
                    this.callback({
                        rows: [],
                        totalItems: 0
                    });
                }
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
            });
    }

    exportOffer(offer: Offer, type: string) {
        this.offersService.exportOfferById(offer.id, String(offer.offerNumber), type);
    }

    sort(column: string, value) {
        this.criteria.resetSort();
        this.criteria[column] = value;
    }

    exportPdfOffer(offer: Offer) {
        this.exportOffer(offer, 'pdf');
    }

    deleteOffer(offer: Offer) {
        this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: '',
                message: 'OFFERS.DELETE_OFFER_MESSAGE',
                messageParams: { param: offer.offerNumber || '' },
                okButton: 'OFFERS.OFFER_CUSTOMER.YES',
                cancelButton: 'OFFERS.OFFER_CUSTOMER.NO',
                bodyIcon: 'fa-exclamation-triangle',
                close: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.offersService.deleteOffer(offer.id).subscribe(() => {
                            this.getOffers();
                        });
                        sub.unsubscribe();
                    });
                }
            }
        });
    }


    public exportWordOffer(offer: Offer) {
        this.exportOffer(offer, 'word');
    }

    public exportRtfOffer(offer: Offer) {
        this.exportOffer(offer, 'rtf');
    }

    public createOffer() {
        SpinnerService.start(this.spinnerSelector)
        this.offersService.createOffer()
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                this.router.navigate(['/offers/edit/' + res.offer.id]);
            });
    }

    private buildOfferStatusOptions() {
        const options = [{
            value: Constant.SPACE,
            label: 'OFFERS.OFFER_DETAIL.OFFER_STATUS.ALL'
        }, {
            value: OFFER_STATUS.OPEN.toString(),
            label: 'OFFERS.OFFER_DETAIL.OFFER_STATUS.OPEN'
        }, {
            value: OFFER_STATUS.ORDERED.toString(),
            label: 'OFFERS.OFFER_DETAIL.OFFER_STATUS.ORDERED'
        }, {
            value: OFFER_STATUS.MANUALLY_ORDERED.toString(),
            label: 'OFFERS.OFFER_DETAIL.OFFER_STATUS.MANUALLY_ORDERED'
        }];

        return options;
    }
}
