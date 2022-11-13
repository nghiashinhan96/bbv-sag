import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'backoffice-message-delete-modal',
    templateUrl: './message-delete-modal.component.html',
    styleUrls: ['./message-delete-modal.component.scss']
})
export class MessageDeleteModalComponent implements OnInit {

    @Input() deletingMessage: any;
    @Input() resMessage: any;

    public closeModal: EventEmitter<any> = new EventEmitter();
    public confirmDelete: EventEmitter<any> = new EventEmitter();
    constructor() { }

    ngOnInit() {
    }

}
