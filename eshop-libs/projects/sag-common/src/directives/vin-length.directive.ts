import { Directive, HostListener, ElementRef, Renderer2 } from '@angular/core';
import { SAG_COMMON_VIN_MAX_LENGTH } from '../constants/sag-common.constant';

@Directive({
    selector: '[sagCommonVinLength]'
})
export class SagCommonVinLengthDirective {

    constructor(private el: ElementRef, private renderer2: Renderer2) { }
    @HostListener('keypress', ['$event'])
    onKeyPress(event: KeyboardEvent) {
        const val = this.el.nativeElement.value || '';
        const nonspaceval = val.replace(/\s/g, '');
        const vinMaxLength = val.length - nonspaceval.length + SAG_COMMON_VIN_MAX_LENGTH;
        this.renderer2.setAttribute(this.el.nativeElement, 'maxlength', vinMaxLength.toString());
    }
    @HostListener('paste', ['$event'])
    onPaste(event: any) {
        // tslint:disable-next-line: no-string-literal
        const text = (event.clipboardData || window['clipboardData']).getData('text').trim();
        let vinMaxLength = text.length > SAG_COMMON_VIN_MAX_LENGTH ? SAG_COMMON_VIN_MAX_LENGTH : text.length;
        // If the character is whitespace, increasing the max length to retain the text
        for (let i = 0; i < vinMaxLength; i++) {
            if (text[i].match(/\s/g) && vinMaxLength >= SAG_COMMON_VIN_MAX_LENGTH) {
                vinMaxLength++;
            }
        }
        this.renderer2.setAttribute(this.el.nativeElement, 'maxlength', vinMaxLength.toString());
        const convertedVal = text.substr(0, vinMaxLength);
        this.renderer2.setValue(this.el.nativeElement, convertedVal);
    }
}
