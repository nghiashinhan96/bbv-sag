import { ActivatedRoute } from '@angular/router';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {
    IAngularMyDpOptions,
    IMyDateModel,
    IMyInputFieldChanged
} from 'angular-mydatepicker';
import * as moment from 'moment';
import { Subject, Subscription } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';

import { OrderHistoryFilter } from '../../models/order-history/order-history-filter.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { DateUtil } from 'src/app/core/utils/date.util';
import { TranslateService } from '@ngx-translate/core';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

const DATE_FORMAT = 'YYYY-MM-DD';
@Component({
    selector: 'connect-order-history-filter',
    templateUrl: './order-history-filter.component.html',
    styleUrls: ['./order-history-filter.component.scss']
})
export class OrderHistoryFilterComponent implements OnInit, OnDestroy {
    @Input() orderHistoryFilter: Observable<OrderHistoryFilter>;
    @Input() userDetail: UserDetail;
    @Output() filterEmitter = new EventEmitter<any>();
    @Input() isSearching = false;

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

    cz = AffiliateEnum.CZ;
    isCz = AffiliateUtil.isCz(environment.affiliate);

    private destroy$ = new Subject();

    constructor (
        private fb: FormBuilder,
        private activeRoute: ActivatedRoute,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        const { fromDetail, from, to } = this.activeRoute.snapshot.queryParams;
        this.fromDetail = fromDetail;
        this.initForm(from, to);

        if (this.fromDetail) {
            this.onSubmit();
        }

        this.locale = this.translateService.currentLang;
    }

    ngOnDestroy() {
        this.destroy$.complete();
        this.destroy$.unsubscribe();
    }

    private updateDateFrom(event: IMyDateModel) {
        const dateFrom = event.singleDate.date;
        const ogirinalDateFrom = DateUtil.buildFullDateData(dateFrom);
        const newDateFrom = moment(ogirinalDateFrom, DATE_FORMAT);
        this.dateToDisableToday = DateUtil.isDateInRange(new Date(), newDateFrom.toDate(), true);

        const dateToLimit = this.getDateToLimit(event);
        const minDateTo = this.getMinDateTo(this.dateTo.value, dateToLimit);

        this.dateFromOption = { ...this.dateFromOption, disableSince: minDateTo.singleDate.date };
        this.dateToOption = { ...this.dateToOption, disableUntil: dateFrom };
        this.filterForm.patchValue({
            dateFrom: DateUtil.buildDataDatePickerMoment(newDateFrom),
            dateTo: this.isCz ? minDateTo : this.dateTo.value
        });
        
    }

    private updateDateTo(event: IMyDateModel) {
        const dateTo = event.singleDate.date;
        const ogirinalDateTo = DateUtil.buildFullDateData(dateTo);
        const newDateTo = moment(ogirinalDateTo, DATE_FORMAT);
        this.dateFromDisableToday = DateUtil.isDateInRange(new Date(), newDateTo.toDate(), false);

        const dateFromLimit = this.getDateFromLimit(event);
        const maxDateFrom = this.getMaxDateFrom(this.dateFrom.value, dateFromLimit);

        this.dateFromOption = { ...this.dateFromOption, disableSince: dateTo };
        this.dateToOption = { ...this.dateToOption, disableUntil: maxDateFrom.singleDate.date };
        this.filterForm.patchValue({
            dateFrom: this.isCz ? maxDateFrom : this.dateFrom.value,
            dateTo: DateUtil.buildDataDatePickerMoment(newDateTo)
        });
    }

    onDateFromChanged(event: IMyDateModel) {
        this.updateDateFrom(event);
    }

    onDateToChanged(event: IMyDateModel) {
        this.updateDateTo(event);
    }

    resetUnfocusInput(control) {
        this.filterForm.get(control).reset();
    }

    onSubmit(callback?) {
        const request = new OrderHistoryFilter().request(this.filterForm.value);

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

    private initForm(from, to) {
        const maxRange = this.isCz ? 3 : 1;
        let dateFromPicker = DateUtil.buildDataDatePickerMoment(moment().subtract(maxRange, 'M'));
        if (from) {
            dateFromPicker = DateUtil.buildDataDatePickerMoment(moment(from));
        }

        let dateToPicker = DateUtil.buildDataDatePickerMoment(moment());
        if (to) {
            dateToPicker = DateUtil.buildDataDatePickerMoment(moment(to));
        }

        const dateFromLimit = this.getDateFromLimit(dateToPicker);
        const maxDateFrom = this.getMaxDateFrom(dateFromPicker, dateFromLimit);
        const tomorrow = DateUtil.buildDataDatePickerMoment(moment().add(1, 'day'));

        this.filterForm = this.fb.group({
            status: ['ALL'],
            username: [this.setUserName()],
            dateFrom: [this.isCz ? maxDateFrom : dateFromPicker],
            dateTo: [dateToPicker],
            orderNumber: [''],
            articleNumber: ['']
        });

        this.dateFromOption = { ...this.dateFromOption, disableSince: dateToPicker.singleDate.date };
        this.dateToOption = { ...this.dateToOption, disableSince: tomorrow.singleDate.date, disableUntil: dateFromPicker.singleDate.date };
    }

    private setUserName() {
        if (this.userDetail.isSalesOnBeHalf || this.userDetail.salesUser || this.userDetail.userAdminRole) {
            return 'ALL';
        } else {
            return this.userDetail.username;
        }
    }

    private getDateFromLimit(dateTo: IMyDateModel) {
        const from = moment(DateUtil.buildFullDateData(dateTo.singleDate.date), DATE_FORMAT);
        const minDateFrom = moment(from).subtract(3, 'M');
        return DateUtil.buildDataDatePickerMoment(minDateFrom);
    }

    private getMaxDateFrom(dateFrom: IMyDateModel, minDateFrom: IMyDateModel) {
        const from = moment(DateUtil.buildFullDateData(dateFrom.singleDate.date), DATE_FORMAT);
        const minFrom = moment(DateUtil.buildFullDateData(minDateFrom.singleDate.date), DATE_FORMAT);

        if (DateUtil.isDateInRange(from, minFrom, false)) {
            return dateFrom;
        }

        return minDateFrom;
    }

    private getDateToLimit(dateFrom: IMyDateModel) {
        const from = moment(DateUtil.buildFullDateData(dateFrom.singleDate.date), DATE_FORMAT);
        const maxDateTo = moment(from).add(3, 'M');
        return DateUtil.buildDataDatePickerMoment(maxDateTo);
    }

    private getMinDateTo(dateTo: IMyDateModel, maxDateTo: IMyDateModel) {
        const to = moment(DateUtil.buildFullDateData(dateTo.singleDate.date), DATE_FORMAT);
        const maxTo = moment(DateUtil.buildFullDateData(maxDateTo.singleDate.date), DATE_FORMAT);

        if (DateUtil.isDateInRange(to, maxTo, true)) {
            return dateTo;
        }

        return maxDateTo;
    }
}
