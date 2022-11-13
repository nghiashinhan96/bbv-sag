import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-haynespro-error-component',
    templateUrl: 'haynespro-error-modal.component.html',
    styleUrls: ['haynespro-error-modal.component.scss']
})
export class SagHaynesproErrorModalComponent {
    @Input() isRequestedLicense: boolean;
    @Input() errorMessage: string;
    @Input() showRequestTrialLicense: boolean;
    @Input() requestTrialLicense: any;

    constructor(
        public bsModalRef: BsModalRef
    ) { }

    handleRequestTrialLicense() {
        if (this.requestTrialLicense) {
            this.requestTrialLicense();
        }
    }
}
