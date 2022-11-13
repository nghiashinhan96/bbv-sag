import { Input, Component, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';

@Component({
    selector: 'sag-common-dropdown-button',
    templateUrl: 'sag-dropdown-button.component.html'
})
export class SagDropdownButtonComponent {
    @Input() items = [];
    @Input() disabled = false;
    @Input() text;
    @Input() icon: string;
    @Input() params = {};
    @Input() css = 'btn-primary';
    @Input() type = 'button';

    @Output() selected = new EventEmitter();

    constructor(private cd: ChangeDetectorRef) { }

    onSelect(item) {
        this.selected.emit(item);
    }
}
