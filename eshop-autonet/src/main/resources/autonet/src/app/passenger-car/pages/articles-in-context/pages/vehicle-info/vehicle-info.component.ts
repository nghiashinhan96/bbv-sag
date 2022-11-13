import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { VehicleInfoDialogComponent } from '../vehicle-info-dialog/vehicle-info-dialog.component';

@Component({
    selector: 'autonet-vehicle-info',
    templateUrl: './vehicle-info.component.html',
    styleUrls: ['./vehicle-info.component.scss']
})
export class VehicleInfoComponent implements OnInit {

    @Input() vehicle: any;
    @Output() refresEmiter = new EventEmitter();
    @Output() openRequestPriceOffer = new EventEmitter();

    constructor(private modalService: BsModalService) { }

    ngOnInit() {
    }

    refresh() {
        this.refresEmiter.emit(true);
    }

    showInfo() {
        this.modalService.show(VehicleInfoDialogComponent, {
            initialState: {
                vehicle: this.vehicle
            }
        });
    }

    onOpenRequestPriceOffer() {
        this.openRequestPriceOffer.emit();
    }
}
