import { Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-haynespro-return-modal',
    templateUrl: 'haynespro-return-modal.component.html',
    styleUrls: ['haynespro-return-modal.component.scss']
})
export class SagHaynesProReturnModalComponent {
    index: number;
    vehicleId: string;
    callback: any;

    constructor(
        public modalRef: BsModalRef
    ) { }

    public retrieveDataFromHaynesPro() {
        if (!this.vehicleId) {
            return;
        }

        if (this.callback) {
            this.callback(this.vehicleId);
        }

        this.modalRef.hide();
    }
}
