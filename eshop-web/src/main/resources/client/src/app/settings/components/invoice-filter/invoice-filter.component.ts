import { environment } from 'src/environments/environment';
import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IMyDate, IMyDateModel } from 'angular-mydatepicker';
import * as moment from 'moment';
import { AffiliateUtil } from 'sag-common';
import { SubSink } from 'subsink';
import { InvoiceSearch } from '../../models/invoice/invoice-search.model';
import { InvoiceBusinessService } from '../../services/invoice-business.service';
import { TODAY } from '../../settings.constant';
import { Constant } from 'src/app/core/conts/app.constant';
import { TranslateService } from '@ngx-translate/core';
import { DateUtil } from 'src/app/core/utils/date.util';

@Component({
    selector: 'connect-invoice-filter',
    templateUrl: './invoice-filter.component.html',
    styleUrls: ['./invoice-filter.component.scss']
})
export class InvoiceFilterComponent implements OnInit, OnDestroy {
    @Input() isCHAffiliates: boolean;
    @Input() errorMessage: string;
    @Input() isShownOldInvoice = false;

    @Output() datePickerEmitter = new EventEmitter<InvoiceSearch>();
    @Output() errorMessageChange = new EventEmitter<string>();
    @Output() isShownOldInvoiceChange = new EventEmitter<boolean>();

    dateFromOption = this.invoiceBusinessService.getDatePickerOption();
    dateToOption = this.invoiceBusinessService.getDatePickerOption();
    dateRangeOptions = this.invoiceBusinessService.getDateRangeSelect();

    filterForm: FormGroup;

    isShownDatePicker = false;
    isUsedDateRangePicker = false;
    isUsedDatePicker = true;
    locale = '';
    dateFromDisableToday = false;
    dateToDisableToday = false;

    isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);

    private maxDateForOldInvoice = { year: 2020, month: 1, day: 1 };
    private maxDateForPicker = { year: 2019, month: 11, day: 31 };
    oldInvoiceYear = this.isAxCz ? 2022 : 2020;
    private readonly dateFormat = 'YYYY-MM-DD';

    subs = new SubSink();

    constructor(
        private fb: FormBuilder,
        private invoiceBusinessService: InvoiceBusinessService,
        private translateService: TranslateService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.initForm();
        this.initSearch();
        
        this.maxDateForOldInvoice = { year: this.oldInvoiceYear, month: 1, day: 1 };
        this.maxDateForPicker = { year: this.oldInvoiceYear-1, month: 11, day: 31 };

        this.subs.sink = this.datePicker.valueChanges.subscribe((date: IMyDateModel) => {
            const selectedDate = moment(date.singleDate.date).subtract(1, 'M').format(this.dateFormat);
            this.isShownDatePicker = true;
            this.isUsedDatePicker = true;
            this.isUsedDateRangePicker = false;

            const request = new InvoiceSearch({
                dateTo: selectedDate,
                dateFrom: selectedDate,
                oldInvoice: this.isShownOldInvoice
            });
            this.datePickerEmitter.emit(request);
        });

        this.subs.sink = this.dateRangeSelect.valueChanges.subscribe((data) => {
            this.isShownDatePicker = false;
            const fromTodayObj = this.invoiceBusinessService.buildDateFromRange(data);

            const request = new InvoiceSearch({
                dateTo: moment().format(this.dateFormat),
                dateFrom: fromTodayObj.format(this.dateFormat),
                oldInvoice: this.isShownOldInvoice
            });

            this.datePickerEmitter.emit(request);
        });

        this.subs.sink = this.dateFrom.valueChanges.subscribe((date: IMyDateModel) => {
            DateUtil.updateDateToWhenChangeDateFrom(
                this.dateFrom.value,
                this.dateTo.value,
                this.updateDateToByDateFrom.bind(this),
                this.updateErrormsg.bind(this),
            );
            if (this.dateFrom.value) {
                const jsFromDate = DateUtil.datePickerToJsDate(this.dateFrom.value.singleDate.date);
                this.dateToDisableToday = DateUtil.isDateInRange(new Date(), jsFromDate, true);
            }
        });

        this.subs.sink = this.dateTo.valueChanges.subscribe((date: IMyDateModel) => {
            DateUtil.updateDateFromWhenChangeDateTo(
                this.dateFrom.value,
                this.dateTo.value,
                this.updateDateFromByDateTo.bind(this),
                this.updateErrormsg.bind(this)
            );
            if (this.dateTo.value) {
                const jsToDate = DateUtil.datePickerToJsDate(this.dateTo.value.singleDate.date);
                this.dateFromDisableToday = DateUtil.isDateInRange(new Date(), jsToDate, false);
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onSubmit() {
        this.isUsedDateRangePicker = true;
        this.isUsedDatePicker = false;

        this.initSearch();
    }

    moveBackOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = this.invoiceBusinessService.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, 'YYYY-MM-DD').subtract(1, 'M');
        const newDateTo = moment(newDateFrom, 'YYYY-MM-DD').add(1, 'M');

        this.filterForm.patchValue({
            dateTo: this.invoiceBusinessService.buildDateData(newDateTo),
            dateFrom: this.invoiceBusinessService.buildDateData(newDateFrom)
        });

        const request = new InvoiceSearch({
            dateTo: newDateTo.format(this.dateFormat),
            dateFrom: newDateFrom.format(this.dateFormat),
            oldInvoice: this.isShownOldInvoice
        });

        this.isUsedDateRangePicker = true;
        this.isUsedDatePicker = false;

        const valid = this.validateDateRangeByChangeOneMonth(request.dateFrom, request.dateTo);

        if (valid) {
            this.datePickerEmitter.emit(request);
        }
    }

    moveForwardOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = this.invoiceBusinessService.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, 'YYYY-MM-DD').add(1, 'M');
        const newDateTo = moment(newDateFrom, 'YYYY-MM-DD').add(1, 'M');

        if (newDateTo.isAfter(this.getCurrentDate(this.isShownOldInvoice))) {
            return;
        }

        this.filterForm.patchValue({
            dateFrom: this.invoiceBusinessService.buildDateData(newDateFrom),
            dateTo: this.invoiceBusinessService.buildDateData(newDateTo),
        });

        const request = new InvoiceSearch({
            dateTo: newDateTo.format(this.dateFormat),
            dateFrom: newDateFrom.format(this.dateFormat),
            oldInvoice: this.isShownOldInvoice
        });

        this.isUsedDateRangePicker = true;
        this.isUsedDatePicker = false;

        const valid = this.validateDateRangeByChangeOneMonth(request.dateFrom, request.dateTo);
        if (valid) {
            this.datePickerEmitter.emit(request);
        }
    }

    enableOldInvoice() {
        this.isShownOldInvoice = !this.isShownOldInvoice;
        this.isUsedDatePicker = false;
        this.isUsedDateRangePicker = false;
        // handle old invoices state
        if (this.isShownOldInvoice) {
            this.dateFromOption = { ...this.dateFromOption, disableSince: this.maxDateForOldInvoice };
            this.dateToOption = { ...this.dateToOption, disableSince: this.maxDateForOldInvoice };
            var oldInvoiceYearString = new String(this.oldInvoiceYear-1);
            this.filterForm.patchValue({
                dateFrom: this.invoiceBusinessService.buildDateData(moment(`${oldInvoiceYearString}-11-30`)),
                dateTo: this.invoiceBusinessService.buildDateData(moment(`${oldInvoiceYearString}-12-31`)),
                });

            this.initSearch();
        } else {
            this.dateFromOption = { ...this.invoiceBusinessService.getDatePickerOption() };
            this.dateToOption = { ...this.invoiceBusinessService.getDatePickerOption() };

            setTimeout(() => {
                this.initSearch();
            });
        }

        this.isShownOldInvoiceChange.emit(this.isShownOldInvoice);
    }

    get dateFrom() { return this.filterForm.get('dateFrom'); }
    get dateTo() { return this.filterForm.get('dateTo'); }
    get datePicker() { return this.filterForm.get('datePicker'); }
    get dateRangeSelect() { return this.filterForm.get('dateRangeSearch'); }

    private validateDateRangeByChangeOneMonth(dateFrom, dateTo): boolean {
        if (moment(dateTo).diff(moment(dateFrom), 'days') > Constant.FILTER_RANGE_DATE) {
            return false;
        }

        return true;
    }

    private initForm() {
        const currentDate = moment();

        const dateToPicker = this.invoiceBusinessService.buildDateData(currentDate);
        const dateFromPicker = this.invoiceBusinessService.buildDateData(currentDate);

        this.filterForm = this.fb.group({
            dateFrom: dateFromPicker,
            dateTo: dateToPicker,
            datePicker: dateToPicker,
            dateRangeSearch: TODAY
        });
    }

    private initSearch() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const dateTo = (this.dateTo.value as IMyDateModel).singleDate.date;

        const dateFromObj = moment(this.invoiceBusinessService.buildFullDateData(dateFrom));
        const dateToObj = moment(this.invoiceBusinessService.buildFullDateData(dateTo));

        const request = new InvoiceSearch({
            dateTo: dateToObj.format(this.dateFormat),
            dateFrom: dateFromObj.format(this.dateFormat),
            oldInvoice: this.isShownOldInvoice
        });

        this.datePickerEmitter.emit(request);
    }

    private updateDateFromByDateTo(dateTo: string) {
        const newDateFrom = moment(dateTo).subtract(1, 'M');
        const newDateFromPicker = this.invoiceBusinessService.buildDateData(newDateFrom);
        this.filterForm.controls['dateFrom'].setValue(newDateFromPicker);
    }

    private updateDateToByDateFrom(dateFrom: string = null) {
        const newDateTo = !!dateFrom ? moment(dateFrom).add(1, 'M') : moment();
        const newDateToPicker = this.invoiceBusinessService.buildDateData(newDateTo);
        this.filterForm.controls['dateTo'].setValue(newDateToPicker);
    }

    private updateErrormsg(msg: string) {
        this.errorMessage = msg;
        this.errorMessageChange.emit(this.errorMessage);
    }


    /**
     * Get current date from the context of old invoice state
     * @param isShownOldInvoice true if allow invoice to show old invoice from database
     */
    getCurrentDate(isShownOldInvoice: boolean) {
        return isShownOldInvoice ? moment(this.maxDateForPicker) : moment();
    }

}
