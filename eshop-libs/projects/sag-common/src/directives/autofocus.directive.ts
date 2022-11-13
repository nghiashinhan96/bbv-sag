import { AfterViewInit, Directive, ElementRef } from '@angular/core';

@Directive({
    selector: '[sagCommonAutofocus]'
})
export class SagCommonAutofocusDirective implements AfterViewInit {
    constructor(private element: ElementRef) { }

    ngAfterViewInit() {
        setTimeout(() => {
            this.element.nativeElement.focus();
        });
    }
}
