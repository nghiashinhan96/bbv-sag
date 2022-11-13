import { Directive, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, Renderer2, SimpleChanges } from "@angular/core";

@Directive({
    selector: '[inputState]'
})

export class InputStateDirective implements OnChanges {
    @Input() inputState: 'success' | 'error';
    @Output() inputStateChange = new EventEmitter<any>();

    constructor(
        private elementRef: ElementRef,
        private render: Renderer2
    ) {
        this.render.addClass(this.elementRef.nativeElement, 'input-state-section');
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.inputState && changes.inputState.currentValue) {
            this.inputState = changes.inputState.currentValue;
            this.handleUpdateState(this.inputState);
        }
    }

    private handleUpdateState(state) {
        this.render.removeClass(this.elementRef.nativeElement, 'error');
        this.render.removeClass(this.elementRef.nativeElement, 'success');

        setTimeout(() => {
            this.render.addClass(this.elementRef.nativeElement, state);
            setTimeout(() => {
                this.inputStateChange.emit(null);
            }, 1000);
        }, 0);
    }
}