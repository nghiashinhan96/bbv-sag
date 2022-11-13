import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

@Component({
    selector: 'connect-action',
    templateUrl: './action.component.html',
    styleUrls: ['./action.component.scss']
})
export class ConnectActionComponent implements OnInit {

    @Input() disabled = false;
    @Input() text;
    @Input() icon;
    @Input() customClass: string;
    @Output() isClicked = new EventEmitter();

    @Input() loading = false;
    @Output() loadingChange = new EventEmitter();

    @Input() forceDisable = false;
    @Output() forceDisableChange = new EventEmitter();

    @Input() name: string;
    constructor() { }

    ngOnInit(): void {

    }

    onClicked() {
        if (this.forceDisable || this.loading || this.disabled) {
            return;
        }
        this.loading = true;
        this.loadingChange.emit(this.loading);
        setTimeout(() => {
            this.isClicked.emit((status) => {
                setTimeout(() => {
                    this.loading = false;
                    this.loadingChange.emit(this.loading);
                    this.forceDisable = status;
                    this.forceDisableChange.emit(this.forceDisable);
                });
            });
        });
    }
}
