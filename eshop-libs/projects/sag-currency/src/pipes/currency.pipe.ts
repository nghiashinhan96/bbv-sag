import { Pipe, PipeTransform } from '@angular/core';
import { SagNumericService } from '../services/numeric.service';
import { SAG_CURRENCY_FORMATTED_MODE } from '../currency.constant';

@Pipe({
    name: 'sagCurrency'
})
export class SagCurrencyPipe implements PipeTransform {

    constructor(private handler: SagNumericService) { }

    transform(val: any, options: any = {}): string {
        if (Number.isNaN(Number(val))) {
            val = '';
        }
        const setting = options.setting;
        const isShownCurrency = options && options.isShownCurrency;
        const digits = options && options.digits;
        const isAllowNegative = options && options.isAllowNegative;
        this.handler.updateSetting(setting);
        val = this.handler.fixedFormat(Number(val), SAG_CURRENCY_FORMATTED_MODE.CURRENCY, digits);
        return this.handler.applyFormat(val + '', {
            isAllowNegative,
            digits,
            enableCurrency: !!isShownCurrency
        });
    }

}
