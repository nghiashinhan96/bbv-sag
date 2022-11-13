import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'backoffice-delete-confirm-modal',
    templateUrl: './delete-confirm-modal.component.html',
    styleUrls: ['./delete-confirm-modal.component.scss']
})
export class DeleteConfirmModalComponent {

    @Input() modalTitle: string;
    @Input() deleteMessage: string;
    @Input() deleteItemName: string;
    @Input() noticeMessage: string;

    errorMessage: string;

    onConfirm: any;

    constructor(private bsModalRef: BsModalRef) { }

    hideDeleteModal() {
        this.bsModalRef.hide();
    }

    onError = (message, callback) => {
        this.errorMessage = message || '';
        if (callback) {
            callback();
        }
    }

    onSuccess = (message, callback) => {
        this.errorMessage = message || '';
        if (callback) {
            callback();
        }
    }

    confirmDelete() {
        if (this.onConfirm) {
            this.onConfirm(this.onError, this.onSuccess);
        }
    }
}
