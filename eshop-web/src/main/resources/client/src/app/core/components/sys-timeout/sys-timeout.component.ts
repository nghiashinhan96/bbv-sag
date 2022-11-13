import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-sys-timeout',
    templateUrl: './sys-timeout.component.html',
    styleUrls: ['./sys-timeout.component.scss']
})
export class SysTimeoutComponent {
    constructor(private bsModalRef: BsModalRef) { }
    confirm() {
        this.bsModalRef.hide();
    }

}
