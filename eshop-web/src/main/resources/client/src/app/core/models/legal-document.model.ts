import * as moment from 'moment';
import { DateUtil } from '../utils/date.util';
const DATE_FORMAT = 'YYYY-MM-DD';

export class LegalDocument {
    dateValidFrom: string;
    dateValidTo: string;
    acceptancePeriodDays: number;
    accepted: boolean;
    content: string;
    daysLeft: number;
    name: string;
    pdfUrl: string;
    sort: number;
    summary: string;
    termId: number;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.acceptancePeriodDays = data.acceptancePeriodDays || 0;
        this.accepted = data.accepted;
        this.content = data.content;
        this.daysLeft = data.daysLeft || 0;
        this.name = data.name;
        this.pdfUrl = data.pdfUrl;
        this.sort = data.sort || 0;
        this.summary = data.summary;
        this.termId = data.termId;
        this.dateValidFrom = DateUtil.formatDateInDate(moment(data.dateValidFrom, DATE_FORMAT).toDate());
        this.dateValidTo = DateUtil.formatDateInDate(moment(data.dateValidFrom, DATE_FORMAT).add(this.acceptancePeriodDays, 'days'));
    }
}