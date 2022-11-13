import * as moment from 'moment';
import { IMyDateModel } from 'angular-mydatepicker';
import { DateUtil } from 'src/app/core/utils/date.util';

const AnalaticalCardSortMapping = {
    postingDate: 'PostingDate',
    dueDate: 'DueDate'
};

export interface AnalyticalCardFilterRequest {
    paymentMethod: string;
    status: string;
    dateFrom: string;
    dateTo: string;
    sorting: string;
    page: number;
}

export class AnalyticalCardFilter {
    paymentMethod: string;
    status: string;
    dateFrom: IMyDateModel;
    dateTo: IMyDateModel;
    sorting = 'NONE';
    page: number;

    constructor(data?: any) {
        if (data) {
            this.paymentMethod = data.paymentMethod;
            this.status = data.status;
            this.dateFrom = data.dateFrom;
            this.dateTo = data.dateTo;
            this.sorting = data.sorting;
            this.page = data.page || 1;
        }
    }

    sort(obj: {
        field: string;
        direction: string;
    }) {
        if (obj && obj.field) {
            this.sorting = `${AnalaticalCardSortMapping[obj.field]}${obj.direction.toUpperCase()}`;
        }
        return this;
    }

    request() {
        return {
            paymentMethod: this.paymentMethod,
            status: this.status,
            dateFrom: this.getDateRequest(this.dateFrom),
            dateTo: this.getDateRequest(this.dateTo),
            sorting: this.sorting || 'NONE',
            page: this.page
        } as AnalyticalCardFilterRequest;
    }

    private getDateRequest(value: IMyDateModel) {
        if (!value) {
            return '';
        }
        const date = value.singleDate.date;
        return DateUtil.buildFullDateData(date);
    }
}
