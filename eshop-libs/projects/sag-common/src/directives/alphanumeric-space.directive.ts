import { Directive, HostListener } from '@angular/core';
import { KEY_BOARD } from '../enums/keyboard.enum';

@Directive({
    selector: '[sagCommonAlphanumericSpace]'
})
export class SagCommonAlphanumericSpaceDirective {

    constructor() { }

    @HostListener('keypress', ['$event'])
    onkeypress(event: KeyboardEvent) {
        this.handleKeyPress(event);
    }

    private handleKeyPress(event: KeyboardEvent) {
        // tslint:disable-next-line: deprecation
        const keyCode = event.keyCode || event.charCode;
        if (keyCode === KEY_BOARD.ENTER) {
            return;
        }

        const isNumberKey = keyCode >= KEY_BOARD.ZERO && keyCode <= KEY_BOARD.NUMBER_NINE;
        const isAlphabetKey = (keyCode >= KEY_BOARD.A && keyCode <= KEY_BOARD.Z) ||
            (keyCode >= KEY_BOARD.a && keyCode <= KEY_BOARD.z);
        const isSpaceKey = keyCode === KEY_BOARD.SPACE;

        if (!isNumberKey && !isAlphabetKey && !isSpaceKey) {
            event.preventDefault();
            event.stopPropagation();
            return;
        }
    }

}
