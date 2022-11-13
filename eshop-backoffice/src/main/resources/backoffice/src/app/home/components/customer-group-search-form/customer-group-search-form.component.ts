import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';
@Component({
    selector: 'backoffice-customer-group-search-form',
    templateUrl: './customer-group-search-form.component.html',
    styleUrls: ['./customer-group-search-form.component.scss'],
})
export class CustomerGroupSearchFormComponent implements OnInit {
    @Input() affiliateData;

    @Output() submit = new EventEmitter();

    form: FormGroup;
    isFormExpanded: boolean = true;

    constructor(
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        this.form = this.fb.group({
            affiliate: [DEFAULT_SELECTOR_VALUE],
            customerGroupName: [''],
        });
    }

    onSubmit() {
        this.submit.emit({
            affiliate: this.form.controls.affiliate.value,
            customerGroupName: this.form.controls.customerGroupName.value.trim(),
        });
    }
}
