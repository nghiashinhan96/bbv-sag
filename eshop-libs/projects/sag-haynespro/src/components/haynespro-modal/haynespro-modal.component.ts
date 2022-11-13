import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-haynespro-modal',
    templateUrl: 'haynespro-modal.component.html',
    styleUrls: ['haynespro-modal.component.scss']
})
export class SagHaynessproModalComponent implements OnInit {
    @Input() vehicleInfo;
    @Input() haynesProOptions = [];
    @Input() getHaynesProParts: any;
    @Input() loginHaynesPro: any;
    @Input() reGenerateLoginUrl: any;
    @Input() haynesProRedirectUrl = '';
    @Input() haynesProSelect = ' ';

    constructor(
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
        if (!this.haynesProRedirectUrl) {
            this.loginHaynesPro(this.haynesProSelect, (url) => this.haynesProRedirectUrl = url);
        }
    }

    handleGetHaynesProParts() {
        if (this.getHaynesProParts) {
            this.bsModalRef.hide();
            this.getHaynesProParts();
        }
    }

    handleLoginHaynesPro() {
        if (this.loginHaynesPro) {
            this.loginHaynesPro(this.haynesProSelect, (url) => this.haynesProRedirectUrl = url);
        }
    }

    handleReGenerateLoginUrl() {
        window.open(this.haynesProRedirectUrl, '_blank');
        if (this.reGenerateLoginUrl) {
            this.reGenerateLoginUrl((url) => this.haynesProRedirectUrl = url);
        }
    }
}
