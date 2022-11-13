import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';
@Component({
    selector: 'backoffice-user-search-form',
    templateUrl: './user-search-form.component.html',
    styleUrls: ['./user-search-form.component.scss'],
})
export class UserSearchFormComponent implements OnInit {
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
            customerNumber: [''],
            userName: [''],
            email: [''],
            telephone: [''],
            name: [''],
        });
    }

    onSubmit() {
        this.submit.emit({
            affiliate: this.form.controls.affiliate.value,
            customerNumber: this.form.controls.customerNumber.value,
            userName: this.form.controls.userName.value,
            email: this.form.controls.email.value,
            telephone: this.form.controls.telephone.value,
            name: this.form.controls.name.value,
        });
    }
}
