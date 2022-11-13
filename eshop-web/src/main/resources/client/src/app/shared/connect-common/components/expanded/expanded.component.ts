import { Component, Input, Output, EventEmitter, TemplateRef } from '@angular/core';

@Component({
    selector: 'connect-expanded',
    templateUrl: 'expanded.component.html',
    styleUrls: ['expanded.component.scss']
})
export class ExpandedComponent {
    @Input() title: string;
    @Input() expand = true;
    @Output() expandChange = new EventEmitter();
    @Input() size: 'sm' | 'lg' = 'lg';
    @Input() customClass = '';
    @Input() titleTemplate: TemplateRef<any>;

    constructor() { }

    toggle() {
        this.expand = !this.expand;
        this.expandChange.emit(this.expand);
    }
}
