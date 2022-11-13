import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

@Component({
    selector: 'sag-common-action',
    templateUrl: './sag-action.component.html',
    styleUrls: ['./sag-action.component.scss']
})
export class SagActionComponent implements OnInit {

    @Input() disabled = false;
    @Input() text;
    @Input() icon;
    @Input() customClass: string;
    @Output() isClicked = new EventEmitter();

    @Input() loading = false;
    @Output() loadingChange = new EventEmitter();

    forceDisable = false;
    constructor() { }

    ngOnInit(): void {

    }

    onClicked(event) {
        event.preventDefault();
        event.stopPropagation();
        this.loading = true;
        this.loadingChange.emit(this.loading);
        setTimeout(() => {
            this.isClicked.emit((status) => {
                setTimeout(() => {
                    this.loading = false;
                    this.loadingChange.emit(this.loading);
                    this.forceDisable = status;
                });
            });
        });
    }
}
