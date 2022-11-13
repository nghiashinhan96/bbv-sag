import { Injectable, Injector } from "@angular/core";
import { BsModalService } from "ngx-bootstrap/modal";
import { from, of } from "rxjs";
import { LegalDocumentDetailModalComponent } from "src/app/authentication/components/legal-document-detail-modal/legal-document-detail-modal.component";
import { LegalDocumentListModalComponent } from "src/app/authentication/components/legal-document-list-modal/legal-document-list-modal.component";
import { LegalDocument } from "../models/legal-document.model";
import { AppModalService } from "./app-modal.service";

@Injectable({
    providedIn: 'root'
})
export class LegalTermModalService {

    constructor(
        private injector: Injector,
    ) { }

    get bsModalService() {
        return this.injector.get(BsModalService);
    }

    get appModal() {
        return this.injector.get(AppModalService);
    }

    showLegalTermInfo(documents: LegalDocument[], token: string) {
        if (!documents || documents.length === 0) {
            return of (true);
        }
        if (documents.length === 1) {
            return this.showLogalDocumentDetail(documents[0], token);
        }
        return this.showLegalDocumentList(documents, token);
    }

    showLegalDocumentList(documents: LegalDocument[], token: string) {
        return from(new Promise(resolve => {
            this.appModal.modals = this.bsModalService.show(LegalDocumentListModalComponent, {
                initialState: {
                    token,
                    documents,
                    done: (canContinue) => {
                        resolve(canContinue);
                    }
                },
                class: 'modal-lg',
                ignoreBackdropClick: true
            })
        }));
    }

    showLogalDocumentDetail(document: LegalDocument, token: string) {
        return from(new Promise(resolve => {
            this.appModal.modals = this.bsModalService.show(LegalDocumentDetailModalComponent, {
                initialState: {
                    token,
                    document,
                    done: (accepted) => {
                        if (accepted) {
                            resolve(true);
                        } else {
                            resolve(!!document.daysLeft)
                        }
                    }
                },
                class: 'modal-lg',
                ignoreBackdropClick: true
            })
        }));
    }
}