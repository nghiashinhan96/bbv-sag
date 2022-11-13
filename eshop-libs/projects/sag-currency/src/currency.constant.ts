import { SagCurrencySettingModel } from './models/currency-setting.model';

export const SAG_CURRENCY_INPUT_DEFAULT_MODE = 'sag-currency-input-default-mode';
export const SAG_CURRENCY_INPUT_HORIZONTAL_MODE = 'sag-currency-input-horizontal-mode';
export const SAG_CURRENCY_FORMATTED_MODE = {
    CURRENCY: 1,
    OTHER: 2
};

export enum CHARACTER {
    COMMAS = ',',
    DOT = '.',
    APOSTROPHE = '\'',
    SPACE = ' '
}

export enum CURRENCY {
    SWISS_CURRENCY = 'SFr.',
    EURO_CURRENCY = 'EUR',
    CHF_CURRENCY = 'CHF',
    CZ_CURRENCY = 'Kč',
    RS_CURRENCY = 'RSD'
}

export enum KEY_BOARD {
    ENTER = 13,
    ARROW_DOWN = 40,
    ARROW_UP = 38,
    ZERO = 48,
    NUMBER_NINE = 57,
    SEPARATOR_COMMAS = 44,
    SEPARATOR_DOT = 46,
    BACK_SPACE = 8,
    ARROW_LEFT = 37,
    ARROW_RIGHT = 39,
    HOME = 36,
    END = 35,
}

export const DEFAULT_DIGIT = 2;
export const DECIMAL_SETTINGS = {
    de_DE: {
        supportedDecimalSeparator: [CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.COMMAS,
        thousandSeparator: CHARACTER.DOT,
        currency: CURRENCY.EURO_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel,
    de_CH: {
        supportedDecimalSeparator: [CHARACTER.DOT, CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.DOT,
        thousandSeparator: CHARACTER.APOSTROPHE,
        currency: CURRENCY.CHF_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel,
    hu_HU: {
        supportedDecimalSeparator: [CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.COMMAS,
        thousandSeparator: CHARACTER.SPACE,
        currency: CURRENCY.CHF_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel,
    ro_RO: {
        supportedDecimalSeparator: [CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.COMMAS,
        thousandSeparator: CHARACTER.DOT,
        currency: CURRENCY.CHF_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel,
    cs_CZ: {
        supportedDecimalSeparator: [CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.COMMAS,
        thousandSeparator: CHARACTER.DOT,
        currency: CURRENCY.CZ_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel,
    sr_RS: {
        supportedDecimalSeparator: [CHARACTER.COMMAS],
        decimalSeparator: CHARACTER.COMMAS,
        thousandSeparator: CHARACTER.DOT,
        currency: CURRENCY.RS_CURRENCY,
        digits: 2
    } as SagCurrencySettingModel
};

export const MAX_QUANTITY = 9999;