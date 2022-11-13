import { DiscountType } from '../enums/discount.enums';

export class DiscountModel {
    type: DiscountType;
    amount: number;
    reason: string;

    constructor(type: DiscountType, amount: number, reason: string) {
        this.type = type;
        this.amount = amount;
        this.reason = reason;
    }
}
