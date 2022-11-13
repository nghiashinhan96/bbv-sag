import { Directive, Input, HostListener, ElementRef } from '@angular/core';

@Directive({
    selector: '[sagCommonTextEllipsis]'
})
export class SagCommonTextEllipsisDirective {
    @Input() libTextEllipsis = '';

    constructor(private el: ElementRef) { }

    @HostListener('mouseover')
    onMouseHover() {
        if (this.el.nativeElement.offsetWidth < this.el.nativeElement.scrollWidth) {
            const title = this.libTextEllipsis || this.el.nativeElement.innerText;
            if (title) {
                this.el.nativeElement.setAttribute('title', title);
            }
        } else {
            this.el.nativeElement.removeAttribute('title');
        }
    }

}
