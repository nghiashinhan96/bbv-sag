import { Component, Input } from '@angular/core';
import { GT_TYPE } from '../../enums/gtmotive.enum';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-gtmotive-vin-warning-modal',
    templateUrl: './gt-vin-warning-modal.component.html'
})
export class SagGtmotiveVinWarningModalComponent {
    @Input() gtType: GT_TYPE;

    @Input() openGtmotive: any;
    @Input() enterVin: any;
    @Input() cancel: any;

    constructor(
        private bsModalRef: BsModalRef
    ) { }

    onOpenGtmotive() {
        this.close();

        if (this.openGtmotive) {
            this.openGtmotive();
        }
    }

    onEnterVin() {
        this.close();

        if (this.enterVin) {
            this.enterVin();
        }
    }

    onCancel() {
        this.close();

        if (this.cancel) {
            this.cancel();
        }
    }

    close() {
        this.bsModalRef.hide();
    }
}
