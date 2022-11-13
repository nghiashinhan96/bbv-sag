import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-common-confirmation-box',
    templateUrl: './sag-confirmation-box.component.html'
})
export class SagConfirmationBoxComponent implements OnInit {
    @Input() title = 'COMMON_LABEL.CONFIRMATION';
    @Input() message;
    @Input() messageParams = {};
    @Input() okButton = 'COMMON_LABEL.ACCEPT';
    @Input() cancelButton = 'COMMON_LABEL.ABORT';
    @Input() showCancelButton = true;
    @Input() showHeaderIcon = true;
    @Input() showCloseButton = false;
    @Input() bodyIcon: string;
    cancel: () => void | Promise<any>;
    close: () => void | Promise<any>;

    result;
    constructor(
        private bsModalRef: BsModalRef
    ) { }

    ngOnInit(): void {
        // throw new Error("Method not implemented.");
    }

    async onCancel() {
        if (this.cancel) {
            this.cancel();
        }
        this.bsModalRef.hide();
    }

    async confirm() {
        if (this.close) {
            this.result = await Promise.resolve(this.close());
        }
        if (!this.result) {
            this.bsModalRef.hide();
        }

    }

    hideModel() {
        this.bsModalRef.hide();
    }
}
