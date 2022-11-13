import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-gtmotive-multi-graphic-modal',
    templateUrl: './gt-multi-graphic-modal.component.html'
})
export class SagGtmotiveMultiGraphicModalComponent {
    @Input() gtLinks: any[];
    @Input() openGtmotiveForGtCode: any;

    constructor(
        public bsModalRef: BsModalRef
    ) {

    }

    onOpenGtmotiveForGtCode(link: any) {
        if (this.openGtmotiveForGtCode) {
            this.openGtmotiveForGtCode({
                umc: link.gt_umc,
                gtDrv: link.gt_drv,
                gtEng: link.gt_eng,
                gtMod: link.gt_mod
            });
        }
        this.bsModalRef.hide();
    }
}
