import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IAngularMyDpOptions, IMyDate, IMyDateModel } from 'angular-mydatepicker';
import * as moment from 'moment';
import { TranslateService } from '@ngx-translate/core';
import { DateUtil } from 'src/app/core/utils/date.util';
import { Subject } from 'rxjs';
import { AnalyticalCardFilter } from '../../models/analytical-card/analytical-card-filter.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ActivatedRoute } from '@angular/router';
import { ANALYTICAL_CARD_PAYMENT, ANALYTICAL_CARD_STATUS } from '../../enums/analytical-card/analytical-card.enum';

@Component({
    selector: 'connect-analytical-card-filter',
    templateUrl: './analytical-card-filter.component.html',
    styleUrls: ['./analytical-card-filter.component.scss']
})
export class AnalyticalCardFilterComponent implements OnInit, OnDestroy {
    @Input() paymentMethod: ANALYTICAL_CARD_PAYMENT;
    @Input() isSearching = false;
    @Output() filterEmitter = new EventEmitter<any>();

    private readonly dateFormat = 'YYYY-MM-DD';

    dateFromOption: IAngularMyDpOptions = {
        dateRange: false,
        dateFormat: 'dd.mm.yyyy',
        sunHighlight: true,
        markCurrentDay: true,
        showSelectorArrow: true,
        showFooterToday: true,
        focusInputOnDateSelect: false
    };
    locale: string;
    dateToOption: IAngularMyDpOptions = { ...this.dateFromOption };

    filterForm: FormGroup;
    fromDetail = false;
    dateFromDisableToday = false;
    dateToDisableToday = false;

    statusOptions = [
        {
            id: 'NONE',
            name: "ALL"
        },
        {
            id: ANALYTICAL_CARD_STATUS.POSTED,
            name: 'SETTINGS.ANALYTICAL_CARD.STATUS.POSTED'
        },
        {
            id: ANALYTICAL_CARD_STATUS.IN_PROCESS,
            name: 'SETTINGS.ANALYTICAL_CARD.STATUS.IN_PROCESS'
        }
    ];

    private destroy$ = new Subject();

    constructor (
        private fb: FormBuilder,
        private activeRoute: ActivatedRoute,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.initForm();
        this.onSubmit(null, true);

        this.locale = this.translateService.currentLang;
    }

    ngOnDestroy() {
        this.destroy$.complete();
        this.destroy$.unsubscribe();
    }

    onDateFromChanged(event: IMyDateModel) {
        DateUtil.updateDateToWhenChangeDateFrom(
            event,
            this.dateTo.value,
            (dateTo) => this.updateDateToByDateFrom(dateTo),
            null
        );
        if (this.dateFrom.value) {
            const jsFromDate = DateUtil.datePickerToJsDate(this.dateFrom.value.singleDate.date);
            this.dateToDisableToday = DateUtil.isDateInRange(new Date(), jsFromDate, true);
        }
    }

    onDateToChanged(event: IMyDateModel) {
        DateUtil.updateDateFromWhenChangeDateTo(
            this.dateFrom.value,
            event,
            (dateFrom) => this.updateDateFromByDateTo(dateFrom),
            null
        );
        if (this.dateTo.value) {
            const jsToDate = DateUtil.datePickerToJsDate(this.dateTo.value.singleDate.date);
            this.dateFromDisableToday = DateUtil.isDateInRange(new Date(), jsToDate, false);
        }
    }

    moveBackOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = this.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, this.dateFormat).subtract(1, 'M');
        const newDateTo = moment(newDateFrom, this.dateFormat).add(1, 'M');

        this.filterForm.patchValue({
            dateTo: DateUtil.buildDataDatePickerMoment(newDateTo),
            dateFrom: DateUtil.buildDataDatePickerMoment(newDateFrom)
        });

        this.onSubmit();
    }

    moveForwardOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = this.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, this.dateFormat).add(1, 'M');
        const newDateTo = moment(newDateFrom, this.dateFormat).add(1, 'M');

        if (newDateTo.isAfter(moment())) {
            return;
        }

        this.filterForm.patchValue({
            dateFrom: DateUtil.buildDataDatePickerMoment(newDateFrom),
            dateTo: DateUtil.buildDataDatePickerMoment(newDateTo),
        });

        this.onSubmit();
    }

    onSubmit(callback?, onInit = false) {
        const request = new AnalyticalCardFilter(this.filterForm.value);
        if (onInit) {
            const { sorting, page } = this.activeRoute.snapshot.queryParams;
            request.sorting = sorting;
            request.page = page;
        } else {
            request.page = 1;
        }

        this.filterEmitter.emit({
            request,
            done: () => {
                if (callback) {
                    callback();
                }
            }
        });
    }

    get dateFrom() { return this.filterForm.get('dateFrom'); }
    get dateTo() { return this.filterForm.get('dateTo'); }

    private initForm() {
        const { paymentMethod, dateFrom, dateTo, status } = this.activeRoute.snapshot.queryParams;

        let dateFromPicker = DateUtil.buildDataDatePickerMoment(moment().subtract(1, 'M'));
        let dateToPicker = DateUtil.buildDataDatePickerMoment(moment());

        if (this.paymentMethod === paymentMethod) {
            if (dateFrom) {
                dateFromPicker = DateUtil.buildDataDatePickerMoment(moment(dateFrom));
            }
    
            if (dateTo) {
                dateToPicker = DateUtil.buildDataDatePickerMoment(moment(dateTo));
            }
        }

        this.filterForm = this.fb.group({
            status: [status || 'NONE'],
            dateFrom: [dateFromPicker],
            dateTo: [dateToPicker]
        });

        this.dateFromOption = { ...this.dateFromOption, disableSince: dateToPicker.singleDate.date };
        this.dateToOption = { ...this.dateToOption, disableUntil: dateFromPicker.singleDate.date };
    }

    private buildFullDateData(date: IMyDate) {
        return `${date.year}-${date.month}-${date.day}`;
    }

    private updateDateFromByDateTo(dateTo: string) {
        const newDateFrom = moment(dateTo).subtract(1, 'M');
        const newDateFromPicker = DateUtil.buildDataDatePickerMoment(newDateFrom);
        this.filterForm.controls['dateFrom'].setValue(newDateFromPicker);
    }

    private updateDateToByDateFrom(dateFrom: string = null) {
        const newDateTo = !!dateFrom ? moment(dateFrom).add(1, 'M') : moment();
        const newDateToPicker = DateUtil.buildDataDatePickerMoment(newDateTo);
        this.filterForm.controls['dateTo'].setValue(newDateToPicker);
    }
}
