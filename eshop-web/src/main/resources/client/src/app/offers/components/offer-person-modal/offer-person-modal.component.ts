import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { OffersService } from '../../services/offers.services';
import { OfferPerson } from '../../models/offer-person.model';
import { SagTableColumn } from 'sag-table';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { Constant } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-offer-person-modal',
    templateUrl: './offer-person-modal.component.html',
    styleUrls: ['./offer-person-modal.component.scss']
})
export class OfferPersonModalComponent implements OnInit {
    createPerson: any;
    selectPerson: any;
    selectedPersonId: any;
    personsLoadStatus = true;

    persons: OfferPerson[];
    filterModels = {
        name: '',
        address: '',
        contactInfo: '',
        orderDescByName: null,
        orderDescByAddress: null,
        orderDescByContactInfo: null
    };
    warningMessage: string;
    successMessage: string;
    creatingCustomer: any;

    columns = [];
    sortKeys = {
        name: 'orderDescByName',
        address: 'orderDescByAddress',
        contactInfo: 'orderDescByContactInfo'
    };
    spinnerSelector = '.modal-content';

    @ViewChild('colRadio', { static: true }) colRadio: TemplateRef<any>;
    @ViewChild('colName', { static: true }) colName: TemplateRef<any>;
    @ViewChild('colAddress', { static: true }) colAddress: TemplateRef<any>;
    @ViewChild('colContact', { static: true }) colContact: TemplateRef<any>;

    constructor(
        public modalRef: BsModalRef,
        private offersService: OffersService
    ) { }

    ngOnInit() {
        this.buildColumns();
    }

    buildColumns() {
        this.columns = [
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colRadio,
                cellClass: 'align-middle py-0',
            },
            {
                id: 'name',
                i18n: 'COMMON_LABEL.NAME',
                filterable: true,
                sortable: true,
                cellTemplate: this.colName,
                cellClass: 'align-middle',
                width: '32%'
            },
            {
                id: 'address',
                i18n: 'COMMON_LABEL.ADDRESS',
                filterable: true,
                sortable: true,
                cellTemplate: this.colAddress,
                cellClass: 'align-middle',
                width: '32%'
            },
            {
                id: 'contact',
                i18n: 'COMMON_LABEL.CONTACT_INFO',
                filterable: true,
                sortable: true,
                cellTemplate: this.colContact,
                cellClass: 'align-middle',
                width: '32%'
            }
        ] as SagTableColumn[];
    }

    searchOfferPersons(data) {
        SpinnerService.start(this.spinnerSelector);
        this.filterModels = data.request.filter;

        if (data.request.sort && data.request.sort.field) {
            this.sort(this.sortKeys[data.request.sort.field], data.request.sort.direction === 'asc');
        }

        this.offersService.getPersons(data.request.page - 1, this.filterModels)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                if (!res) {
                    return;
                }

                this.persons = res.persons;
                // Reset selection
                this.selectedPersonId = null;

                if (data.callback) {
                    data.callback({
                        rows: res.persons,
                        totalItems: res.total,
                        itemsPerPage: res.size
                    });
                }
            }, err => {
                if (data.callback) {
                    data.callback({
                        rows: [],
                        totalItems: 0
                    });
                }
                this.warningMessage =
                    err && err.error && err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
            });
    }

    sort(column: string, value) {
        this.resetSort();
        this.filterModels[column] = value;
    }

    resetSort() {
        this.filterModels.orderDescByName = null;
        this.filterModels.orderDescByAddress = null;
        this.filterModels.orderDescByContactInfo = null;
    }

    showCreatingPersonModal() {
        this.createPerson();
        this.modalRef.hide();
    }

    getSelectedPersonId(personId) {
        this.selectedPersonId = personId;
        this.successMessage = null;
    }

    getSelectedPerson() {
        this.persons.forEach(person => {
            if (person.id === this.selectedPersonId) {
                this.selectPerson(person);
                this.successMessage = 'MESSAGES.ADDED_SUCCESSFULLY';
                setTimeout(() => {
                    this.modalRef.hide();
                }, 1000);
            }
        });
    }
}
