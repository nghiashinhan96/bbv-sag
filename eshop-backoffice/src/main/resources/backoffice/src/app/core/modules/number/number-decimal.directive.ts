import { Directive, ElementRef, HostListener, Input } from '@angular/core';

import { StringHelper } from '../../utils/string.util';
import { BIGGEST_INT } from '../../conts/app.constant';

@Directive({ selector: '[sagNumberDecimal]' })
export class NumberDecimalDirective {
    el: ElementRef;
    @Input() isDecimalNumber = false;
    @Input() digitAfterSeperator?: number = BIGGEST_INT;
    constructor(el: ElementRef) {
        this.el = el;
    }

    @HostListener('input') onMouseEnter() {
        if (this.isDecimalNumber) {
            this.el.nativeElement.value = this.removeInvalidCharDecimalFormat(
                this.el.nativeElement.value,
                this.digitAfterSeperator
            );
        } else {
            this.el.nativeElement.value = StringHelper.removeNonDigits(
                this.el.nativeElement.value
            );
        }
    }

    private removeInvalidCharDecimalFormat(
        value: string,
        digitAfterSeperator: number
    ): string {
        const cleanValue = value.replace(/[^0-9.]/g, '');
        let element = cleanValue.split('.');
        if (element.length > 1) {
            element[0] = element[0] + '.';
            element = element.join('').split('.');
            element[1] = element[1].substr(0, digitAfterSeperator);
        }
        return element.join('.');
    }
}
