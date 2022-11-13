import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-happy-point-dialog',
    templateUrl: './happy-point-dialog.component.html',
    styleUrls: ['./happy-point-dialog.component.scss']
})
export class HappyPointDialogComponent implements OnInit {

    close: any;
    constructor(
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() { }

    saveAccetpTerm() {
        this.close();
        this.bsModalRef.hide();
    }
}
