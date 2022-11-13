import { Injectable } from '@angular/core';

import {
  CurrencyUtil
} from 'sag-currency';

import { KEY_BOARD, CHARACTER, CURRENCY } from '../../../core/enums/enums';
import { DecimalSettingModel } from './decimal-setting.model';
import { FORMATTED_MODE } from './decimal-number.constant';
import AffUtils from 'src/app/core/utils/aff-utils';

const DEFAULT_DIGIT = 2;
const DECIMAL_SETTINGS = {
  de_DE: {
    supportedDecimalSeparator: [CHARACTER.COMMAS.toString()],
    decimalSeparator: CHARACTER.COMMAS.toString(),
    thousandSeparator: CHARACTER.DOT.toString(),
    currency: CURRENCY.EURO_CURRENCY.toString(),
    digits: 2,
  } as DecimalSettingModel,

  de_CH: {
    supportedDecimalSeparator: [
      CHARACTER.DOT.toString(),
      CHARACTER.COMMAS.toString(),
    ],
    decimalSeparator: CHARACTER.DOT.toString(),
    thousandSeparator: CHARACTER.APOSTROPHE.toString(),
    currency: CURRENCY.CHF_CURRENCY.toString(),
    digits: 2,
  } as DecimalSettingModel,
};
@Injectable()
export class DecimalNumberService {
  private exceptionKeyCode = [
    KEY_BOARD.BACK_SPACE,
    KEY_BOARD.ARROW_LEFT,
    KEY_BOARD.ARROW_RIGHT,
    KEY_BOARD.HOME,
    KEY_BOARD.END,
  ];
  private setting: DecimalSettingModel = {};
  private thousandSeparator: any;
  private decimalSeparator: string;
  private supportedDecimalSeparators: any[];
  private currency: string;
  private decimalSeparatorCodes: any[];
  private digits = 2;

  constructor() {
    if (AffUtils.isAT()) {
      this.setting = DECIMAL_SETTINGS.de_DE;
    } else if (AffUtils.isCH()) {
      this.setting = DECIMAL_SETTINGS.de_CH;
    }
    if (!this.setting.digits) {
      this.setting.digits = DEFAULT_DIGIT;
    }
    this.updateSetting();
  }

  updateSetting(setting?) {
    if (!!setting) {
      Object.assign(this.setting, setting);
    }
    this.decimalSeparator = this.setting.decimalSeparator;
    this.supportedDecimalSeparators = this.setting
      .supportedDecimalSeparator || [CHARACTER.COMMAS];
    this.thousandSeparator = this.setting.thousandSeparator || CHARACTER.DOT;
    this.currency = this.setting.currency;
    const digits = Number(this.setting.digits);
    if (!Number.isNaN(digits)) {
      this.digits = digits;
    } else {
      this.digits = DEFAULT_DIGIT;
    }
    // translate text to char code;
    this.decimalSeparatorCodes = this.getSeparatorCode(
      this.supportedDecimalSeparators
    );
  }

  getCurrency() {
    return this.currency;
  }

  keyPress(event: KeyboardEvent) {
    let keyCode = event.keyCode || event.charCode;
    if (keyCode === KEY_BOARD.ENTER) {
      // $(this.el.nativeElement).trigger('blur');
      event.preventDefault();
      event.stopPropagation();
      return;
    }

    const isNotNumberKey =
      keyCode < KEY_BOARD.ZERO || keyCode > KEY_BOARD.NUMBER_NINE;
    const isExceptionKey = this.exceptionKeyCode.indexOf(keyCode) !== -1;
    const isSupportkey = this.decimalSeparatorCodes.indexOf(keyCode) !== -1;
    if (
      !isExceptionKey &&
      ((isNotNumberKey && !isSupportkey) ||
        (isSupportkey && this.hasSeparatorAlready(event)))
    ) {
      event.preventDefault();
      event.stopPropagation();
      return;
    }
  }
  /**
   * Apply number format
   * @param value
   * @param options
   * {
   *    enableCurrency: bool,
   *    digits: number
   * }
   */
  applyFormat(value: string, options: any = {}) {
    if (options.enableCurrency) {
      value = value
        .replace(new RegExp(this.currency.toString(), 'g'), '')
        .trim();
    }
    let digits = Number(options.digits);
    if (Number.isNaN(digits)) {
      digits = this.digits;
    } else {
      digits = Number(options.digits);
    }
    const decimalSeparator = this.supportedDecimalSeparators.join('');
    const decimalSeparatorOr = this.supportedDecimalSeparators.join('|');
    const regexString =
      '(^-?\\d{0,100})[^' +
      decimalSeparator +
      ']*((?:[' +
      decimalSeparatorOr +
      ']\\d{0,' +
      digits +
      '})?)';
    let reg = new RegExp(regexString, 'g');
    let replaceRegex = new RegExp(`[^\\d${decimalSeparator}]`, 'g');
    let match = reg.exec(value.replace(replaceRegex, ''));
    let formattedVal = match[1] + match[2];
    let thousandReg = new RegExp(
      '\\d(?=(\\d{3})+' + (match[2].length > 0 ? '\\D' : '$') + ')',
      'g'
    );
    let result = formattedVal.replace(
      thousandReg,
      '$&' + this.thousandSeparator
    );
    if (options.enableCurrency) {
      return `${this.currency} ${result}`;
    }
    return result;
  }

  clearFormat(value: string) {
    if (!value) {
      return '';
    }
    let supportedDecimalSeparators = this.supportedDecimalSeparators.join('');
    let thousandSeparator = this.thousandSeparator;
    if (!!this.currency) {
      value = value
        .replace(new RegExp(this.currency.toString(), 'g'), '')
        .trim();
    }
    if (!!thousandSeparator) {
      value = value.replace(new RegExp('[' + thousandSeparator + ']', 'g'), '');
    }
    if (!!supportedDecimalSeparators) {
      value = value.replace(
        new RegExp('[' + supportedDecimalSeparators + ']', 'g'),
        '.'
      );
    }
    // replace all none numeric by empty
    // just double check
    return value.replace(/[^\d.]/g, '');
  }

  /**
   * Convert number to current format
   * Type is currency, number will be rounded
   * @param val
   * @param type
   * @param digits
   */
  fixedFormat(
    val: any,
    type = FORMATTED_MODE.CURRENCY,
    digits = this.setting.digits
  ) {
    let rounded: string;
    if (type === FORMATTED_MODE.CURRENCY) {
      rounded = CurrencyUtil.roundHalfEvenTo2digits(Number(val)).toFixed(digits) + '';
    } else {
      rounded = val + '';
    }
    return rounded.replace(CHARACTER.DOT.toString(), this.decimalSeparator);
  }

  private getSeparatorCode(separators: any[]) {
    let getCode = (value: any) => {
      let code;
      switch (value) {
        case CHARACTER.COMMAS:
          code = KEY_BOARD.SEPARATOR_COMMAS;
          break;
        default:
          code = KEY_BOARD.SEPARATOR_DOT;
          break;
      }
      return code;
    };
    return separators.map((charator) => getCode(charator));
  }

  private hasSeparatorAlready(event) {
    let value = event['target']['value'];
    let isExisted = false;
    this.supportedDecimalSeparators.forEach((separator) => {
      isExisted = isExisted || value.indexOf(separator) !== -1;
    });
    return isExisted;
  }
}
