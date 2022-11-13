import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-customer-search-form',
    templateUrl: './customer-search-form.component.html',
    styleUrls: ['./customer-search-form.component.scss'],
})
export class CustomerSearchFormComponent implements OnInit {
    @Input() affiliateData;

    @Output() submit = new EventEmitter();

    form: FormGroup;
    isFormExpanded = true;

    constructor(
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        this.form = this.fb.group({
            affiliate: [DEFAULT_SELECTOR_VALUE],
            customerNr: [''],
            companyName: [''],
        });
    }

    onSubmit() {
        this.submit.emit({
            affiliate: this.form.controls.affiliate.value,
            customerNr: this.form.controls.customerNr.value,
            companyName: this.form.controls.companyName.value,
        });
    }
}
