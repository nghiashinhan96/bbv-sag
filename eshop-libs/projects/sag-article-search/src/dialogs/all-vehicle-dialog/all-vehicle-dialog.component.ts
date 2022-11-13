import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-search-vehicle-dialog',
    templateUrl: './all-vehicle-dialog.component.html',
    styleUrls: ['./all-vehicle-dialog.component.scss']
})
export class SagSearchVehicleDialogComponent implements OnInit {

    @Input() vehicles: number;

    closing: any;
    constructor(public bsModalRef: BsModalRef) { }

    ngOnInit() {

    }

    navigateTo(event, vehicleId: string) {
        event.preventDefault();
        event.stopPropagation();
        if (this.closing) {
            this.closing(vehicleId);
        }
        this.bsModalRef.hide();
    }

}
