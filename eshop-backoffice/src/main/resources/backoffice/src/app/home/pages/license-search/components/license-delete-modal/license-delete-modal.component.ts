import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
    selector: "backoffice-license-delete-modal",
    templateUrl: './license-delete-modal.component.html'
})
export class LicenseDeleteModalComponent implements OnInit, OnDestroy {
    @Input() license;
    @Output() deleteEventEmitter = new EventEmitter();
    @Output() closeModalEventEmitter = new EventEmitter();

    deletedPackName: string;

    private subs = new Subscription();

    constructor() { }

    ngOnInit(): void {
        if (this.license) {
            this.deletedPackName = this.license.packName;
        }
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    onDelete() {
        this.deleteEventEmitter.emit();
    }

    onCancel() {
        this.closeModalEventEmitter.emit();
    }
}
