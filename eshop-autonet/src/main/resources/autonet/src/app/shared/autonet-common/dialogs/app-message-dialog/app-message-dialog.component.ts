import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'autonet-app-message-dialog',
    templateUrl: './app-message-dialog.component.html',
    styleUrls: ['./app-message-dialog.component.scss']
})
export class AppMessageDialogComponent implements OnInit {

    @Input() messageCode: string;
    @Input() message: string;
    @Input() titleCode: string;
    @Input() title: string;

    constructor(public bsModalRef: BsModalRef) { }

    ngOnInit() {

    }

}
