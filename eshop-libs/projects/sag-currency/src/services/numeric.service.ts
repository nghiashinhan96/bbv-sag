import { SagCurrencySettingModel } from '../models/currency-setting.model';
import { SagCurrencyStorageService } from './currency-storage.service';
import { Injectable } from '@angular/core';
import { CurrencyUtil } from '../utils/currency.util';
import { SAG_CURRENCY_FORMATTED_MODE, DECIMAL_SETTINGS, DEFAULT_DIGIT, CHARACTER, KEY_BOARD } from '../currency.constant';

@Injectable()
export class SagNumericService {

    private exceptionKeyCode = [
        KEY_BOARD.BACK_SPACE,
        KEY_BOARD.ARROW_LEFT,
        KEY_BOARD.ARROW_RIGHT,
        KEY_BOARD.HOME,
        KEY_BOARD.END
    ];
    private defaultSetting: SagCurrencySettingModel = {};
    private setting: SagCurrencySettingModel = {};
    private thousandSeparator: any;
    private decimalSeparator: string;
    private supportedDecimalSeparators: any[];
    private currency: string;
    private decimalSeparatorCodes: any[];
    private digits = 2;

    constructor(private storge: SagCurrencyStorageService) {
        // this.defaultSetting = DECIMAL_SETTINGS[this.storge.locale] || {};
        // this.updateSetting();
    }

    updateSetting(setting?) {
        this.defaultSetting = DECIMAL_SETTINGS[this.storge.locale] || {};
        this.setting = { ...this.defaultSetting };
        if (!!setting) {
            Object.assign(this.setting, setting);
        }
        this.decimalSeparator = this.setting.decimalSeparator;
        this.supportedDecimalSeparators = this.setting.supportedDecimalSeparator || [CHARACTER.DOT];
        this.thousandSeparator = this.setting.thousandSeparator || CHARACTER.APOSTROPHE;
        const customerCurrency = this.storge.customerCurrency || '';
        if (customerCurrency !== '') {
            this.currency = customerCurrency;
        } else {
            this.currency = this.setting.currency;
        }
        const digits = Number(this.setting.digits);
        if (!Number.isNaN(digits)) {
            this.digits = digits;
        } else {
            this.digits = DEFAULT_DIGIT;
        }
        // translate text to char code;
        this.decimalSeparatorCodes = this.getSeparatorCode(this.supportedDecimalSeparators);
    }

    getCurrency() {
        return this.currency;
    }

    keyPress(event: KeyboardEvent) {
        const keyCode = event.keyCode || event.charCode;
        if (keyCode === KEY_BOARD.ENTER) {
            event.preventDefault();
            event.stopPropagation();
            return;
        }

        const isNotNumberKey = keyCode < KEY_BOARD.ZERO || keyCode > KEY_BOARD.NUMBER_NINE;
        const isExceptionKey = this.exceptionKeyCode.indexOf(keyCode) !== -1;
        const isSupportkey = this.decimalSeparatorCodes.indexOf(keyCode) !== -1;
        if (!isExceptionKey && ((isNotNumberKey && !isSupportkey) || (isSupportkey && this.hasSeparatorAlready(event)))) {
            event.preventDefault();
            event.stopPropagation();
            return;
        }
    }
    /**
     * Apply number format
     * @param options
     * {
     *    enableCurrency: bool,
     *    digits: number
     * }
     */
    applyFormat(value: string, options: any = {}) {
        if (options.enableCurrency) {
            value = value.replace(new RegExp(this.currency.toString(), 'g'), '').trim();
        }
        let digits = Number(options.digits);
        if (Number.isNaN(digits)) {
            digits = this.digits;
        } else {
            digits = Number(options.digits);
        }
        const decimalSeparator = this.supportedDecimalSeparators.join('');
        const decimalSeparatorOr = this.supportedDecimalSeparators.join('|');
        const regexString = '(^-?\\d{0,100})[^' + decimalSeparator + ']*((?:[' + decimalSeparatorOr + ']\\d{0,' + digits + '})?)';
        const reg = new RegExp(regexString, 'g');
        let replaceRegex = new RegExp(`[^\\d${decimalSeparator}]`, 'g');
        if (options.isAllowNegative) {
            replaceRegex = new RegExp(`[^-\\d${decimalSeparator}]`, 'g');
        }
        const match = reg.exec(value.replace(replaceRegex, ''));
        const formattedVal = match[1] + match[2];
        const thousandReg = new RegExp('\\d(?=(\\d{3})+' + (match[2].length > 0 ? '\\D' : '$') + ')', 'g');
        const result = formattedVal.replace(thousandReg, '$&' + this.thousandSeparator);
        if (options.enableCurrency) {
            return `${this.currency} ${result}`;
        }
        return result;
    }

    clearFormat(value: string) {
        if (!value) {
            return '';
        }
        const supportedDecimalSeparators = this.supportedDecimalSeparators.join('');
        const thousandSeparator = this.thousandSeparator;
        if (!!this.currency) {
            value = value.replace(new RegExp(this.currency.toString(), 'g'), '').trim();
        }
        if (!!thousandSeparator) {
            value = value.replace(new RegExp('[' + thousandSeparator + ']', 'g'), '');
        }
        if (!!supportedDecimalSeparators) {
            value = value.replace(new RegExp('[' + supportedDecimalSeparators + ']', 'g'), '.');
        }
        // replace all none numeric by empty
        // just double check
        return value.replace(/[^\d.]/g, '');
    }

    /**
     * Convert number to current format
     * Type is currency, number will be rounded
     */
    fixedFormat(val: any, type = SAG_CURRENCY_FORMATTED_MODE.CURRENCY, digits = this.setting.digits) {
        let rounded: string;
        if (type === SAG_CURRENCY_FORMATTED_MODE.CURRENCY) {
            rounded = CurrencyUtil.roundHalfEvenTo2digits(Number(val)).toFixed(digits) + '';
        } else {
            rounded = val + '';
        }
        return rounded.replace(CHARACTER.DOT.toString(), this.decimalSeparator);
    }

    private getSeparatorCode(separators: any[]) {
        const getCode = (value: any) => {
            let code;
            switch (value) {
                case CHARACTER.COMMAS:
                    code = KEY_BOARD.SEPARATOR_COMMAS;
                    break;
                default: code = KEY_BOARD.SEPARATOR_DOT;
            }
            return code;
        };
        return separators.map(charator => getCode(charator));
    }

    private hasSeparatorAlready(event) {
        const value = event.target.value;
        let isExisted = false;
        this.supportedDecimalSeparators.forEach(separator => {
            isExisted = isExisted || value.indexOf(separator) !== -1;
        });
        return isExisted;
    }
}
