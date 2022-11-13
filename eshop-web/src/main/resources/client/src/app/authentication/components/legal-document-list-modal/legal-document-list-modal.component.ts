import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { LegalDocument } from "src/app/core/models/legal-document.model";
import { AppModalService } from "src/app/core/services/app-modal.service";
import { LegalTermService } from "src/app/core/services/legal-term.service";
import { LegalDocumentDetailModalComponent } from "../legal-document-detail-modal/legal-document-detail-modal.component";

@Component({
    selector: 'connect-legal-document-list-modal',
    templateUrl: './legal-document-list-modal.component.html',
    styleUrls: ['legal-document-list-modal.component.scss']
})
export class LegalDocumentListModalComponent implements OnInit, OnDestroy {
    @Input() token: string;
    @Input() documents: LegalDocument[];
    @Input() done: any;

    constructor(
        private bsModalRef: BsModalRef,
        private bsModalService: BsModalService,
        private appModal: AppModalService,
        private legalTermService: LegalTermService
    ) {

    }

    ngOnInit(): void {
        this.documents.sort((a, b) => {
            if (a.accepted && !b.accepted) {
                return 1;
            }

            if (!a.accepted && b.accepted) {
                return -1;
            }

            if (!a.accepted) {
                const days = a.daysLeft - b.daysLeft;
                if (days != 0) {
                    return days;
                }
            }

            const sort = a.sort - b.sort;
            if (sort !== 0) {
                return sort;
            }

            if (a.name > b.name) {
                return 1;
            }
            if (a.name < b.name) {
                return -1;
            }

            return 0;
        })
    }

    ngOnDestroy(): void {
        const canProceed = this.legalTermService.canProceed(this.documents);
        this.done(canProceed);
    }

    get isAllTermsAccepted() {
        return this.documents.every(term => term.accepted);
    }

    showDocumentDetail(document: LegalDocument) {
        this.appModal.modals = this.bsModalService.show(LegalDocumentDetailModalComponent, {
            initialState: {
                token: this.token,
                document,
                done: (accepted) => {
                    document.accepted = accepted;
                }
            },
            class: 'modal-lg',
            ignoreBackdropClick: true
        });
    }

    close() {
        this.bsModalRef.hide();
    }
}