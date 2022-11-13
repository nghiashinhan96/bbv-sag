import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackUtils } from '../utils/feedback.utils';

export class FeedbackSalesUserData {
    private userId: number;
    private userEmail: string;
    private userPhone: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.userId = data.userId;
        this.userEmail = data.userEmail;
        this.userPhone = data.userPhone;
    }

    get userIdValue(): string {
        return FeedbackUtils.getValue(this.userId ? this.userId.toString() : null);
    }

    get userEmailValue(): string {
        return FeedbackUtils.getValue(this.userEmail);
    }

    get userPhoneValue(): string {
        return FeedbackUtils.getValue(this.userPhone);
    }

    get userInfoValue(): string {
        if (this.userId || this.userEmail) {
            return `${this.userIdValue} - ${this.userEmailValue}`;
        }
        return NOT_AVAILABLE;
    }
}
