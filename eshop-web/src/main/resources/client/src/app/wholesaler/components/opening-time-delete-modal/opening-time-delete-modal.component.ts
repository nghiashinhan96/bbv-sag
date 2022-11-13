import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-opening-time-delete-modal',
    templateUrl: './opening-time-delete-modal.component.html',
    styleUrls: ['./opening-time-delete-modal.component.scss']
})
export class OpeningTimeDeleteModalComponent implements OnInit {
    @Input() modalTitle: string;
    @Input() deleteMessage: string;
    @Input() deleteItemName: string;
    @Input() noticeMessage: string;
    onConfirm: any;

    constructor(private bsModalRef: BsModalRef) { }

    ngOnInit() {
    }

    hideDeleteModal() {
        this.bsModalRef.hide();
    }

    confirmDelete() {
        if (this.onConfirm) {
            this.onConfirm();
        }
    }
}
