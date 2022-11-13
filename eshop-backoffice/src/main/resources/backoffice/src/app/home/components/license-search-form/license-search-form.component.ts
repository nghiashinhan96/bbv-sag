import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { cloneDeep } from 'lodash';
import { IMyDateModel } from 'angular-mydatepicker';
import { DateUtil } from 'src/app/core/utils/date.util';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { TranslateService } from '@ngx-translate/core';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';
import * as moment from 'moment';

@Component({
    selector: 'backoffice-license-search-form',
    templateUrl: './license-search-form.component.html',
    styleUrls: ['./license-search-form.component.scss'],
})
export class LicenseSearchFormComponent implements OnInit {
    @Input() affiliateData;
    @Input() licensesData;

    @Output() submit = new EventEmitter();

    form: FormGroup;
    isFormExpanded = true;
    dateFromSetting = {};
    dateToSetting = {};
    beginDate: any;
    endDate: any;
    locale = '';

    constructor(
        private fb: FormBuilder,
        private translateService: TranslateService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.form = this.fb.group({
            affiliate: [DEFAULT_SELECTOR_VALUE],
            customerNr: [''],
            packName: [DEFAULT_SELECTOR_VALUE],
            beginDate: [''],
            endDate: [''],
        });
        this.buildDatePickerValues();
    }

    onSubmit() {
        this.submit.emit({
            affiliate: this.form.controls.affiliate.value.value || '',
            customerNr: this.form.controls.customerNr.value || '',
            packName: this.form.controls.packName.value.value || '',
            beginDate: this.beginDate || '',
            endDate: this.endDate || ''
        });
    }

    onDateFromChanged(event: IMyDateModel) {
        if (!event || !event.singleDate.date) {
            return;
        }
        const copy = cloneDeep(this.dateToSetting);
        copy.disableUntil = event.singleDate.date;
        this.dateToSetting = copy;
        const beginDate = new Date(Date.UTC(event.singleDate.date.year, event.singleDate.date.month - 1, event.singleDate.date.day));
        this.beginDate = beginDate.toISOString() || '';
    }

    onDateToChanged(event: IMyDateModel) {
        if (!event || !event.singleDate.date) {
            return;
        }
        const copy = cloneDeep(this.dateFromSetting);
        copy.disableSince = event.singleDate.date;
        this.dateFromSetting = copy;
        const endDate = new Date(Date.UTC(event.singleDate.date.year, event.singleDate.date.month - 1, event.singleDate.date.day));
        this.endDate = endDate.toISOString() || '';
    }

    private buildDatePickerValues() {
        // validate range date from can not be over date to
        const dateFromSetting = Object.assign(
            {},
            this.dateFromSetting,
            DateUtil.getCommonSetting()
        );

        this.dateFromSetting = dateFromSetting;

        // validate range date to can not be before date from
        const dateToSetting = Object.assign(
            {},
            this.dateToSetting,
            DateUtil.getCommonSetting()
        );

        this.dateToSetting = dateToSetting;
    }
}
