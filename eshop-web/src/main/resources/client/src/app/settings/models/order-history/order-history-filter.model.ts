import * as moment from 'moment';
import { IMyDateModel } from 'angular-mydatepicker';
import { DateUtil } from 'src/app/core/utils/date.util';

export interface SelectModel {
    id: string;
    name: string;
}

export interface OrderHistoryFilterRequest {
    status: string;
    username: string;
    from: string;
    to: string;
    orderNumber: string;
    articleNumber: string;
}

export class OrderHistoryFilter {
    orderStatuses: SelectModel[];
    usernames: SelectModel[];
    dateFrom: string;
    dateTo: string;
    orderNumber: string;
    articleNumber: string;

    constructor(data?: any) {
        if (data) {
            this.orderStatuses = this.tranformOrderStatus(data.orderStatuses || []);
            this.usernames = this.tranformOrderStatus(data.usernames || []);
            this.dateFrom = data.dateFrom;
            this.dateTo = data.dateTo;
            this.orderNumber = data.orderNumber;
            this.articleNumber = data.articleNumber;
        }
    }

    tranformOrderStatus(orderStatus: any[]): SelectModel[] {
        return orderStatus.map(status => ({
            id: status,
            name: status
        }));
    }

    request({ status, username, dateFrom, dateTo, orderNumber, articleNumber }) {
        const fromDate = (dateFrom as IMyDateModel).singleDate.date;
        const ogirinalFromDate = DateUtil.buildFullDateData(fromDate);
        const newDateFrom = moment(ogirinalFromDate, 'YYYY-MM-DD')

        const toDate = (dateTo as IMyDateModel).singleDate.date;
        const ogirinalToDate = DateUtil.buildFullDateData(toDate);
        const newToDate = moment(ogirinalToDate, 'YYYY-MM-DD')
        return {
            status,
            username,
            from: newDateFrom.format('YYYY-MM-DD'),
            to: newToDate.format('YYYY-MM-DD'),
            orderNumber: orderNumber,
            articleNumber: articleNumber
        } as OrderHistoryFilterRequest;
    }
}
