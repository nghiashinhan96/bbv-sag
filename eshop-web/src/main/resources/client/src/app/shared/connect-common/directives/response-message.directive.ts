import { Directive, Input, ElementRef, OnInit, Renderer2, HostBinding } from '@angular/core';
import { ResponseMessage } from 'src/app/core/models/response-message.model';

@Directive({
    selector: '[connectResponseMessage]'
})
export class ResponseMessageDirective implements OnInit {
    @Input() responseMessage: ResponseMessage;

    @HostBinding('class.alert-danger') get danger() { return this.responseMessage.isError; }
    @HostBinding('class.alert-success') get success() { return !this.responseMessage.isError; }

    constructor(private el: ElementRef, private renderer: Renderer2) { }

    ngOnInit() {
        this.renderer.addClass(this.el.nativeElement, 'alert');
    }

}
