import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-gtmotive-multi-parts-modal',
    templateUrl: './gt-multi-parts-modal.component.html'
})
export class SagGtmotiveMultiPartsModalComponent {
    @Input() multiPartsData: any;
    @Input() selectPartFromMultiParts: any;
    @Input() dimissPart: any;
    @Input() close: any;

    disabled = true;

    constructor(
        private bsModalRef: BsModalRef
    ) { }

    handleSelectPartFromMultiParts(part, isMaintenance) {
        this.disabled = false;

        if (this.selectPartFromMultiParts) {
            this.selectPartFromMultiParts(part, isMaintenance);
        }
    }

    handleDimissPart(partCode, isMaintenance) {
        if (this.dimissPart) {
            this.dimissPart(partCode, isMaintenance);
        }
        this.handleClose();
    }

    handleClose() {
        if (this.close) {
            this.bsModalRef.hide();
            this.close();
        }
    }
}
