import {
    Directive, Input, ElementRef, OnInit,
    HostListener, forwardRef, Renderer2
} from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { DecimalSettingModel } from './decimal-setting.model';
import { DecimalNumberService } from './decimal-number.service';
import { FORMATTED_MODE } from './decimal-number.constant';


@Directive({
    selector: '[appDecimalNumber]',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            multi: true,
            useExisting: forwardRef(() => DecimalNumberDirective),
        },
        DecimalNumberService
    ]
})
export class DecimalNumberDirective implements OnInit, ControlValueAccessor {
    @Input() appDecimalNumber = true;
    @Input() setting: DecimalSettingModel;
    @Input() formattedMode = FORMATTED_MODE.OTHER;
    rawVal: any;
    onChange: Function;
    onTouch: Function;
    constructor(private el: ElementRef, private render2: Renderer2, private handler: DecimalNumberService) { }

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
