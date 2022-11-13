import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'autonet-vehicle-info-dialog',
    templateUrl: './vehicle-info-dialog.component.html',
    styleUrls: ['./vehicle-info-dialog.component.scss']
})
export class VehicleInfoDialogComponent implements OnInit {
    @Input() vehicle;

    constructor(public bsModalRef: BsModalRef) { }

    ngOnInit() {

    }
}
