import { Directive, ElementRef, HostListener, Input } from '@angular/core';
import { StringHelper } from 'src/app/core/utils/string.util';
@Directive({ selector: '[boNumberField]' })
export class NumberFieldDirective {
    el: ElementRef;
    constructor(el: ElementRef) {
        this.el = el;
    }

    @HostListener('input') onMouseEnter() {
        this.el.nativeElement.value = StringHelper.removeNonDigits(this.el.nativeElement.value);
    }
}
