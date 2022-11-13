import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { OffersService } from '../../services/offers.services';
import { TranslateService } from '@ngx-translate/core';
import { OfferPerson } from '../../models/offer-person.model';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-offer-person-form-modal',
    templateUrl: './offer-person-form-modal.component.html',
    styleUrls: ['./offer-person-form-modal.component.scss']
})

export class OfferPersonFormModalComponent implements OnInit {
    canBack: boolean;
    callback: any;
    editCustomer: OfferPerson;
    spinnerSelector = '.modal-content';

    public formCustomer = new FormGroup({});
    public customerSalutations: any;

    // value company selected dropdown
    public COMPANY_DROPDOWN_VALUE = 'GENERAL_SALUTATION_COMPANY';

    title: string;

    constructor(
        public modalRef: BsModalRef,
        private offersService: OffersService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        if (!this.editCustomer) {
            this.editCustomer = new OfferPerson();
        }
        this.formCustomer = new FormGroup({
            id: new FormControl(this.editCustomer.id),
            salutation: new FormControl(this.editCustomer.salutation.toString() || null),
            companyName: new FormControl(this.editCustomer.companyName),
            firstName: new FormControl(this.editCustomer.firstName, [Validators.required]),
            lastName: new FormControl(this.editCustomer.lastName, [Validators.required]),
            road: new FormControl(this.editCustomer.road),
            additionalAddress1: new FormControl(this.editCustomer.additionalAddress1),
            additionalAddress2: new FormControl(this.editCustomer.additionalAddress2),
            postCode: new FormControl(this.editCustomer.postCode, [Validators.required]),
            place: new FormControl(this.editCustomer.place, [Validators.required]),
            phone: new FormControl(this.editCustomer.phone),
            fax: new FormControl(this.editCustomer.fax),
            email: new FormControl(this.editCustomer.email)
        });
        this.title = this.isCreatingMode() ? 'OFFERS.OFFER_CUSTOMER.NEW_CUSTOMER_TITLE' : 'OFFERS.OFFER_CUSTOMER.EDIT_CUSTOMER_TITLE';
        this.offersService.getSalutations().subscribe(
            res => {
                if (!res) {
                    return;
                }
                const arr = [];
                res.forEach(element => {
                    arr.push(
                        {
                            value: element,
                            label: this.translate.instant('SETTINGS.PROFILE.SALUTATION.' + element)
                        }
                    );
                });
                this.customerSalutations = arr;
            }
        );
    }

    onSubmit({ value, valid }: { value: OfferPerson, valid: boolean }) {
        if (valid) {
            SpinnerService.start(this.spinnerSelector);
            if (!value.salutation) {
                value.salutation = '';
            }
            const api = value.id ? this.offersService.updateEndCustomerPerson(value) :
                this.offersService.createEndCustomer(value);

            api.pipe(finalize(() => {
                SpinnerService.stop(this.spinnerSelector);
            })).subscribe(
                () => {
                    if (this.callback) {
                        this.callback();
                    }
                    this.modalRef.hide();
                },
                () => {
                    console.log('Can not create end customer');
                });
        }
    }

    backToPreviousModel() {
        this.modalRef.hide();
        this.callback();
    }

    onSalutationChanged() {
        if (this.formCustomer.controls.salutation.value === this.COMPANY_DROPDOWN_VALUE) {
            this.formCustomer.controls.companyName.setValidators([Validators.required]);
            this.formCustomer.controls.companyName.updateValueAndValidity();
        } else {
            this.formCustomer.controls.companyName.setValidators(null);
            this.formCustomer.controls.companyName.setValue('');
        }

    }

    private isCreatingMode() {
        return !this.editCustomer.id;
    }
}
