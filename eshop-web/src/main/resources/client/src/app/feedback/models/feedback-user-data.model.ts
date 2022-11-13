import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackUserData {
    private salesId: number;
    private salesEmail: string;
    private customerNr: number;
    private customerName: string;
    private customerTown: string;
    private customerEmails: string[];
    private customerPhones: string[];
    private defaultBranch: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.salesId = data.salesId;
        this.salesEmail = data.salesEmail;
        this.customerNr = data.customerNr;
        this.customerName = data.customerName;
        this.customerTown = data.customerTown;
        this.customerEmails = data.customerEmails;
        this.customerPhones = data.customerPhones;
        this.defaultBranch = data.defaultBranch;
    }

    get salesIdValue() {
        return FeedbackUtils.getValue((this.salesId || '').toString());
    }

    get salesEmailValue() {
        return FeedbackUtils.getValue(this.salesEmail);
    }

    get customerNrValue() {
        return FeedbackUtils.getValue((this.customerNr || '').toString());
    }

    get customerNameValue() {
        return FeedbackUtils.getValue(this.customerName);
    }

    get customerTownValue(): string {
        return FeedbackUtils.getValue(this.customerTown);
    }

    get customerEmailsValue() {
        return FeedbackUtils.getValues(this.customerEmails || [], true);
    }

    get customerPhonesValue() {
        return FeedbackUtils.getValues(this.customerPhones || []);
    }

    get defaultBranchValue(): string {
        return FeedbackUtils.getValue(this.defaultBranch);
    }

    get salesInfoValue() {
        if (this.salesId || this.salesEmail) {
            return `${this.salesIdValue} - ${this.salesEmailValue}`;
        }
        return NOT_AVAILABLE;
    }

    get customerInfoValue(): string {
        if (this.customerNr || this.customerName || this.customerTown) {
            return `${this.customerNrValue} - ${this.customerNameValue}, ${this.customerTownValue}`;
        }
        return NOT_AVAILABLE;
    }

    hasSalesInfo() {
        return this.salesId || this.salesEmail;
    }
}
