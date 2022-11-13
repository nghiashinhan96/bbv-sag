import { Directive, Input, ElementRef, HostListener, OnInit, forwardRef } from '@angular/core';
import { SAG_CURRENCY_FORMATTED_MODE } from '../currency.constant';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { SagNumericService } from '../services/numeric.service';
import { SagCurrencySettingModel } from '../models/currency-setting.model';

@Directive({
    selector: '[libNumeric]',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            multi: true,
            useExisting: forwardRef(() => SagNumericDirective),
        }
    ]
})
export class SagNumericDirective implements OnInit {
    @Input() libNumeric = true;
    @Input() formattedMode = SAG_CURRENCY_FORMATTED_MODE.CURRENCY;
    @Input() setting: SagCurrencySettingModel;
    @Input() ignoreShowRoundedPlaceholder = false;

    rawVal: any;
    onChange: any;
    onTouch: any;

    constructor(private el: ElementRef, private handler: SagNumericService) { }

    ngOnInit(): void {
        this.handler.updateSetting(this.setting);
    }

    @HostListener('keypress', ['$event'])
    onkeypress(event: KeyboardEvent) {
        this.handler.keyPress(event);
    }

    @HostListener('input')
    oninput() {
        this.rawValue = this.handler.applyFormat(this.rawValue);
        this.onChange(this.handler.clearFormat(this.rawValue));
    }

    @HostListener('blur', ['$event'])
    onblur(event) {
        try {
            this.onTouch().apply(event);
        } catch (e) {

        }
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouch = fn;
    }

    setDisabledState(isDisabled: boolean): void {
        this.el.nativeElement.disabled = isDisabled;
    }

    writeValue(val: any): void {
        if(this.ignoreShowRoundedPlaceholder && (val === '' || val === null)) {
            this.rawVal = val;
            return;
        }

        if (Number.isNaN(Number(val))) {
            val = '';
        }

        val = this.handler.fixedFormat(val, this.formattedMode);
        this.rawValue = this.handler.applyFormat(val + '');
    }


    get rawValue(): string {
        return this.el.nativeElement && this.el.nativeElement.value;
    }

    set rawValue(value: string) {
        if (this.el.nativeElement) {
            this.el.nativeElement.value = value;
        }
    }

}
