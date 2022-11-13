import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { BsModalRef } from "ngx-bootstrap/modal";
import { finalize } from "rxjs/operators";
import { LegalDocument } from "src/app/core/models/legal-document.model";
import { LegalTermService } from "src/app/core/services/legal-term.service";
import { SpinnerService } from "src/app/core/utils/spinner";

@Component({
    selector: 'connect-legal-document-detail-modal',
    templateUrl: './legal-document-detail-modal.component.html',
    styleUrls: ['legal-document-detail-modal.component.scss']
})
export class LegalDocumentDetailModalComponent implements OnInit, OnDestroy {
    @Input() token: string;
    @Input() document: LegalDocument;
    @Input() done: any;

    isAccepted = false;
    errorMessage = null;

    constructor(
        private bsModalRef: BsModalRef,
        private legalTermService: LegalTermService
    ) {

    }
    ngOnInit(): void {
        this.isAccepted = this.document.accepted;
    }

    ngOnDestroy(): void {
        this.done(this.document.accepted);
    }

    accept() {
        this.errorMessage = null;
        const spinner = SpinnerService.start('connect-legal-document-detail-modal .modal-body');
        this.legalTermService.acceptDocuments(this.document, this.token).pipe(
            finalize(() => SpinnerService.stop(spinner))
        ).subscribe(() => {
            if (this.done) {
                this.document.accepted = true;
                this.bsModalRef.hide();
            }
        }, () => {
            this.errorMessage = 'LOGIN.ERROR_MESSAGE.SYSTEM_ERROR'
        });
    }

    close() {
        this.bsModalRef.hide();
    }
}