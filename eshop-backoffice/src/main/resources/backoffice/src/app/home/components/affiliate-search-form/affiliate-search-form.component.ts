import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';
@Component({
    selector: 'backoffice-affiliate-search-form',
    templateUrl: './affiliate-search-form.component.html',
    styleUrls: ['./affiliate-search-form.component.scss'],
})
export class AffiliateSearchFormComponent implements OnInit {
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
        });
    }

    onSubmit() {
        this.submit.emit(this.form.controls.affiliate.value);
    }
}
