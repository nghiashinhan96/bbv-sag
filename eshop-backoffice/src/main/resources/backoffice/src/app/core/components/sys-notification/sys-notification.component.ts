import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-sys-notification',
    templateUrl: './sys-notification.component.html',
    styleUrls: ['./sys-notification.component.scss']
})
export class SysNotificationComponent {
    @Input() message = '';
    close: any;

    constructor(private bsModalRef: BsModalRef) { }

    cancel() {
        this.bsModalRef.hide();
    }

    confirm() {
        if (this.close) {
            this.close();
        }
        this.bsModalRef.hide();
    }

}
