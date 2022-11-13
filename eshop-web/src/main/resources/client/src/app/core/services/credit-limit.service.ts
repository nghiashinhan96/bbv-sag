import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { SagCurrencyPipe } from 'sag-currency';

import { environment } from 'src/environments/environment';
import { UserDetail } from '../models/user-detail.model';
import { AffiliateUtil } from 'sag-common';
import { TranslateService } from '@ngx-translate/core';
import { PAYMENT_METHOD } from '../enums/shopping-basket.enum';


export class AxCreditLimit {
    alreadyUsedCredit = 0;
    availableCredit = 0;
    alreadyUsedCreditCashPayment = 0;
    availableCreditCashPayment = 0;
}

const CREDIT_CHECK_URL = 'customers/credit/check';

@Injectable({
    providedIn: 'root'
})
export class CreditLimitService {
    private creditLimitInfoSub$ = new BehaviorSubject<any>(null);
    private creditCardLimitSub$ = new BehaviorSubject<any>(null);
    private baseUrl = environment.baseUrl;
    private axCredit: AxCreditLimit = new AxCreditLimit();
    private isSalesOnBeHalf = false;
    private enableCreditLimit = false;
    private isSb = AffiliateUtil.isSb(environment.affiliate);

    creditCardInfo: {
        creditValid: boolean;
        creditMessageListPart: {};
        creditMessageQuickView: {};
        creditLimitAvail: number;
        alreadyUsedCredit: number;
    };
    constructor(
        private http: HttpClient,
        private currencyPipe: SagCurrencyPipe,
        private translateService: TranslateService
    ) { }

    get creditLimitInfo$() {
        return this.creditLimitInfoSub$.asObservable();
    }

    get creditCardInfo$() {
        return this.creditCardLimitSub$.asObservable();
    }

    get getTotalCreditLimit() {
        return this.axCredit.alreadyUsedCredit + this.axCredit.availableCredit;
    }

    get getTotalCreditLimitCashPayment() {
        return this.axCredit.alreadyUsedCreditCashPayment + this.axCredit.availableCreditCashPayment;
    }

    get axCreditInfo() {
        return this.axCredit || {};
    }

    initCreditCard({ isUserOnBehalf: isSalesOnBeHalf }) {
        this.isSalesOnBeHalf = isSalesOnBeHalf;
        this.enableCreditLimit = this.isSalesOnBeHalf || this.isSb;
        this.axCredit = new AxCreditLimit();
        this.creditLimitInfoSub$.next(this.axCredit);
        this.creditCardLimitSub$.next(null);
    }

    resetCreditCardInfo(user: UserDetail) {
        if (this.enableCreditLimit) {
            this.getCreditLimit(user.companyName, user.custNr).subscribe(res => {
                this.axCredit = res;
                this.creditLimitInfoSub$.next(this.axCredit);
                this.checkCreditLimitAvail();
            });
        }
    }

    updateCreditCardInfo() {
        if (this.enableCreditLimit) {
            this.checkCreditLimitAvail();
        }
    }

    private checkCreditLimitAvail() {
        const { 
            alreadyUsedCredit,
            availableCredit,
            availableCreditCashPayment
        } = this.axCredit;
        const defaultInfo = {
            creditValid: true,
            creditMessageListPart: null,
            creditMessageQuickView: null,
            creditLimitAvail: 0,
            alreadyUsedCredit: 0
        };
        if (this.isSb) {
            if (this.getTotalCreditLimit === 0 && this.getTotalCreditLimitCashPayment === 0) {
                this.creditCardInfo = defaultInfo;
                this.creditCardLimitSub$.next(this.creditCardInfo);
                return;
            }

            const url = `${this.baseUrl}${CREDIT_CHECK_URL}?customerCredit=${availableCredit}&customerCreditCashPayment=${availableCreditCashPayment}`;
            this.http.get(url).subscribe((res: any) => {
                this.emitCreditInfoByPaymentType(res);
            });
            return;
        }
        if (this.getTotalCreditLimit === 0) {
            this.creditCardInfo = defaultInfo;
            this.creditCardLimitSub$.next(this.creditCardInfo);
            return;
        }
        const url = `${this.baseUrl}${CREDIT_CHECK_URL}?customerCredit=${availableCredit}`;
        this.http.get(url).subscribe((res: any) => {
            this.emitCreditInfo(res, availableCredit, alreadyUsedCredit);
        });
    }

    private emitCreditInfo(res: any, creditLimitAvail = 0, alreadyUsedCredit = 0) {
        this.creditCardInfo = {
            creditValid: res && res.valid,
            creditMessageListPart: this.buildCreditMessage(res, true),
            creditMessageQuickView: this.buildCreditMessage(res, false),
            creditLimitAvail,
            alreadyUsedCredit
        };
        this.creditCardLimitSub$.next(this.creditCardInfo);
    }

    private emitCreditInfoByPaymentType(res: any) {
        const isValid = (res && res.creditCheckResultByPaymentMethod || []).every(item => item.valid);
        this.creditCardInfo = {
            creditValid: isValid,
            creditMessageListPart: this.buildCreditMessageByPaymentMethod(isValid, res.creditCheckResultByPaymentMethod, true),
            creditMessageQuickView: this.buildCreditMessageByPaymentMethod(isValid, res.creditCheckResultByPaymentMethod, false),
            creditLimitAvail: 0,
            alreadyUsedCredit: 0
        };
        this.creditCardLimitSub$.next(this.creditCardInfo);
    }

    private getCreditLimit(affiliate: string, custNr: string): Observable<AxCreditLimit> {
        const url = `${this.baseUrl}customers/credit/info?affiliate=${affiliate}&custNr=${custNr}`;
        return this.http.get<AxCreditLimit>(url);
    }

    private buildCreditMessage(credit, hasSubMessage) {
        if (!credit || credit.valid) {
            return '';
        }
        const message = `SHOPPING_BASKET.CREDIT_MESSAGE.${credit.paymentMethod}`;
        let subMessage = '';
        if (hasSubMessage) {
            subMessage = 'SHOPPING_BASKET.CREDITE_LIMIT_REMOVE_ITEM';
        }
        const messageParams = {
            credit: this.currencyPipe.transform(credit.customerCredit, { isAllowNegative: true }),
            additionalCredit: this.currencyPipe.transform(credit.additionalCredit, { isAllowNegative: true })
        };
        return { message, subMessage, messageParams };
    }

    private buildCreditMessageByPaymentMethod(isValid: boolean, infos: any[], hasSubMessage: boolean) { 
        if (isValid) {
            return '';
        }

        const messages = [];

        const credit = infos.find(item => item.paymentMethod === PAYMENT_METHOD.WHOLESALE && !item.valid);
        if (credit) {
            const creditAmount = this.currencyPipe.transform(credit.customerCredit || 0, { isAllowNegative: true });
            messages.push(this.translateService.instant('SHOPPING_BASKET.CREDIT_MESSAGE.CREDIT', { credit: creditAmount }));
        }

        const cashCredit = infos.find(item => item.paymentMethod === PAYMENT_METHOD.CASH && !item.valid);
        if (cashCredit) {
            const cashCreditAmount = this.currencyPipe.transform(cashCredit.customerCredit || 0, { isAllowNegative: true });
            messages.push(this.translateService.instant('SHOPPING_BASKET.CREDIT_MESSAGE.ADDITIONAL_CREDIT', { additionalCredit: cashCreditAmount }));
        }

        let message = messages.join('<br />');

        let subMessage = '';
        if (hasSubMessage) {
            subMessage = 'SHOPPING_BASKET.CREDITE_LIMIT_REMOVE_ITEM_NON_AX';
        }

        return { message, subMessage };
    }
}
