import { Directive, Output, EventEmitter, HostListener, ElementRef, Input } from '@angular/core';

@Directive({
    selector: '[scrollTracker]'
})
export class ScrollTrackerDirective {
    @Output() onScrollEnd = new EventEmitter<void>();
    @Input() scrollThreshold = 20; // px

    constructor(private element: ElementRef) { }

    @HostListener('scroll')
    public onScroll() {
        const scrolled = this.element.nativeElement.scrollTop;
        const height = this.element.nativeElement.offsetHeight;
        const clienHeigth = this.element.nativeElement.clientHeight;
        if (height - scrolled >= (clienHeigth - this.scrollThreshold)) {
            this.onScrollEnd.emit();
        }

    }
}