import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OffersService } from '../../services/offers.services';
import { Subscription } from 'rxjs';
import { SagTableControl, SagTableColumn } from 'sag-table';
import { Constant } from 'src/app/core/conts/app.constant';
import { OfferPerson } from '../../models/offer-person.model';
import { finalize } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SagConfirmationBoxComponent } from 'sag-common';
import { OfferPersonFormModalComponent } from '../../components/offer-person-form-modal/offer-person-form-modal.component';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-offer-customers',
    templateUrl: 'offer-customers.component.html',
    styleUrls: ['offer-customers.component.scss']
})
export class OfferCustomersComponent implements OnInit, OnDestroy, SagTableControl {
    titleSub: Subscription;

    persons: OfferPerson[];
    criteria = {
        name: '',
        address: '',
        contactInfo: '',
        orderDescByName: null,
        orderDescByAddress: null,
        orderDescByContactInfo: null
    };

    columns = [];
    sortKeys = {
        name: 'orderDescByName',
        address: 'orderDescByAddress',
        contactInfo: 'orderDescByContactInfo'
    };

    notFoundText = '';

    private currentPage = 0;
    private callback: any;

    readonly spinnerSelector = '.offer-customers-table';

    @ViewChild('colName', { static: true }) colName: TemplateRef<any>;
    @ViewChild('colAddress', { static: true }) colAddress: TemplateRef<any>;
    @ViewChild('colContact', { static: true }) colContact: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

    constructor(
        private activatedRoute: ActivatedRoute,
        private modalService: BsModalService,
        private offersService: OffersService
    ) {
        this.titleSub = this.activatedRoute.data.subscribe(({ title }) => {
            this.offersService.title.next(title);
        });
    }

    ngOnInit() {
        this.buildColumns();
    }

    ngOnDestroy() {
        this.titleSub.unsubscribe();
    }

    buildColumns() {
        this.columns = [
            {
                id: 'name',
                i18n: 'COMMON_LABEL.NAME',
                filterable: true,
                sortable: true,
                cellTemplate: this.colName,
                cellClass: 'align-middle',
                width: '30%'
            },
            {
                id: 'address',
                i18n: 'COMMON_LABEL.ADDRESS',
                filterable: true,
                sortable: true,
                cellTemplate: this.colAddress,
                cellClass: 'align-middle',
                width: '30%'
            },
            {
                id: 'contact',
                i18n: 'COMMON_LABEL.CONTACT_INFO',
                filterable: true,
                sortable: true,
                cellTemplate: this.colContact,
                cellClass: 'align-middle',
                width: '30%'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
                cellClass: 'align-middle'
            }
        ] as SagTableColumn[];
    }

    searchTableData(data) {
        this.currentPage = data.request.page - 1;
        this.criteria = data.request.filter;
        this.callback = data.callback;
        if (data.request.sort && data.request.sort.field) {
            this.sort(this.sortKeys[data.request.sort.field], data.request.sort.direction === 'asc');
        }

        this.getCustomers();
    }

    getCustomers() {
        return this.offersService.getPersons(this.currentPage, this.criteria)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe((res) => {
                if (!res) {
                    return;
                }
                if (this.callback) {
                    this.callback({
                        rows: res.persons,
                        totalItems: res.total,
                        itemsPerPage: res.size
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

    sort(column: string, value) {
        this.resetSort();
        this.criteria[column] = value;
    }

    resetSort() {
        this.criteria.orderDescByName = null;
        this.criteria.orderDescByAddress = null;
        this.criteria.orderDescByContactInfo = null;
    }

    createPerson() {
        this.modalService.show(OfferPersonFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                callback: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.getCustomers();
                        sub.unsubscribe();
                    });
                }
            }
        });
    }

    editCustomer(customer: OfferPerson) {
        this.offersService.getEndCustomerPerson(customer.id).subscribe(res => {
            this.modalService.show(OfferPersonFormModalComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    editCustomer: res,
                    callback: () => {
                        const sub = this.modalService.onHidden.subscribe(() => {
                            SpinnerService.start(this.spinnerSelector);
                            this.getCustomers();
                            sub.unsubscribe();
                        });
                    }
                }
            });
        });

    }

    deleteCustomer(customer: OfferPerson) {
        this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: '',
                message: 'OFFERS.OFFER_CUSTOMER.DELETE_MESSAGE',
                messageParams: { param: `${customer.id} - ${customer.firstName} ${customer.lastName}` },
                okButton: 'OFFERS.OFFER_CUSTOMER.YES',
                cancelButton: 'OFFERS.OFFER_CUSTOMER.NO',
                bodyIcon: 'fa-exclamation-triangle',
                close: () => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        this.offersService.deleteEndCustomerPerson(customer.id).subscribe(() => {
                            this.getCustomers();
                        });
                        sub.unsubscribe();
                    });
                }
            }
        });
    }
}
